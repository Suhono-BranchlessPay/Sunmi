package com.branchlesspay.auditshield

object SunmiPaymentParser {
    fun parseExtras(extras: Map<String, String?>): PaymentEvent? {
        val transactionId = firstNonBlank(
            extras["transactionId"],
            extras["transId"],
            extras["orderId"],
            extras["transaction_id"],
        ) ?: return null

        val amountRaw = firstNonBlank(
            extras["amount"],
            extras["payAmount"],
            extras["totalAmount"],
        )
        val amountCents = parseAmountCents(amountRaw) ?: return null
        val currency = firstNonBlank(extras["currency"], extras["currencyCode"]) ?: "IDR"
        val method = firstNonBlank(extras["payType"], extras["paymentMethod"]) ?: "card"

        return PaymentEvent(
            transactionId = transactionId,
            amountCents = amountCents,
            currency = currency.uppercase(),
            paymentMethod = method,
            merchantId = firstNonBlank(extras["merchantId"]) ?: "",
        )
    }

    private fun firstNonBlank(vararg values: String?): String? =
        values.firstOrNull { !it.isNullOrBlank() }

    private fun parseAmountCents(raw: String?): Long? {
        if (raw.isNullOrBlank()) return null
        val cleaned = raw.replace(",", "").trim()
        return cleaned.toDoubleOrNull()?.let { (it * 100).toLong() }
            ?: cleaned.toLongOrNull()
    }
}
