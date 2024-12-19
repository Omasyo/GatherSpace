package com.omasyo.gatherspace.profile

import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.models.response.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.io.Buffer

interface ProfileViewModel {

    val coroutineScope: CoroutineScope

    val state: StateFlow<ProfileScreenState>

    val userDetails: StateFlow<UserDetails?>

    val userSessions: StateFlow<List<UserSession>>

    fun updateImage(buffer: Buffer)

    fun logout()

    fun logoutSession(session: UserSession)

    fun onEventReceived(event: ProfileScreenEvent)
}