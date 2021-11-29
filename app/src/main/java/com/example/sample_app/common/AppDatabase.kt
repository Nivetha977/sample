package com.example.sample_app.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sample_app.db.Address
import com.example.sample_app.db.AddressDao

/**
 *Created by Nivetha S on 24-11-2021.
 */
@Database(entities = [Address::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAddressDao(): AddressDao

}