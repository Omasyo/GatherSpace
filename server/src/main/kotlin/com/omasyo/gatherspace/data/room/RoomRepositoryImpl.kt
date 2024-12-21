package com.omasyo.gatherspace.data.room

import com.omasyo.gatherspace.data.DatabaseResponse
import com.omasyo.gatherspace.data.user.userMapper
import com.omasyo.gatherspace.database.RoomQueries
import com.omasyo.gatherspace.database.Room_memberQueries
import com.omasyo.gatherspace.database.User_accountQueries
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.User
import com.omasyo.gatherspace.utils.ImageDirPath
import com.omasyo.gatherspace.utils.createImageFile
import com.omasyo.gatherspace.utils.getImagePath
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.io.Buffer
import java.io.File

internal class RoomRepositoryImpl(
    private val roomQueries: RoomQueries,
    private val roomMemberQueries: Room_memberQueries,
    private val userAccountQueries: User_accountQueries
) : RoomRepository {
    override fun create(
        name: String,
        description: String,
        creatorId: Int,
        imageBuffer: Buffer?
    ): DatabaseResponse<Int> =
        roomQueries.transactionWithResult {
            val imageId = imageBuffer?.let { createImageFile(it) }
            val roomId = roomQueries.create(name, description, imageId, creatorId).executeAsOne()
            roomMemberQueries.create(roomId, creatorId)
            DatabaseResponse.Success(roomId)
        }

    override fun updateRoom(name: String?, description: String?, imageBuffer: Buffer?, roomId: Int) =
        roomQueries.transaction {
            val room = roomQueries.getRoomById(roomId).executeAsOne()

            val newImageId = imageBuffer?.let {
                room.image?.let { id -> File(ImageDirPath, id).delete() }
                createImageFile(it)
            }
            roomQueries.update(
                name = name ?: room.name,
                description = description ?: room.description,
                image = newImageId ?: room.image,
                id = roomId
            )
        }

    override fun addMembers(roomId: Int, userIds: List<Int>) =
        roomMemberQueries.transaction {
            for (userId in userIds) {
                roomMemberQueries.create(roomId, userId)
            }
        }

    override fun removeMembers(roomId: Int, userIds: List<Int>) =
        roomMemberQueries.transaction {
            for (userId in userIds) {
                roomMemberQueries.delete(roomId, userId)
            }
        }

    override fun getMembers(roomId: Int): List<User> =
        roomMemberQueries.getRoomMembers(roomId, ::User).executeAsList()

    override fun getRoom(roomId: Int, userId: Int?): RoomDetails? =
        roomQueries.transactionWithResult {
            val room = roomQueries.getRoomById(roomId).executeAsOneOrNull()
                ?: return@transactionWithResult null
            val creator = room.creator?.let { userAccountQueries.getById(it, ::userMapper).executeAsOne() }

            val isMember = userId?.let { roomMemberQueries.contains(roomId, it).executeAsOne() } ?: false
            val members = getMembers(roomId)
            with(room) {
                RoomDetails(
                    id = id,
                    name = name,
                    imageUrl = image,
                    creator = creator,
                    isMember = isMember,
                    members = members,
                    created = created.toKotlinLocalDateTime(),
                    modified = modified.toKotlinLocalDateTime()
                )
            }
        }

    override fun getUserRooms(userId: Int): List<Room> =
        roomQueries.getUserRooms(userId) { id, name, image ->
            Room(id, name, getImagePath(image))
        }.executeAsList()

    override fun getAllRooms(): List<Room> =
        roomQueries.getAll { id, name, image ->
            Room(id, name, getImagePath(image))
        }.executeAsList()
}