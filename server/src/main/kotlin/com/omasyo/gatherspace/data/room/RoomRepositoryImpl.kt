package com.omasyo.gatherspace.data.room

import com.omasyo.gatherspace.database.RoomQueries
import com.omasyo.gatherspace.database.Room_memberQueries
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.User
import kotlinx.datetime.toKotlinLocalDateTime


internal class RoomRepositoryImpl(
    private val roomQueries: RoomQueries,
    private val roomMemberQueries: Room_memberQueries,
) : RoomRepository {
    override fun create(name: String) {
        roomQueries.create(name)
    }

    override fun updateName(name: String, roomId: Int) {
        roomQueries.update(name, roomId)
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

    override fun getRoom(roomId: Int): RoomDetails? {
        return roomQueries.transactionWithResult {
            val room = roomQueries.getRoomById(roomId).executeAsOneOrNull()
                ?: return@transactionWithResult null

            val members = getMembers(roomId)
            with(room) {
                RoomDetails(
                    id = id,
                    name = name,
                    members = members,
                    created = created.toKotlinLocalDateTime(),
                    modified = modified.toKotlinLocalDateTime()
                )
            }
        }
    }

    override fun getAllRooms(): List<Room> {
        return roomQueries.getAll(::Room).executeAsList()
    }
}