package com.dev.amintopup.fragments.notifications.model

data class NotificationModel(
    val data: List<Data>,
    val message: String,
    val success: Boolean
)