package com.example.expandablenotificationapp

import android.annotation.SuppressLint
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.channels.Channel

class MainActivity : AppCompatActivity() {
    private val CHANNEL_ID = "IVA1"
    private val notificationId = 105
    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        val DefaultNotifBtn = findViewById<Button>(R.id.DefaultNotifBtn)
        val TapForMoreBtn = findViewById<Button>(R.id.TapNotifBtn)
        val SnoozeClickBtn = findViewById<Button>(R.id.SnoozeClickNotifBtn)
        val ProgressBarBtn = findViewById<Button>(R.id.ProgressBarNotifBtn)
        val LargeImageNotifBtn = findViewById<Button>(R.id.LargeImageNotifBtn)
        val LargeTextNotifBtn = findViewById<Button>(R.id.LargeTextNotifBtn)

        DefaultNotifBtn.setOnClickListener{
            sendDefaultNotification()
        }
        TapForMoreBtn.setOnClickListener(){
            sendTapForMoreNotification()
        }
        SnoozeClickBtn.setOnClickListener{
            sendSnoozeClickNotification()
            //snooze button problems...!//
        }
        ProgressBarBtn.setOnClickListener{
            sendProgressBarNotification()
            //
            //
        }
        LargeImageNotifBtn.setOnClickListener{
            sendLargeImageNotification()
        }
        LargeTextNotifBtn.setOnClickListener{
            sendLargeTextNotification()
        }

    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification App"
            val descriptionText = "Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
    private fun sendDefaultNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(androidx.loader.R.drawable.notification_bg)
            .setContentTitle("Hello Dear")
            .setContentText("Its a default notification...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun sendTapForMoreNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(androidx.loader.R.drawable.notification_bg)
            .setContentTitle("Hello Dear")
            .setContentText("Its a Tap For More notification...Tap For More")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendSnoozeClickNotification(){
        val snoozeIntent = Intent(this, MainActivity::class.java).apply {
            val ACTION_SNOOZE = "SNOOZE123"
            action = ACTION_SNOOZE
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(androidx.coordinatorlayout.R.drawable.notification_bg)
            .setContentTitle("Hello Still Dear")
            .setContentText("Its a Snooze Click Notification.Click For More")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .addAction(
                androidx.vectordrawable.animated.R.drawable.notification_bg, "SNOOZE",
                snoozePendingIntent)
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }
    private fun sendProgressBarNotification(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle("File Download")
            setContentText("Download in progress")
            setSmallIcon(androidx.transition.R.drawable.notification_bg)
            setPriority(NotificationCompat.PRIORITY_LOW)
        }
        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 0
        NotificationManagerCompat.from(this).apply {
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
            notify(notificationId, builder.build())


            builder.setContentText("Download complete")
                .setProgress(0, 0, false)
            notify(notificationId, builder.build())
        }
    }
    private fun sendLargeImageNotification(){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val bitmapLargeIcon = BitmapFactory.decodeResource(applicationContext.resources,
            androidx.legacy.coreutils.R.drawable.notification_bg)
        val bitmap = BitmapFactory.decodeResource(applicationContext.resources,
            androidx.legacy.coreutils.R.drawable.notification_bg)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(androidx.legacy.coreutils.R.drawable.notification_bg)
            .setContentTitle("Large Image Notification")
            .setContentText("Photo Below")
            .setLargeIcon(bitmapLargeIcon)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notification.build())
        }
    }
    private fun sendLargeTextNotification(){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val bitmapLargeIcon = BitmapFactory.decodeResource(applicationContext.resources,
            androidx.legacy.coreutils.R.drawable.notification_bg)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(androidx.legacy.coreutils.R.drawable.notification_bg)
            .setContentTitle("Large Text Notification")
            .setContentText("Text Below")
            .setLargeIcon(bitmapLargeIcon)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("much longer text...we extended it to much longer...welcome))"))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notification.build())
        }
    }


}
