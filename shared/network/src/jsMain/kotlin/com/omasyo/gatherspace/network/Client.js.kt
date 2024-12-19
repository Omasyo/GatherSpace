package com.omasyo.gatherspace.network

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

actual fun provideEngine(): HttpClientEngine = Js.create()
