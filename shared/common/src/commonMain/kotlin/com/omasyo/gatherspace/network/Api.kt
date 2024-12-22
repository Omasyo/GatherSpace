package com.omasyo.gatherspace.network

object Api {
    const val PROTOCOL = "http"
    val HOST: String = "192.168.96.113"//TODO("Set Server IP Address")
    const val PORT = 8080

    val IMAGE_URL_PATH = "$PROTOCOL://$HOST:$PORT/images/"
}