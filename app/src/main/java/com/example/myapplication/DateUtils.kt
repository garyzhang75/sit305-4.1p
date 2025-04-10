package com.example.myapplication

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
