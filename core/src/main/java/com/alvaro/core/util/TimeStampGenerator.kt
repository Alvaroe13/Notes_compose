package com.alvaro.core.util

import java.text.SimpleDateFormat
import java.util.*

class TimeStampGenerator {

    fun getDate(): String? {
        return try {
            val date = SimpleDateFormat("dd/MM/yy")
            val currentDate = date.format(Date())
            currentDate
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}