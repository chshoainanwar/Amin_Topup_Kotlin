package com.dev.amintopup.fragments.history.model

data class AllTopup(
    val country: String? = null,
    val created_at: String? = null,
    val id: Int? = null,
    val processing_fee: Double? = null,
    val receiver_email: String? = null,
    val receiver_image: Any? = null,
    val receiver_name: String? = null,
    val receiver_network: String? = null,
    val receiver_number: String? = null,
    val status: Int? = null,
    val topup_amount: Int? = null,
    val topup_amount_usd: Double? = null,
    val total_amount_usd: Double? = null,
    val updated_at: String? = null,
    val user_id: Int? = null
)