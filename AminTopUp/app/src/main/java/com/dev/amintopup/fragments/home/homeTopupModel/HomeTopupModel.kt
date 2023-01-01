package com.dev.amintopup.fragments.home.homeTopupModel

data class HomeTopupModel(
    val WeeklyTopups: Double? = null,
    val graphData: Double? = null,
    val recentTopups: ArrayList<RecentTopup> ? = null
)