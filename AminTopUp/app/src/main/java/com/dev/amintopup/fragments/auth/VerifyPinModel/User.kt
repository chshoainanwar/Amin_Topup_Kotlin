package com.dev.amintopup.fragments.auth.VerifyPinModel

data class User(
    val country: String,
    val created_at: String,
    val date_of_birth: String,
    val email: String,
    val email_verified_at: Any,
    val id: Int,
    val name: String,
    val otp: Int,
    val phone_number: String,
    val profile: String,
    val type: String,
    val updated_at: String
)