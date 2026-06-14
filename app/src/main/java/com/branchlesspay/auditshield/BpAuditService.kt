package com.branchlesspay.auditshield

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * M1 stub — keeps process alive for future M2 payment listener integration.
 * Auto-started from MainActivity and BOOT_COMPLETED.
 */
class BpAuditService : Service() {
    companion object {
        private const val TAG = "BpAuditService"
        const val ACTION_START = "com.branchlesspay.auditshield.START"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "BP Audit background service started (M1 stub)")
        return START_STICKY
    }
}
