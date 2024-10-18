package com.omasyo.gatherspace

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}



@OptIn(ExperimentalJsExport::class)
@JsExport
 fun tospi(): String {
    return "Test";
}