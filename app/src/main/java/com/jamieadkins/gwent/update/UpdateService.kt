package com.jamieadkins.gwent.update

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.GwentApplication.Companion.coreComponent
import javax.inject.Inject

class UpdateService : Service(), UpdateContract.View {

    @Inject
    lateinit var presenter: UpdateContract.Presenter

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onCreate() {
        DaggerUpdateComponent.builder()
            .core(coreComponent)
            .service(this)
            .build()
            .inject(this)
        super.onCreate()

        setupNotificationChannel()
        NotificationManagerCompat.from(this).cancel(COMPLETE_NOTIFICATION_ID)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(this, R.color.gwentGreen))
            .setContentTitle(getString(R.string.update_notification_progress_title))
            .setProgress(0, 0, true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(IN_PROGRESS_NOTIFICATION_ID, notification)
        presenter.onAttach()
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    override fun finish() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(this, R.color.gwentGreen))
            .setContentTitle(getString(R.string.update_notification_complete_title))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        NotificationManagerCompat.from(this).notify(COMPLETE_NOTIFICATION_ID, notification)

        stopSelf()
    }

    override fun showError() {
        val retryIntent = Intent(this, UpdateService::class.java)
        val retryPendingIntent: PendingIntent =
            PendingIntent.getService(this, 0, retryIntent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(this, R.color.gwentGreen))
            .setContentTitle(getString(R.string.update_notification_error_title))
            .addAction(R.drawable.ic_notification, getString(R.string.retry), retryPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        NotificationManagerCompat.from(this).notify(COMPLETE_NOTIFICATION_ID, notification)

        stopSelf()
    }

    private fun setupNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.update_notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "com.jamieadkins.gwent.update.notifications"
        private const val IN_PROGRESS_NOTIFICATION_ID = 31413
        private const val COMPLETE_NOTIFICATION_ID = 31414
    }
}
