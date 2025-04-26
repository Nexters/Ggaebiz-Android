package com.ggaebiz.ggaebiz.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.VibrationEffect
import android.os.Vibrator
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
    private var vibrator: Vibrator? = null
    private var audioResPath: String = ""
    private var timerJob: Job? = null

    private val binder = TimerBinder()
    private val _overSeconds = MutableStateFlow(0)
    val overSeconds: StateFlow<Int> get() = _overSeconds
    private var overSecondsJob: Job? = null

    private lateinit var _notificationManager : NotificationManager
    val notificationManager: NotificationManager get() = _notificationManager
    private lateinit var wakeLock: PowerManager.WakeLock

    companion object {
        const val ACTION_START = "START_TIMER"
        const val ACTION_STOP = "STOP_TIMER"
        const val INTENT_KEY_TIMER_SECONDS = "TIMER_SECONDS"
        const val INTENT_KEY_TIMER_AUDIO = "TIMER_AUDIO"
        const val INTENT_KEY_VIBRATION = "TIMER_VIBRATION"
        const val INTENT_KEY_VOLUME = "TIMER_VOLUME"
        const val REQUEST_CODE = 1004
        const val NOTIFICATION_CHANNEL_ID = "timer_channel"
        const val NOTIFICATION_ID = 1

        const val EXTRA_LAUNCH_HOME_ID = "EXTRA_LAUNCH_HOME"
    }

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("TimerService", "onCreate()")

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TimerService::WakelockTag")
        wakeLock.acquire()


        // Notification 채널 생성
        _notificationManager = getSystemService(NotificationManager::class.java)
        createNotificationChannel()

        // player 초기화
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        player = ExoPlayer.Builder(this).build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        Log.d("TimerService", "onStartCommand() :: action :: ${action}")

        if (action == ACTION_STOP) {
            stopSelf()
        } else if (action == ACTION_START) {
            val seconds = intent.getIntExtra(INTENT_KEY_TIMER_SECONDS, 0)
            val vibration = intent.getIntExtra(INTENT_KEY_VIBRATION,3)
            val volume = intent.getIntExtra(INTENT_KEY_VOLUME, 3)
            audioResPath = intent.getStringExtra(INTENT_KEY_TIMER_AUDIO) ?: ""
            startTimer(seconds, vibration, volume)
            // 서비스 실행
            startForegroundService()
        }

        return START_STICKY
    }

    private fun startTimer(times: Int, vibration: Int, volume: Int) {
        var remainingTime = times
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                remainingTime--
                delay(1000L) // 1초 대기
                if (remainingTime > 0) {
                    Log.d("TimerService", "startTimer() :: 현재 숫자  :: ${remainingTime}")
                    updateTimerNotification(remainingTime)
                } else {
                    Log.d("TimerService", "startTimer() :: 타이머 종료")
                    timerJob?.cancel()
                    onTimerFinished(vibration,volume)
                }
            }
        }
    }

    private fun onTimerFinished(vibration: Int, volume: Int) {
        Log.d("TimerService", "onTimerFinished()")
        updateNotification()
        startOverSecondsJob()
        navigateToTargetScreen()
        playAudio(vibration,volume) // 음원 재생
    }

    private fun startOverSecondsJob() {
        overSecondsJob?.cancel()
        overSecondsJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(1000L) // 1초 대기
                _overSeconds.value += 1
                Log.d("TimerService", "_overSeconds :: ${overSeconds.value}")
            }
        }
    }

    private fun startForegroundService() {
        Log.d("TimerService", "startForegroundService()")
        val notification = createSilentNotification()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun playAudio(vibration: Int, volume: Int) {
        val mediaItem =
            MediaItem.fromUri(Uri.parse("android.resource://$packageName/${audioResPath}"))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.repeatMode = Player.REPEAT_MODE_ONE
        player.volume = volume.coerceIn(1, 10) / 10f
        vibrator?.vibratePattern(vibration)
        player.play()
    }

    private fun navigateToTargetScreen() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_LAUNCH_HOME_ID, true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun updateNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_LAUNCH_HOME_ID, true)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, REQUEST_CODE, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val finishNotification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_content))
            .setSmallIcon(R.mipmap.ic_app_icon_round)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, finishNotification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Timer Service",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createSilentNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_default_content))
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 우선순위 최소
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // 잠금 화면에서도 숨김
            .setSilent(true) // 알림 사운드 없음
            .setSmallIcon(R.mipmap.ic_app_icon_round)
            .setOngoing(true)
            .build()
    }

    private fun updateTimerNotification(remainingSeconds: Int) {
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(formattedRemainingTime(remainingSeconds))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSilent(true)
            .setSmallIcon(R.mipmap.ic_app_icon_round)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun formattedRemainingTime(remainingSeconds: Int): String {
        val hours = remainingSeconds / 3600
        val minutes = (remainingSeconds % 3600) / 60
        val seconds = remainingSeconds % 60

        return buildString {
            append("남은 시간 ")
            if (hours > 0) append("${hours.format2Digits()}시간 ")
            if (hours > 0 || minutes > 0) append("${minutes.format2Digits()}분 ")
            append("${seconds.format2Digits()}초")
        }.trim()
    }

    private fun Int.format2Digits() = String.format("%02d", this)

    override fun onDestroy() {
        Log.d("TimerService", "onDestroy()")
        timerJob?.cancel()
        overSecondsJob?.cancel()
        player.release()
        vibrator?.cancel()
        wakeLock.release()
        super.onDestroy()
    }

    private fun Vibrator.vibratePattern(strength: Int) {
        if (strength == 0) {
            this.cancel()
            return
        }
        val clampedStrength = strength.coerceIn(1, 10) * 25
        val pattern = longArrayOf(0, 300, 500)
        val amplitudes = intArrayOf(0, clampedStrength, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, 0)) // 무한 반복
        } else {
            this.vibrate(pattern, 0) // 예전 방식 (강도 조절 불가)
        }
    }
}
