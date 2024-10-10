package com.goglitter

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.goglitter.utils.Prefs
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Singleton

/**
@author-Padma A
date-29/5/2023
 **/
@Singleton
@HiltAndroidApp
class GlitterApp : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: GlitterApp? = null
        var prefs: Prefs? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

val prefs: Prefs by lazy {
    GlitterApp.prefs!!
}