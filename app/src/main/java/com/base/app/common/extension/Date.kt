package com.base.app.common.extension

import android.annotation.SuppressLint
import com.base.app.common.EMPTY_STRING
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun Date.timeAgo(): String {
    val SECOND_MILLIS: Int = 1000
    val MINUTE_MILLIS: Int = 60 * SECOND_MILLIS
    val HOUR_MILLIS: Int = 60 * MINUTE_MILLIS
    val DAY_MILLIS: Int = 24 * HOUR_MILLIS

    var time = time
    if (time < 1000000000000L) {
        time *= 1000
    }
    val now = System.currentTimeMillis()
    if (time > now || time <= 0) {
        return ""
    }
    val diff = now - time
    return if (diff < MINUTE_MILLIS) {
        "Vừa xong"
    } else if (diff < 2 * MINUTE_MILLIS) {
        "1 phút trước"
    } else if (diff < 50 * MINUTE_MILLIS) {
        (diff / MINUTE_MILLIS).toString() + " phút trước"
    } else if (diff < 90 * MINUTE_MILLIS) {
        "1 giờ trước"
    } else if (diff < 24 * HOUR_MILLIS) {
        (diff / HOUR_MILLIS).toString() + " giờ trước"
    } else if (diff < 48 * HOUR_MILLIS) {
        "Hôm qua"
    } else {
        (diff / DAY_MILLIS).toString() + " ngày trước"
    }
}

fun String.formatDate3(): String? {
    @SuppressLint("SimpleDateFormat") val formatterInput =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    formatterInput.timeZone = TimeZone.getTimeZone("UTC")
    var date: Date? = null
    try {
        date = formatterInput.parse(toString())
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat("HH:mm dd/MM/yyyy")
    return if (date == null) {
        toString()
    } else formatter.format(date)
}

fun String.formatDate4(): String? {
    @SuppressLint("SimpleDateFormat") val formatterInput =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    formatterInput.timeZone = TimeZone.getTimeZone("UTC")
    var date: Date? = null
    try {
        date = formatterInput.parse(toString())
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat("dd-MM-yyyy")
    return if (date == null) {
        toString()
    } else {
        var list = formatter.format(date).split("-").toList()
        "Ngày ${list[0]} tháng ${list[1]} năm ${list[2]} "
    }
}

fun String.formatDate2(): String? {
    @SuppressLint("SimpleDateFormat") val formatterInput =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    formatterInput.timeZone = TimeZone.getTimeZone("UTC")
    var date: Date? = null
    try {
        date = formatterInput.parse(toString())
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat("HH:mm dd-MM-yyyy")
    return if (date == null) {
        toString()
    } else formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun convertToTimeAgo(time: String?, newFormat: String?): String {
    if (time != null) {
        val formatterInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        formatterInput.timeZone = TimeZone.getTimeZone("UTC")
        var date: Date? = null
        try {
            date = formatterInput.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val formatter =
            SimpleDateFormat(newFormat)
        formatter.timeZone = TimeZone.getDefault()
        return if (date == null) {
            time
        } else {
            val sdf = SimpleDateFormat(newFormat)
            try {
                val d = sdf.parse(sdf.format(date))
                return d.timeAgo()
            } catch (ex: ParseException) {

            }
            return EMPTY_STRING
        }
    } else {
        return EMPTY_STRING
    }
}

fun String.formatDate5(): String? {
    @SuppressLint("SimpleDateFormat") val formatterInput =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    formatterInput.timeZone = TimeZone.getTimeZone("UTC")
    var date: Date? = null
    try {
        date = formatterInput.parse(toString())
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat("HH:mm:ss - dd/MM/yyyy")
    return if (date == null) {
        toString()
    } else formatter.format(date)
}

fun String.convertDate3(): String? {
    val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val outputFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    val date: Date = inputFormat.parse(this)
    return outputFormat.format(date)
}

fun String.convertDate5(): String? {
    val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
    val date: Date = inputFormat.parse(this)
    return outputFormat.format(date)
}

fun convertTime(time: String?, currentFormat: String?, newFormat: String?): String? {
    if (time != null) {
        @SuppressLint("SimpleDateFormat") val formatterInput =
            SimpleDateFormat(currentFormat)
        var date: Date? = null
        try {
            date = formatterInput.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        @SuppressLint("SimpleDateFormat") val formatter =
            SimpleDateFormat(newFormat)
        return if (date == null) {
            time
        } else formatter.format(date)
    }
    return ""

}
