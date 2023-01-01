package com.dev.amintopup.network

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.dev.amintopup.base.BaseApplication
import com.dev.amintopup.models.UserItem
import com.google.gson.Gson


//val localUserApp: User?
//    get() {
//        return LocalPreference.shared.user
//    }
class LocalPreference(private val mContext: Context?) {
    fun removeAll() {
//        val array = LocalPreference.shared.listCategories
        editor?.apply {
            clear()
            apply()
        }
    }

    //
    var user: UserItem?
        get() {
            val stUser = preferences?.getString("UserObje", "") ?: ""
            if (stUser.isEmpty()) {
                return null
            }
            return Gson().fromJson(stUser, UserItem::class.java)
        }
        set(newValue) {
            val userString = Gson().toJson(newValue)
            editor?.apply {
                putString("UserObje", userString)
                apply()
            }
        }
    var token: String?
        get() = preferences?.getString("AuthToken", "") ?: ""
        set(token) {
            editor?.apply {
                putString("AuthToken", token)
                apply()
            }
        }

    private var preferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null


    companion object {
        val shared: LocalPreference
            get() {
                return LocalPreference(BaseApplication.instance)
            }
    }

    init {
        preferences = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            mContext?.getSharedPreferences(
                BaseApplication.instance?.packageName, Context.MODE_PRIVATE
            )
        } else {
            mContext?.getSharedPreferences(
                BaseApplication.instance?.packageName, Context.MODE_PRIVATE
            )
        }
        editor = preferences?.edit()
        editor?.apply()
    }

}

