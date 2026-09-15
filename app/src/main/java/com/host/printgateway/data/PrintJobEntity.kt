package com.host.printgateway.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "print_jobs")
data class PrintJobEntity(
    @PrimaryKey val id: String,
    val kind: String,
    val payloadFormat: String,
    val payload: String,
    val status: String = STATUS_PENDING,
    val createdAt: Long = System.currentTimeMillis(),
) {
    companion object {
        const val STATUS_PENDING = "PENDING"
        const val STATUS_PRINTED = "PRINTED"
    }
}