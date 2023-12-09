package edu.northeastern.jetpackcomposev1

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class MyNotificationApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // first channel for job recommendation
        val channelRecommendation = NotificationChannel(
            "channel_recommendation",
            "Recommendation",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channelRecommendation.description = "Used for the job recommendation"

        // second channel for post
        val channelPost = NotificationChannel(
            "channel_post",
            "Post",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channelPost.description = "Used for the post notification"

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channelRecommendation)
        notificationManager.createNotificationChannel(channelPost)
    }
}