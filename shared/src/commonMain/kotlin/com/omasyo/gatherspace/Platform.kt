package com.omasyo.gatherspace

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform