package com.ssti.dharmendrapractical.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssti.dharmendrapractical.data.model.CartsResponse

class Converters {

    private val gson = Gson()

    // ---- Cart List ----
    @TypeConverter
    fun fromCartList(value: List<CartsResponse.Cart>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCartList(value: String): List<CartsResponse.Cart> {
        val type = object : TypeToken<List<CartsResponse.Cart>>() {}.type
        return gson.fromJson(value, type)
    }
}
