package com.example.wsspostboard.posts.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun convertTimestampToFormattedDate(timestamp: Long): String {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
        val formattedDate = sdf.format(date)

        val day = formattedDate.split(" ")[0].toInt()
        val dayWithSuffix = addDaySuffix(day)

        return formattedDate.replaceFirst(day.toString(), dayWithSuffix)
    }
}

fun addDaySuffix(day: Int): String {
    return when (day) {
        1, 21, 31 -> "$day" + "st"
        2, 22 -> "$day" + "nd"
        3, 23 -> "$day" + "rd"
        else -> "$day" + "th"
    }
}