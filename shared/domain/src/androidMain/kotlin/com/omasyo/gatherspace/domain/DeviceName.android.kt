package com.omasyo.gatherspace.domain

import android.os.Build

actual fun getDeviceName(): String = Build.MANUFACTURER + " " + Build.MODEL