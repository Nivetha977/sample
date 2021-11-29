package com.example.sample_app.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.sample_app.common.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import android.content.SharedPreferences


/**
 *Created by Nivetha S on 19-11-2021.
 */
@HiltAndroidApp
class App : Application() {

    companion object {
        var mAppDatabase: AppDatabase? = null
        lateinit var mInstance: App
        var isLogin = "isLogin"

    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        mAppDatabase = initializeRoomDb(this, "address")

    }


    fun initializeRoomDb(context: Context, dbName: String): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .allowMainThreadQueries()
            .build()
    }
}