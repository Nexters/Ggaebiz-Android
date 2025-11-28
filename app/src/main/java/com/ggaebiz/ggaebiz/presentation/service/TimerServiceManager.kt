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
    private var timerConnection: ServiceConnection? = null
    private var overCountConnection: ServiceConnection? = null

    fun startTimerService(seconds: Int, audioResPath: String, vibration : Int, volume : Int, actionButtonVisible: Boolean) {
        val intent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_START
            putExtra(TimerService.INTENT_KEY_TIMER_SECONDS, seconds)
            putExtra(TimerService.INTENT_KEY_TIMER_AUDIO, audioResPath)
            putExtra(TimerService.INTENT_KEY_VIBRATION, vibration)
            putExtra(TimerService.INTENT_KEY_VOLUME, volume)
            putExtra(TimerService.INTENT_KEY_ACTION_BUTTON_VISIBLE, actionButtonVisible)
        }
        context.startService(intent)
    }

    fun pauseTimer() {
        val intent = Intent(context, TimerService::class.java)
            .setAction(TimerService.ACTION_PAUSE)
        context.startService(intent)
    }

    fun resumeTimer() {
        val intent = Intent(context, TimerService::class.java)
            .setAction(TimerService.ACTION_RESUME)
        context.startService(intent)
    }

    fun stopTimerService() {
        val stopServiceIntent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_STOP
        }
        unbindOverCountService()
        unbindTimerService()
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

    fun bindTimerService(onServiceConnected: (StateFlow<NotificationTimerState>) -> Unit) {
        if (timerConnection == null) {
            timerConnection = createTimerServiceConnection(onServiceConnected)
        }

        val intent = Intent(context, TimerService::class.java)
        context.bindService(intent, timerConnection!!, Context.BIND_AUTO_CREATE)
    }

    fun bindOverCountService(onServiceConnected: (StateFlow<Int>) -> Unit) {
        if (overCountConnection == null) {
            overCountConnection = createOverCountServiceConnection(onServiceConnected)
        }

        val intent = Intent(context, TimerService::class.java)
        context.bindService(intent, overCountConnection!!, Context.BIND_AUTO_CREATE)
    }

    private fun unbindTimerService() {
        timerConnection?.let {
            context.unbindService(it)
            timerConnection = null
        }
        service = null
    }

    fun unbindOverCountService() {
        overCountConnection?.let {
            context.unbindService(it)
            overCountConnection = null
        }
        overCountConnection = null
    }

    private fun createTimerServiceConnection(onServiceConnected: (StateFlow<NotificationTimerState>) -> Unit): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                val timerBinder = binder as? TimerService.TimerBinder
                service = timerBinder?.getService()

                service?.let {
                    onServiceConnected(it.notificationTimerState)
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                service = null
            }
        }
    }

    private fun createOverCountServiceConnection(onServiceConnected: (StateFlow<Int>) -> Unit): ServiceConnection {
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
