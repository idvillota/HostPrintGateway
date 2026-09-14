package com.host.printgateway.network

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * Thin HTTP client for the Host print-client job queue.
 */
class PrintJobApi(
    private val baseUrl: String,
    private val deviceToken: String,
) {

    fun fetchPendingJobs(): Result<List<PrintJobDto>> = runCatching {
        val conn = open("GET", "/api/print-client/jobs/pending")
        try {
            val code = conn.responseCode
            val body = readBody(conn)
            if (code !in 200..299) {
                error("Pending jobs HTTP $code: ${body.take(200)}")
            }
            parseJobs(body)
        } finally {
            conn.disconnect()
        }
    }

    fun acknowledge(jobId: String, ack: PrintJobAckRequest): Result<Unit> = runCatching {
        val conn = open("POST", "/api/print-client/jobs/$jobId/ack")
        try {
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            val json = JSONObject()
                .put("success", ack.success)
                .put("errorMessage", ack.errorMessage ?: JSONObject.NULL)
            OutputStreamWriter(conn.outputStream, StandardCharsets.UTF_8).use { it.write(json.toString()) }
            val code = conn.responseCode
            val body = readBody(conn)
            if (code !in 200..299) {
                error("Ack HTTP $code: ${body.take(200)}")
            }
        } finally {
            conn.disconnect()
        }
    }

    private fun open(method: String, path: String): HttpURLConnection {
        val url = URL(baseUrl.trimEnd('/') + path)
        return (url.openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 15_000
            readTimeout = 30_000
            setRequestProperty("Accept", "application/json")
            setRequestProperty("X-Print-Client-Token", deviceToken)
        }
    }

    private fun readBody(conn: HttpURLConnection): String {
        val stream = if (conn.responseCode in 200..299) conn.inputStream else conn.errorStream
            ?: return ""
        return BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8)).use { it.readText() }
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
