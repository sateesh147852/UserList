package com.users.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log

class TimerService : Service() {

    private var timerBinder: TimerBinder = TimerBinder()
    private var maxValue = 5
    private lateinit var handler: Handler

    override fun onBind(intent: Intent?): IBinder {
        return timerBinder
    }

    override fun onCreate() {
        super.onCreate()
        handler = Handler(Looper.myLooper()!!)
    }

    fun startTask() {
        val runnable: Runnable = object : Runnable {

            override fun run() {
                maxValue -= 1
                if (maxValue == 0){
                    handler.removeCallbacks(this)
                    stopSelf()
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable, 1000)
    }

    fun getCurrentValue(): Int = maxValue


    inner class TimerBinder() : Binder() {

        fun getService(): TimerService {
            return this@TimerService
        }
    }


}