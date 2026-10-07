package com.host.printgateway.network

/**
 * Contracts aligned with Host docs: GET pending + POST ack (X-Api-Key).
 * API may not be deployed yet — client fails with a clear message until it is.
 */
data class PrintJobDto(
    val id: String,
    val kind: String = "receipt",
    val payloadFormat: String,
    val payload: String,
)

data class PrintJobAckRequest(
    val success: Boolean,
    val errorMessage: String? = null,
)
