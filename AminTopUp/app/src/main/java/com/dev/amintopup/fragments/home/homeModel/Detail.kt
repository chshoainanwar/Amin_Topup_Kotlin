package com.dev.amintopup.fragments.home.homeModel

import java.io.Serializable

data class Detail(
    val created_at: String? = null,
    val denomination: Double? = null,
    val exchange_rate: Double? = null,
    val fee_percentage: Double? = null,
    val id: Int? = null,
    val operator_id: Int? = null,
    val product_code_stripe: String? = null,
    val product_code_topup: String? = null,
    val stripe_fee: Double? = null,
    val topup_usd: Double? = null,
    val totalAmount: Double? = null,
    val updated_at: String? = null,
    val receiver_get_AFN: Double? = null,
    val processing_fee: Double? = null,
) : Serializable