package com.omasyo.gatherspace.data.room

import com.omasyo.gatherspace.data.DatabaseResponse
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.User
import kotlinx.io.Buffer

interface RoomRepository {
    fun create(name: String, description: String, creatorId: Int, imageBuffer: Buffer?): DatabaseResponse<Int>

    fun updateRoom(name: String?, description: String?, imageBuffer: Buffer?, roomId: Int)

    fun addMembers(roomId: Int, userIds: List<Int>)

    fun removeMembers(roomId: Int, userIds: List<Int>)

    fun getMembers(roomId: Int): List<User>

    fun getRoom(roomId: Int, userId: Int?): RoomDetails?

    fun getUserRooms(userId: Int): List<Room>

    fun getAllRooms(): List<Room>
}