package com.omasyo.gatherspace.common

import com.omasyo.gatherspace.home.HomeViewModel
import com.omasyo.gatherspace.home.HomeViewModelImpl

val homeViewModel: HomeViewModel = with(domainComponent) {
    HomeViewModelImpl(roomRepository, userRepository, authRepository)
}

