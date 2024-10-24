package com.omasyo.gatherspace.network

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*

actual fun provideEngine(): HttpClientEngine {
    return CIO.create()
}