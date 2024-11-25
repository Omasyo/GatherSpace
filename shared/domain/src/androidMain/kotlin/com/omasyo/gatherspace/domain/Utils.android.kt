package com.omasyo.gatherspace.domain

import android.os.Build

actual fun getDeviceName(): String {
    return Build.MANUFACTURER + " " + Build.MODEL
}