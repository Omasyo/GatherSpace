package com.omasyo.gatherspace.data.room

import com.omasyo.gatherspace.data.DatabaseResponse
import com.omasyo.gatherspace.database.RoomQueries
import com.omasyo.gatherspace.database.Room_memberQueries
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.User
import com.omasyo.gatherspace.utils.ImageDirPath
import com.omasyo.gatherspace.utils.createImageFile
import com.omasyo.gatherspace.utils.fromDb
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.io.Buffer
import java.io.File

internal class RoomRepositoryImpl(
    private val roomQueries: RoomQueries,
    private val roomMemberQueries: Room_memberQueries,
) : RoomRepository {
    override fun create(name: String, description: String, imageBuffer: Buffer?): DatabaseResponse<Int> {
        return roomQueries.transactionWithResult {
            val imageId = imageBuffer?.let { createImageFile(it) }
            DatabaseResponse.Success(roomQueries.create(name, description, imageId).executeAsOne())
        }
    }

    override fun updateRoom(name: String?, description: String?, imageBuffer: Buffer?, roomId: Int) {
        roomQueries.transaction {
            val room = roomQueries.getRoomById(roomId).executeAsOne()

            val newImageId = imageBuffer?.let {
                room.image?.let { File(ImageDirPath, it).delete() }
                createImageFile(it)
            }
            roomQueries.update(
                name = name ?: room.name,
                description = description ?: room.description,
                image = newImageId ?: room.image,
                id = roomId
            )
        }
    }

    override fun addMembers(roomId: Int, userIds: List<Int>) {
        roomMemberQueries.transaction {
            for (userId in userIds) {
                roomMemberQueries.create(roomId, userId)
            }
        }
    }

    override fun removeMembers(roomId: Int, userIds: List<Int>) {
        roomMemberQueries.transaction {
            for (userId in userIds) {
                roomMemberQueries.delete(roomId, userId)
            }
        }
    }

    override fun getMembers(roomId: Int): List<User> {
        return roomMemberQueries.getRoomMembers(roomId, ::User).executeAsList()
    }

    override fun getRoom(roomId: Int, userId: Int?): RoomDetails? {
        return roomQueries.transactionWithResult {
            val room = roomQueries.getRoomById(roomId).executeAsOneOrNull()
                ?: return@transactionWithResult null

            val isMember = userId?.let { roomMemberQueries.contains(roomId, it).executeAsOne() } ?: false
            val members = getMembers(roomId)
            with(room) {
                RoomDetails(
                    id = id,
                    name = name,
                    imageUrl = image, //TODO image will have relative path
                    isMember = isMember,
                    members = members,
                    created = created.toKotlinLocalDateTime(),
                    modified = modified.toKotlinLocalDateTime()
                )
            }
        }
    }

    override fun getUserRooms(userId: Int): List<Room> {
        return roomQueries.getUserRooms(userId, Room::fromDb).executeAsList()
    }

    override fun getAllRooms(): List<Room> {
        return roomQueries.getAll(Room::fromDb).executeAsList()
    }
}