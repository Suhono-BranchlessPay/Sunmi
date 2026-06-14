package com.branchlesspay.auditshield

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TransactionFormatterTest {
    @Test
    fun statusLabels() {
        assertEquals("Pending", TransactionFormatter.statusLabel(QueueStatus.PENDING))
        assertEquals("Anchored", TransactionFormatter.statusLabel(QueueStatus.ANCHORED))
        assertEquals("Failed", TransactionFormatter.statusLabel(QueueStatus.FAILED))
    }

    @Test
    fun formatAmount_fromPayloadJson() {
        val json = """{"amount":25000,"currency":"IDR"}"""
        assertEquals("Rp 25000", TransactionFormatter.formatAmount(json))
    }

    @Test
    fun buildVerifyUrl() {
        val url = TransactionFormatter.buildVerifyUrl("abc-123")
        assertEquals("https://branchlesspay.com/verify/abc-123", url)
    }

    @Test
    fun formatMoney_usd() {
        assertEquals("$10.50", TransactionFormatter.formatMoney(10.5, "USD"))
    }
}

class SunmiPaymentParserTest {
    @Test
    fun parseExtras_standardFields() {
        val event = SunmiPaymentParser.parseExtras(
            mapOf(
                "transactionId" to "TX-1001",
                "amount" to "150.00",
                "currency" to "IDR",
                "payType" to "QRIS",
            ),
        )
        assertEquals("TX-1001", event!!.transactionId)
        assertEquals(15000L, event.amountCents)
        assertEquals("QRIS", event.paymentMethod)
    }

    @Test
    fun parseExtras_missingAmount_returnsNull() {
        assertEquals(null, SunmiPaymentParser.parseExtras(mapOf("transactionId" to "TX-1")))
    }
}

class AnchorProcessorTest {
    private val event = PaymentEvent(
        transactionId = "PAY-UNIT-1",
        amountCents = 25000,
        currency = "IDR",
        paymentMethod = "card",
    )

    @Test
    fun processPayment_onlineSuccess_recordsHistory() {
        val queue = InMemoryAnchorQueue()
        val processor = AnchorProcessor(
            queue = queue,
            apiClientFactory = {
                object : BpApiClient("test-key") {
                    override fun postAnchor(payload: Map<String, Any>): BpAnchorResult =
                        BpAnchorResult(
                            ok = true,
                            httpStatus = 202,
                            anchorId = "anchor-1",
                            verifyUrl = "https://branchlesspay.com/verify/anchor-1",
                            status = "queued",
                            contentHash = "hash",
                            error = null,
                            rawBody = "{}",
                        )
                }
            },
            isOnline = { true },
        )
        val result = processor.processPayment(event, "V2", "SN1")
        assertTrue(result.ok)
        assertEquals(0, queue.countPending())
        assertEquals(1, queue.countAnchored())
    }

    @Test
    fun processPayment_offlineQueues() {
        val queue = InMemoryAnchorQueue()
        val processor = AnchorProcessor(
            queue = queue,
            apiClientFactory = { BpApiClient("test-key") },
            isOnline = { false },
        )
        val result = processor.processPayment(event, "V2", "SN1")
        assertTrue(result.queuedOffline)
        assertEquals(1, queue.countPending())
    }

    @Test
    fun flushQueue_anchorsPendingItems() {
        val queue = InMemoryAnchorQueue()
        val payload = BpAnchorPayload.buildPayment(event, "V2", "SN1")
        queue.enqueue(event.transactionId, "sunmi_payment", payload)
        val mockProcessor = AnchorProcessor(
            queue = queue,
            apiClientFactory = {
                object : BpApiClient("test-key") {
                    override fun postAnchor(payload: Map<String, Any>): BpAnchorResult =
                        BpAnchorResult(
                            ok = true,
                            httpStatus = 202,
                            anchorId = "a2",
                            verifyUrl = "https://branchlesspay.com/verify/a2",
                            status = "queued",
                            contentHash = "h",
                            error = null,
                            rawBody = "{}",
                        )
                }
            },
            isOnline = { true },
        )
        val flush = mockProcessor.flushQueue()
        assertEquals(1, flush.anchored)
        assertEquals(1, queue.countAnchored())
    }

    @Test
    fun flushQueue_maxRetryMarksFailed() {
        val queue = InMemoryAnchorQueue()
        val payload = BpAnchorPayload.buildPayment(event, "V2", "SN1")
        val id = queue.enqueue(event.transactionId, "sunmi_payment", payload)
        repeat(AnchorProcessor.MAX_RETRIES - 1) {
            queue.incrementRetry(id, "fail")
        }
        val processor = AnchorProcessor(
            queue = queue,
            apiClientFactory = {
                object : BpApiClient("test-key") {
                    override fun postAnchor(payload: Map<String, Any>): BpAnchorResult =
                        BpAnchorResult(false, 500, null, null, null, null, "server error", "{}")
                }
            },
            isOnline = { true },
        )
        processor.flushQueue()
        assertEquals(1, queue.recent(5).count { it.status == QueueStatus.FAILED })
    }
}

class InMemoryQueueTest {
    @Test
    fun enqueueAndMarkAnchored() {
        val queue = InMemoryAnchorQueue()
        val id = queue.enqueue("PAY-1", "sunmi_payment", mapOf("reference_id" to "PAY-1"))
        assertEquals(1, queue.countPending())
        queue.markAnchored(id, "anchor-x", "https://branchlesspay.com/verify/anchor-x")
        assertEquals(0, queue.countPending())
        assertEquals(1, queue.countAnchored())
    }

    @Test
    fun recordAnchored_insertsHistoryRow() {
        val queue = InMemoryAnchorQueue()
        queue.recordAnchored(
            referenceId = "PAY-2",
            eventType = "sunmi_payment",
            payload = mapOf("reference_id" to "PAY-2", "amount" to 100.0, "currency" to "IDR"),
            anchorId = "id-2",
            verifyUrl = "https://branchlesspay.com/verify/id-2",
        )
        assertEquals(1, queue.countAnchored())
        assertEquals("PAY-2", queue.getById(1)?.referenceId)
    }
}

class PaymentEventTest {
    @Test
    fun simulated_hasPayPrefix() {
        val event = PaymentEvent.simulated()
        assertTrue(event.transactionId.startsWith("PAY-"))
    }
}

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

    @Test
    fun buildPayment_hasSunmiPaymentEventType() {
        val event = PaymentEvent.simulated(amountCents = 50000, currency = "IDR")
        val payload = BpAnchorPayload.buildPayment(event, "T2mini", "SN999")
        assertEquals("sunmi_payment", payload["event_type"])
        assertEquals(event.transactionId, payload["reference_id"])
        assertEquals(50000.0, payload["amount"])
        @Suppress("UNCHECKED_CAST")
        val metadata = payload["metadata"] as Map<String, Any>
        assertEquals("card", metadata["payment_method"])
        assertEquals("T2mini", metadata["device_model"])
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
