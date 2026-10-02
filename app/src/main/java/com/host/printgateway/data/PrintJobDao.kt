package com.host.printgateway.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PrintJobDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(jobs: List<PrintJobEntity>)

    @Query("SELECT * FROM print_jobs WHERE status = 'PENDING' ORDER BY createdAt ASC")
    suspend fun getPending(): List<PrintJobEntity>

    @Query("SELECT * FROM print_jobs WHERE status = 'PRINTED' ORDER BY createdAt ASC")
    suspend fun getPrintedAwaitingAck(): List<PrintJobEntity>

    @Query("UPDATE print_jobs SET status = 'PRINTED' WHERE id = :id")
    suspend fun markPrinted(id: String)

    @Query("DELETE FROM print_jobs WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM print_jobs")
    suspend fun count(): Int
}