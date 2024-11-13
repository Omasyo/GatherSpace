package com.omasyo.gatherspace.data.room

import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.User

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