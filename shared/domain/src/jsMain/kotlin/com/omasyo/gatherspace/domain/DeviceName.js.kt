package com.omasyo.gatherspace.domain

import kotlinx.browser.window

actual fun getDeviceName(): String {
    val version = window.navigator.appVersion
    val osName = when {
        version.contains("Android") -> {
            val i = version.indexOf("Android")
            version.substring(i, i + 10).split(";").first()
        }

        version.contains("IPhone") -> "IPhone"
        version.contains("Win") -> "Windows"
        version.contains("Mac") -> "MacOS"
        version.contains("Unix") -> "Unix"
        version.contains("Linux") -> "Linux"
        else -> "Unknown OS"
    }

    val navigator = window.navigator

    val userAgent = navigator.userAgent

    val browser = when {
        userAgent.contains("OPR") -> "Opera"
        userAgent.contains("Edg") -> "Microsoft Edge"
        userAgent.contains("MSIE") -> "Microsoft Internet Explorer"
        userAgent.contains("Chrome") -> "Chrome"
        userAgent.contains("Safari") -> "Safari"
        userAgent.contains("Firefox") -> "Firefox"
        else -> {
            val nameOffset = userAgent.lastIndexOf(" ") + 1
            val verOffset = userAgent.lastIndexOf("/")
            userAgent.substring(nameOffset, verOffset)
        }
    }

    return "$osName - $browser"
}