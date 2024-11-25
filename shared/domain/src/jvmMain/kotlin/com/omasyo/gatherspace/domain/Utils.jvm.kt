package com.omasyo.gatherspace.domain

import java.net.InetAddress

actual fun getDeviceName(): String {
    return InetAddress.getLocalHost().hostName
}