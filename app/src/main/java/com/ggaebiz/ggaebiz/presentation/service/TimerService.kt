package com.ggaebiz.ggaebiz.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimerService : Service() {

    private lateinit var player: ExoPlayer
    private var audioResPath: String = ""
    private var timerJob: Job? = null

    companion object {
        const val ACTION_START = "START_TIMER"
        const val ACTION_STOP = "STOP_TIMER"
        const val INTENT_KET_TIMER_SECONDS = "TIMER_SECONDS"
        const val INTENT_KET_TIMER_AUDIO = "TIMER_AUDIO"
        const val REQUEST_CODE = 1004
        const val NOTIFICATION_CHANNEL_ID = "timer_channel"
    }

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action

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
                    remainingTime--
                } else {
                    timerJob?.cancel()
                    onTimerFinished()
                }
                delay(1000L) // 1초 대기
            }
        }
    }

    private fun onTimerFinished() {
        navigateToTargetScreen()
        startForegroundService()
        playAudio() // 음원 재생
    }

    private fun startForegroundService() {
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
        timerJob?.cancel()
        player.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
