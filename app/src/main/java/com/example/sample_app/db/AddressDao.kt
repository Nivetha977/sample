package com.example.sample_app.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 *Created by Nivetha S on 24-11-2021.
 */
@Dao
interface AddressDao {
    @Insert
    fun insert(address: Address)

    @Query("SELECT * FROM address")
    fun getAddress(): List<Address>

    @Query("DELETE FROM address")
    fun deleteAll()
}