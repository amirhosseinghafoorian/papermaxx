package com.a.papermaxx.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    companion object {
        lateinit var publicApp: Application
    }

    override fun onCreate() {
        super.onCreate()
        publicApp = this
    }
}