package com.ggaebiz.ggaebiz.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerService : Service() {

    private lateinit var player: ExoPlayer
    private var audioResPath: String = ""
    private var timerJob: Job? = null

    private val binder = TimerBinder()
    private val _overSeconds = MutableStateFlow(0)
    val overSeconds: StateFlow<Int> get() = _overSeconds
    private var overSecondsJob: Job? = null

    companion object {
        const val ACTION_START = "START_TIMER"
        const val ACTION_STOP = "STOP_TIMER"
        const val INTENT_KET_TIMER_SECONDS = "TIMER_SECONDS"
        const val INTENT_KET_TIMER_AUDIO = "TIMER_AUDIO"
        const val REQUEST_CODE = 1004
        const val NOTIFICATION_CHANNEL_ID = "timer_channel"
    }

    inner class TimerBinder : Binder(){
        fun getService() : TimerService = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }


    override fun onCreate() {
        super.onCreate()
        Log.d("TimerService","onCreate()")
        player = ExoPlayer.Builder(this).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        Log.d("TimerService","onStartCommand() :: action :: ${action}")

        if (action == ACTION_STOP) {
            stopSelf()
        } else if (action == ACTION_START) {
            val seconds = intent.getIntExtra(INTENT_KET_TIMER_SECONDS, 0)
            audioResPath = intent.getStringExtra(INTENT_KET_TIMER_AUDIO) ?: ""
            startTimer(seconds)
        }

        return START_STICKY
    }

    private fun startTimer(times: Int) {
        var remainingTime = times
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                if (remainingTime > 0) {
                    Log.d("TimerService","startTimer() :: 현재 숫자  :: ${remainingTime}")
                    remainingTime--
                } else {
                    Log.d("TimerService","startTimer() :: 타이머 종료")
                    timerJob?.cancel()
                    onTimerFinished()
                }
                delay(1000L) // 1초 대기
            }
        }
    }

    private fun onTimerFinished() {
        Log.d("TimerService","onTimerFinished()")
        startOverSecondsJob()
        navigateToTargetScreen()
        startForegroundService()
        playAudio() // 음원 재생
    }

    private fun startOverSecondsJob() {
        overSecondsJob?.cancel()
        overSecondsJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(1000L) // 1초 대기
                _overSeconds.value +=1
                Log.d("TimerService","_overSeconds :: ${overSeconds.value}")
            }
        }
    }

    private fun startForegroundService() {
        Log.d("TimerService","startForegroundService()")

        val notification = createNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 이상: 추가적인 Foreground Service 유형 필요
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(1, notification)
        }
    }

    private fun playAudio() {
        val mediaItem =
            MediaItem.fromUri(Uri.parse("android.resource://$packageName/${audioResPath}"))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.play()
    }

    private fun navigateToTargetScreen() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("EXTRA_LAUNCH_HOME", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun createNotification(): Notification {
        val channelId = NOTIFICATION_CHANNEL_ID
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Timer Service", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, REQUEST_CODE, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val finishNotification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_content))
            .setSmallIcon(R.drawable.ic_noti)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // 클릭 시 알림을 자동으로 제거
            .build()

        return finishNotification
    }

    override fun onDestroy() {
        Log.d("TimerService","onDestroy()")
        timerJob?.cancel()
        overSecondsJob?.cancel()
        player.release()
        super.onDestroy()
    }
}