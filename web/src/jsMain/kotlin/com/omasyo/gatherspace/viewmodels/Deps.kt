package com.omasyo.gatherspace.viewmodels

import com.omasyo.gatherspace.domain.auth.WebTokenStorage
import com.omasyo.gatherspace.domain.deps.DomainComponent
import com.omasyo.gatherspace.network.deps.NetworkComponent
import kotlinx.coroutines.Dispatchers


val tokenStorage = WebTokenStorage()

val networkComponent = NetworkComponent(tokenStorage)

val domainComponent = DomainComponent(networkComponent, tokenStorage, Dispatchers.Main)
