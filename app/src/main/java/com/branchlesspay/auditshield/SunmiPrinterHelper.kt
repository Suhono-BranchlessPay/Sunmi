package com.branchlesspay.auditshield

import android.content.Context
import android.util.Log
import com.sunmi.peripheral.printer.InnerPrinterCallback
import com.sunmi.peripheral.printer.InnerPrinterManager
import com.sunmi.peripheral.printer.SunmiPrinterService

/**
 * Prints verify URL as QR code on Sunmi built-in printer.
 * Falls back gracefully on emulator / non-Sunmi devices.
 */
object SunmiPrinterHelper {
    private const val TAG = "SunmiPrinterHelper"

    fun printVerifyQr(
        context: Context,
        verifyUrl: String,
        onResult: (Boolean, String) -> Unit,
    ) {
        try {
            InnerPrinterManager.getInstance().bindService(
                context.applicationContext,
                object : InnerPrinterCallback() {
                    override fun onConnected(service: SunmiPrinterService?) {
                        if (service == null) {
                            onResult(false, context.getString(R.string.printer_unavailable))
                            return
                        }
                        try {
                            service.printQRCode(verifyUrl, 8, 2, null)
                            service.lineWrap(3, null)
                            onResult(true, "ok")
                        } catch (exc: Exception) {
                            Log.e(TAG, "Print failed", exc)
                            onResult(false, exc.message ?: "print failed")
                        } finally {
                            runCatching {
                                InnerPrinterManager.getInstance().unBindService(
                                    context.applicationContext,
                                    this,
                                )
                            }
                        }
                    }

                    override fun onDisconnected() {
                        onResult(false, context.getString(R.string.printer_unavailable))
                    }
                },
            )
        } catch (exc: Exception) {
            Log.w(TAG, "Sunmi printer bind failed", exc)
            onResult(false, context.getString(R.string.printer_unavailable))
        }
    }
}
