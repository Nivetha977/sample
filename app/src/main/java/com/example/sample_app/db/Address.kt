package com.example.sample_app.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *Created by Nivetha S on 24-11-2021.
 */
@Entity(tableName = "address")
class Address {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var address: String = ""
    var lat: Double = 0.0
    var lng: Double = 0.0
}