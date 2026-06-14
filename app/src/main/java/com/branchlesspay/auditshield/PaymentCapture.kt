package com.branchlesspay.auditshield

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat

/**
 * Listens for Sunmi payment broadcasts on real Sunmi hardware.
 * Drop PayLib AAR into app/libs/ for direct SDK callbacks (see docs/SDK_SETUP.md).
 */
class SunmiPaymentCapture(
    private val context: Context,
) : PaymentCapture {
    companion object {
        private const val TAG = "SunmiPaymentCapture"
        val SUNMI_ACTIONS = listOf(
            "sunmi.payment.action.success",
            "com.sunmi.payment.action.SUCCESS",
            "woyou.aidlservice.jiuiv5.PAY_SUCCESS",
        )
    }

    override val sourceName: String = "sunmi_sdk"

    private var callback: ((PaymentEvent) -> Unit)? = null
    private var receiver: BroadcastReceiver? = null

    override fun start(onPayment: (PaymentEvent) -> Unit) {
        stop()
        callback = onPayment
        receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                if (intent == null) return
                parsePaymentIntent(intent)?.let { event ->
                    Log.i(TAG, "Payment captured tx=${event.transactionId} amount=${event.amountCents}")
                    callback?.invoke(event)
                }
            }
        }
        val filter = IntentFilter()
        SUNMI_ACTIONS.forEach { filter.addAction(it) }
        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
        Log.i(TAG, "Sunmi payment listener active on ${Build.MANUFACTURER}")
    }

    override fun stop() {
        receiver?.let { runCatching { context.unregisterReceiver(it) } }
        receiver = null
        callback = null
    }

    internal fun parsePaymentIntent(intent: Intent): PaymentEvent? {
        val extras = mutableMapOf<String, String?>()
        intent.extras?.keySet()?.forEach { key ->
            extras[key] = intent.extras?.get(key)?.toString()
        }
        extras["transactionId"] = intent.getStringExtra("transactionId") ?: extras["transactionId"]
        extras["transId"] = intent.getStringExtra("transId") ?: extras["transId"]
        extras["orderId"] = intent.getStringExtra("orderId") ?: extras["orderId"]
        extras["amount"] = intent.getStringExtra("amount") ?: extras["amount"]
        extras["currency"] = intent.getStringExtra("currency") ?: extras["currency"]
        extras["payType"] = intent.getStringExtra("payType") ?: extras["payType"]
        return SunmiPaymentParser.parseExtras(extras)
    }
}

object PaymentCaptureFactory {
    fun create(context: Context): PaymentCapture {
        val manufacturer = Build.MANUFACTURER.orEmpty()
        return if (manufacturer.equals("SUNMI", ignoreCase = true)) {
            SunmiPaymentCapture(context.applicationContext)
        } else {
            DebugPaymentCapture()
        }
    }
}

/**
 * Emulator / non-Sunmi devices — payments triggered via service simulate action.
 */
class DebugPaymentCapture : PaymentCapture {
    override val sourceName: String = "debug_simulator"

    private var callback: ((PaymentEvent) -> Unit)? = null

    override fun start(onPayment: (PaymentEvent) -> Unit) {
        callback = onPayment
    }

    override fun stop() {
        callback = null
    }

    fun simulatePayment(event: PaymentEvent = PaymentEvent.simulated()) {
        callback?.invoke(event)
    }
}
