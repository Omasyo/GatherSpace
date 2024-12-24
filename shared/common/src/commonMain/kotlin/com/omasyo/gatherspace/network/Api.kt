package com.omasyo.gatherspace.network

object Api {
    const val PROTOCOL = "http"
    val HOST: String = TODO("Set Server IP Address")
    const val PORT = 8080

    val IMAGE_URL_PATH = "$PROTOCOL://$HOST:$PORT/images/"
}