package com.branchlesspay.auditshield

import android.content.Context
import android.os.Build
import android.provider.Settings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

object DeviceInfo {
    fun model(): String = Build.MODEL ?: "unknown"

    fun serial(context: Context): String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Build.getSerial()
            } else {
                @Suppress("DEPRECATION")
                Build.SERIAL
            }
        } catch (_: SecurityException) {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
                ?: "unknown"
        }
    }
}

object BpAnchorPayload {
    fun testTransaction(context: Context): Map<String, Any> =
        buildTestTransaction(DeviceInfo.model(), DeviceInfo.serial(context))

    fun buildTestTransaction(deviceModel: String, deviceSn: String): Map<String, Any> {
        val suffix = UUID.randomUUID().toString().substring(0, 8).uppercase(Locale.US)
        return mapOf(
            "event_type" to "sunmi_transaction",
            "reference_id" to "TEST-$suffix",
            "amount" to 10000.0,
            "currency" to "IDR",
            "timestamp" to isoNow(),
            "vendor" to "sunmi",
            "merchant_id" to deviceSn,
            "metadata" to mapOf(
                "erp" to "sunmi_pos",
                "erp_system" to "Sunmi Android POS",
                "device_model" to deviceModel,
                "device_sn" to deviceSn,
            ),
        )
    }

    fun isoNow(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date())
    }
}
