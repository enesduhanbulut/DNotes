package com.duhapp.dnotes.app

import android.app.Application
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        EmojiManager.install(GoogleEmojiProvider())
        Timber.plant(Timber.DebugTree())
    }
}