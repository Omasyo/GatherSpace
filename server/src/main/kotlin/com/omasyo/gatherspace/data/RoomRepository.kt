package com.omasyo.gatherspace.data

import com.omasyo.gatherspace.models.Room
import com.omasyo.gatherspace.models.User
import com.omasyo.gatherspace.models.UserDetails
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

fun createRoomRepository(database: Database) = RoomRepositoryImpl(database.roomQueries, database.room_memberQueries)


interface RoomRepository {
    fun create(name: String)

    fun updateName(name: String, roomId: Int)

    fun addMember(roomId: Int, userId: Int)

    fun removeMember(roomId: Int, userId: Int)

    fun getMembers(roomId: Int): List<User>

    fun getRoom(roomId: Int): Room?
}

class RoomRepositoryImpl(
    private val roomQueries: RoomQueries,
    private val roomMemberQueries: Room_memberQueries
) : RoomRepository {
    override fun create(name: String) {
        roomQueries.create(name)
    }

    override fun updateName(name: String, roomId: Int) {
        roomQueries.update(name, roomId)
    }

    override fun addMember(roomId: Int, userId: Int) {
        roomMemberQueries.create(roomId, userId)
    }

    override fun removeMember(roomId: Int, userId: Int) {
        roomMemberQueries.delete(roomId, userId)
    }

    override fun getMembers(roomId: Int): List<User> {
        return roomMemberQueries.getRoomMembers(roomId, ::User).executeAsList()
    }

    override fun getRoom(roomId: Int): Room? {
        return roomQueries.transactionWithResult {
            val room = roomQueries.getRoomById(roomId).executeAsOneOrNull() ?: return@transactionWithResult null
            val members = getMembers(roomId)
            with(room) {
                Room(id, name, members, created.toKotlinLocalDateTime(), modified.toKotlinLocalDateTime())
            }
        }
    }
}