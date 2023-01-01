package com.dev.amintopup.fragments.auth.VerifyPinModel

data class VerifyPinModel(
    val access_token: String,
    val message: String,
    val status: Int,
    val user: User
)