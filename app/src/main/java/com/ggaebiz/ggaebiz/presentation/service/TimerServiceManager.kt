package com.ggaebiz.ggaebiz.presentation.service

import android.app.ActivityManager
import android.content.Context
import android.content.Intent

class TimerServiceManager(private val context: Context) {

    fun startTimerService(seconds: Int, audioResPath: String) {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_START
            putExtra(TimerService.INTENT_KET_TIMER_SECONDS, seconds)
            putExtra(TimerService.INTENT_KET_TIMER_AUDIO, audioResPath)
        }
        context.startService(intent)
    }

    fun stopTimerService() {
        val stopServiceIntent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_STOP
        }
        context.stopService(stopServiceIntent)
    }

    fun isTimerServiceRunning(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
        for (service in runningServices) {
            if (TimerService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

}
