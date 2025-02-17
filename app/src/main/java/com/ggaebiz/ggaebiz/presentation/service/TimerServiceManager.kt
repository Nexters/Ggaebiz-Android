package com.ggaebiz.ggaebiz.presentation.service

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import kotlinx.coroutines.flow.StateFlow

class TimerServiceManager(private val context: Context) {
    private var service: TimerService? = null
    private var serviceConnection: ServiceConnection? = null

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
        unbindService()
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

    fun bindService(onServiceConnected: (StateFlow<Int>) -> Unit) {
        if (serviceConnection == null) {
            serviceConnection = createServiceConnection(onServiceConnected)
        }

        val intent = Intent(context, TimerService::class.java)
        context.bindService(intent, serviceConnection!!, Context.BIND_AUTO_CREATE)
    }

    fun unbindService() {
        serviceConnection?.let {
            context.unbindService(it)
            serviceConnection = null
        }
        service = null
    }

    private fun createServiceConnection(onServiceConnected: (StateFlow<Int>) -> Unit): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                val timerBinder = binder as? TimerService.TimerBinder
                service = timerBinder?.getService()

                // TimerService의 StateFlow를 TimerManager의 StateFlow로 전달
                service?.let {
                    onServiceConnected(it.overSeconds)
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                service = null
            }
        }
    }

}
