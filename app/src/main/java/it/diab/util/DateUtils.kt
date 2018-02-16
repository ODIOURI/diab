package it.diab.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private var SECOND: Long = 1000 // Millisecond * 1000
    private var MINUTE = 60 * SECOND
    private var HOUR = 60 * MINUTE
    private var DAY = 24 * HOUR
    var WEEK = 7 * DAY

    fun dateToString(date: Date): String {
        val str = SimpleDateFormat("EEEE dd-MM\nHH:mm", Locale.getDefault()).format(date)
        // Uppercase first char
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length)
    }
}
