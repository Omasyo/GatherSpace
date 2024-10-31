package com.omasyo.gatherspace.models.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(val name: String)

@Serializable
data class MembersRequest(val members: List<Int>)