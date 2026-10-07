package com.host.printgateway.network

import org.json.JSONArray
import org.json.JSONObject

/**
 * Thin HTTP client for the Host print-client job queue.
 */
class PrintJobApi(
    private val baseUrl: String,
    private val deviceToken: String = BackendAuth.DEFAULT_TOKEN,
) {
    private val client = ApiClient(baseUrl = baseUrl, token = deviceToken)

    fun fetchPendingJobs(): Result<List<PrintJobDto>> = runCatching {
        val response = client.call("GET", "/api/print-client/jobs/pending")
        if (response.code !in 200..299) {
            error("Pending jobs HTTP ${response.code}: ${response.body.take(200)}")
        }
        parseJobs(response.body)
    }

    fun acknowledge(jobId: String, ack: PrintJobAckRequest): Result<Unit> = runCatching {
        val json = JSONObject()
            .put("success", ack.success)
            .put("errorMessage", ack.errorMessage ?: JSONObject.NULL)
            .toString()
        val response = client.call("POST", "/api/print-client/jobs/$jobId/ack", jsonBody = json)
        if (response.code !in 200..299) {
            error("Ack HTTP ${response.code}: ${response.body.take(200)}")
        }
    }

    private fun parseJobs(body: String): List<PrintJobDto> {
        if (body.isBlank()) return emptyList()
        val trimmed = body.trim()
        val array: JSONArray = when {
            trimmed.startsWith("[") -> JSONArray(trimmed)
            trimmed.startsWith("{") -> {
                val obj = JSONObject(trimmed)
                when {
                    obj.has("jobs") -> obj.getJSONArray("jobs")
                    obj.has("items") -> obj.getJSONArray("items")
                    else -> JSONArray().put(obj)
                }
            }
            else -> error("Unexpected pending-jobs body")
        }
        val jobs = mutableListOf<PrintJobDto>()
        for (i in 0 until array.length()) {
            val o = array.getJSONObject(i)
            jobs += PrintJobDto(
                id = o.getString("id"),
                kind = o.optString("kind", "receipt"),
                payloadFormat = o.getString("payloadFormat"),
                payload = o.getString("payload"),
            )
        }
        return jobs
    }
}
