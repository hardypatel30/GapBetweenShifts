package com.hardy.practical.utils

import java.sql.Date
import java.text.SimpleDateFormat

object DateUtils {
    @JvmStatic
    fun toSimpleString(date: Date) : String {
        val format = SimpleDateFormat("dd/MM/yyy")
        return format.format(date)
    }
}