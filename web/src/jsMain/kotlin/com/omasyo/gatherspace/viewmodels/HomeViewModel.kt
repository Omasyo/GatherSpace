package com.omasyo.gatherspace.viewmodels

import com.omasyo.gatherspace.home.HomeViewModel
import com.omasyo.gatherspace.home.HomeViewModelImpl

val homeViewModel: HomeViewModel = with(domainComponent) {
    HomeViewModelImpl(roomRepository, userRepository)
}