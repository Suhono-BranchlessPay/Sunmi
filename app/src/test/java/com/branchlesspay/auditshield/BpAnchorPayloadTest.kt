package com.branchlesspay.auditshield

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BpAnchorPayloadTest {
    @Test
    fun buildTestTransaction_hasSunmiFields() {
        val payload = BpAnchorPayload.buildTestTransaction("V2 Pro", "SN12345")
        assertEquals("sunmi_transaction", payload["event_type"])
        assertEquals("IDR", payload["currency"])
        assertEquals(10000.0, payload["amount"])
        @Suppress("UNCHECKED_CAST")
        val metadata = payload["metadata"] as Map<String, Any>
        assertEquals("sunmi_pos", metadata["erp"])
        assertEquals("V2 Pro", metadata["device_model"])
        assertEquals("SN12345", metadata["device_sn"])
        assertTrue((payload["reference_id"] as String).startsWith("TEST-"))
    }
}

class BpApiClientTest {
    @Test
    fun legacyContentHash_isDeterministic() {
        val payload = linkedMapOf<String, Any>(
            "event_type" to "sunmi_transaction",
            "reference_id" to "TEST-FIXED",
            "amount" to 10000.0,
            "currency" to "IDR",
            "timestamp" to "2026-06-14T00:00:00Z",
            "metadata" to mapOf("erp" to "sunmi_pos"),
        )
        val hash1 = BpApiClient.legacyContentHash(payload)
        val hash2 = BpApiClient.legacyContentHash(payload)
        assertEquals(hash1, hash2)
        assertEquals(64, hash1.length)
    }

    @Test
    fun postAnchor_withoutLicenseKey_failsFast() {
        val result = BpApiClient(licenseKey = "").postAnchor(
            BpAnchorPayload.buildTestTransaction("T2mini", "dev-sn"),
        )
        assertTrue(!result.ok)
        assertEquals("BP license key is not configured", result.error)
    }
}
