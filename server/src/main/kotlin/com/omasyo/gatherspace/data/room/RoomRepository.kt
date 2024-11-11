package com.omasyo.gatherspace.data.room

import com.omasyo.gatherspace.database.Database
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.User

fun createRoomRepository(database: Database): RoomRepository =
    RoomRepositoryImpl(database.roomQueries, database.room_memberQueries)

interface RoomRepository {
    fun create(name: String, description: String, image: String?): Int

    fun updateRoom(name: String?, description: String?, image: String?, roomId: Int)

    fun addMembers(roomId: Int, userIds: List<Int>)

    fun removeMembers(roomId: Int, userIds: List<Int>)

    fun getMembers(roomId: Int): List<User>

    fun getRoom(roomId: Int): RoomDetails?

    fun getUserRooms(userId: Int): List<Room>

    fun getAllRooms(): List<Room>
}