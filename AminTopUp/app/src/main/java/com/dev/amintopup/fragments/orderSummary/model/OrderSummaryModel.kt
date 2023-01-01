package com.dev.amintopup.fragments.orderSummary.model

data class OrderSummaryModel(
    val currency: String,
    val message: String,
    val payment_id: String,
    val payment_url: String,
    val result: String
)