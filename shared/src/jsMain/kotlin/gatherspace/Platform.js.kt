package com.omasyo.gatherspace

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/Js"
}

actual fun getPlatform(): Platform = JsPlatform()

private val testFlow = flow<String> {
    repeat(10) {
        delay(1000)
        emit("This is not a drill $it")
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
class Test(private val name: String) {
    fun callback(onEvent: (event: String) -> Unit) {
        GlobalScope.launch {
            testFlow.collect {
                onEvent("$name says $it")
            }
        }
    }
}