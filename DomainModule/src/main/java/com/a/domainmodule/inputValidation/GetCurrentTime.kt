package com.a.domainmodule.inputValidation

import java.text.SimpleDateFormat
import java.util.*

class GetCurrentTime {

    fun dateToString(date: Date?): String? {
        val d: Date = Calendar.getInstance().time
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ROOT)
        return if (date == null) {
            format.format(d)
        } else {
            format.format(date)
        }
    }

    fun customDateToString(tag: String, date: Date?): String? {
        val d = Calendar.getInstance().time
        val day = SimpleDateFormat("dd",Locale.ROOT)
        val month = SimpleDateFormat("MM",Locale.ROOT)
        val year = SimpleDateFormat("yyyy",Locale.ROOT)
        val hour = SimpleDateFormat("hh",Locale.ROOT)
        val minute = SimpleDateFormat("mm",Locale.ROOT)
        val second = SimpleDateFormat("ss",Locale.ROOT)
        val milli = SimpleDateFormat("SSS",Locale.ROOT)
        var s: String? = ""
        if (date == null) {
            when (tag) {
                "year" -> {
                    s = year.format(d)
                }
                "month" -> {
                    s = month.format(d)
                }
                "day" -> {
                    s = day.format(d)
                }
                "hour" -> {
                    s = hour.format(d)
                }
                "minute" -> {
                    s = minute.format(d)
                }
                "second" -> {
                    s = second.format(d)
                }
                "milli" -> {
                    s = milli.format(d)
                }
            }
        } else {
            when (tag) {
                "year" -> {
                    s = year.format(date)
                }
                "month" -> {
                    s = month.format(date)
                }
                "day" -> {
                    s = day.format(date)
                }
                "hour" -> {
                    s = hour.format(date)
                }
                "minute" -> {
                    s = minute.format(date)
                }
                "second" -> {
                    s = second.format(date)
                }
                "milli" -> {
                    s = milli.format(date)
                }
            }
        }
        return s
    }
}