package com.omasyo.gatherspace.models.request

import kotlinx.serialization.Serializable

@Serializable
data class
CreateRoomRequest(val name: String, val description: String)

@Serializable
data class MembersRequest(val members: List<Int>)

@Serializable
data class UserRoomRequest(val roomId: Int)