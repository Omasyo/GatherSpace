package com.omasyo.gatherspace.data.room

import com.omasyo.gatherspace.database.Database
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.User

fun createRoomRepository(database: Database): RoomRepository =
    RoomRepositoryImpl(database.roomQueries, database.room_memberQueries)

interface RoomRepository {
    fun create(name: String)

    fun updateName(name: String, roomId: Int)

    fun addMembers(roomId: Int, userIds: List<Int>)

    fun removeMembers(roomId: Int, userIds: List<Int>)

    fun getMembers(roomId: Int): List<User>

    fun getRoom(roomId: Int): Room?
}