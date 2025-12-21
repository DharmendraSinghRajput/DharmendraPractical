package com.ssti.dharmendrapractical.utils

import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

class PrefUtil @Inject constructor(private val sharedPreferences: SharedPreferences) {
    companion object {
        const val LOGIN_DATA = "login_data"
        const val USER_ID= "iUserId"
    }
    fun putString(key: String, value: String) = sharedPreferences.edit { putString(key, value) }

    fun putInt(key: String, value: Int) = sharedPreferences.edit { putInt(key, value) }

    fun getString(key: String) = sharedPreferences.getString(key, null)

    fun getInt(key: String) = sharedPreferences.getInt(key, -1)

    fun remove(key: String) = sharedPreferences.edit { remove(key) }

    fun removeAll() = sharedPreferences.edit { clear() }
}
