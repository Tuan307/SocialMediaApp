package com.base.app.common

import java.util.concurrent.TimeUnit

/**
 * @author tuanpham
 * @since 9/28/2023
 */
object TimeAgo {
    private val dateTimes: List<Long> = listOf(
        TimeUnit.DAYS.toMillis(365),
        TimeUnit.DAYS.toMillis(30),
        TimeUnit.DAYS.toMillis(1),
        TimeUnit.HOURS.toMillis(1),
        TimeUnit.MINUTES.toMillis(1),
        TimeUnit.SECONDS.toMillis(1)
    )
    private val timesString: List<String> =
        listOf("year", "month", "day", "hour", "minute", "second")

    public fun toDuration(duration: Long): String {
        val res = StringBuffer()
        for (i in dateTimes.indices) {
            val current: Long = dateTimes[i]
            val temp = duration / current
            if (temp > 0) {
                res.append(temp).append(" ").append(timesString[i])
                    .append(if (temp != 1L) "s" else "").append(" ago")
                break
            }
        }
        return if ("" == res.toString()) "recently" else res.toString()
    }
}