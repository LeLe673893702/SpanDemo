package com.example.spandemo

import android.app.Application
import android.content.Context
import android.util.Log

class MyApplication : Application() {
    override fun onCreate() {
        Log.d("application", "我正在启动中")

        super.onCreate()
        context = applicationContext
        Thread.sleep(3000)
    }
    companion object {
        private var context :Context? = null
        @JvmStatic
        fun getContext(): Context? = context
    }
}