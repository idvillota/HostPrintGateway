package com.host.printgateway.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PrintJobEntity::class], version = 1, exportSchema = false)
abstract class PrintGatewayDatabase : RoomDatabase() {

    abstract fun printJobDao(): PrintJobDao

    companion object {
        @Volatile
        private var instance: PrintGatewayDatabase? = null

        fun get(context: Context): PrintGatewayDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    PrintGatewayDatabase::class.java,
                    "print_gateway.db",
                ).build().also { instance = it }
            }
    }
}