package com.host.printgateway.network

object BackendAuth {

    const val DEFAULT_TOKEN = ""

    const val AUTHORIZATION_HEADER = "Authorization"

    fun authorizationValue(token: String): String {
        val cleanToken = token
            .trim()
            .removePrefix("Bearer ")
            .trim()

        return "Bearer $cleanToken"
    }
}