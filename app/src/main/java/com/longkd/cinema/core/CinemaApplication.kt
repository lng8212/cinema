package com.longkd.cinema.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CinemaApplication : Application() {
    companion object {
        lateinit var instance: CinemaApplication private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
