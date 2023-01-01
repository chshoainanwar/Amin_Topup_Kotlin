package com.dev.amintopup.fragments.notifications.model

data class Data(
    val contact_id: Int,
    val created_at: String,
    val id: Int,
    val message: String,
    val notification_status: Int,
    val notification_type: String,
    val transaction_id: Any,
    val updated_at: String,
    val user_id: Int
)