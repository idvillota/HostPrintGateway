package com.host.printgateway.data

import android.content.Context
import android.provider.Settings

/**
 * Stable per-install device id used by mobile-sync.
 * Same value as the previous inline Settings.Secure.ANDROID_ID reads.
 */
object DeviceIdProvider {
    fun get(context: Context): String =
        Settings.Secure.getString(
            context.applicationContext.contentResolver,
            Settings.Secure.ANDROID_ID,
        ) ?: "unknown-device"
}
