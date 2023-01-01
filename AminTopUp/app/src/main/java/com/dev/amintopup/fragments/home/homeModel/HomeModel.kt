package com.dev.amintopup.fragments.home.homeModel

import java.io.Serializable

data class HomeModel(
    val created_at: String? = null,
    val details: ArrayList<Detail>? = null,
    val id: Int? = null,
    val operator_image: String? = null,
    val operator_name: String? = null,
    val updated_at: String? = null
) : Serializable