package com.example.spandemo.handler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.spandemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HandlerActivity : AppCompatActivity() {
    val TAG = "HandlerActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handler)

        val handler = Handler(Looper.myLooper()!!)

        findViewById<ConstraintLayout>(R.id.handler_content).run {
            lifecycleScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    val syncMessage = Message.obtain(handler) {
                        sendSyncMessage()
                    }
                    syncMessage.isAsynchronous = false

                    val asyncMessage = Message.obtain(handler) {
                        sendAsyncMessage()
                    }
                    asyncMessage.isAsynchronous = true
                    requestLayout()
                    handler.sendMessage(syncMessage)

                    handler.sendMessage(asyncMessage)
                }
            }
        }

    }

    private fun sendSyncMessage() {
        Log.d(TAG, "sendSyncMessage")
    }

    private fun sendAsyncMessage() {
        Log.d(TAG, "sendAsyncMessage")
    }
}