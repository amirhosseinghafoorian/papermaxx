package com.a.papermaxx.general

import android.content.Context
import android.content.SharedPreferences
import com.a.papermaxx.app.MyApp

class setting {
    private val prefKey = "locals"
    val app = MyApp.publicApp
    private val preferences: SharedPreferences? =
        app.getSharedPreferences(prefKey, Context.MODE_PRIVATE)

    fun getString(key: String): String {
        return preferences?.getString(key, "***").toString()
    }

    fun getBool(key: String): String {
        if (preferences != null) {
            return if (preferences.contains(key)) {
                preferences.getBoolean(key, false).toString()
            } else "***"
        }
        return ""
    }

    fun getInt(key: String): String {
        if (preferences != null) {
            return if (preferences.contains(key)) {
                preferences.getInt(key, -1).toString()
            } else "***"
        }
        return ""
    }

    fun remove(key: String): String {
        if (preferences != null) {
            return if (preferences.contains(key)) {
                preferences.edit().remove(key).apply()
                "Success"
            } else "***"
        }
        return ""
    }

    fun putString(key: String, value: String): String {
        if (preferences != null) {
            return if (!preferences.contains(key)) {
                preferences.edit()?.putString(key, value)?.apply()
                "Success"
            } else "***"
        }
        return ""
    }

    fun putBool(key: String, value: Boolean): String {
        if (preferences != null) {
            return if (!preferences.contains(key)) {
                preferences.edit()?.putBoolean(key, value)?.apply()
                "Success"
            } else "***"
        }
        return ""
    }

    fun putInt(key: String, value: Int): String {
        if (preferences != null) {
            return if (!preferences.contains(key)) {
                preferences.edit()?.putInt(key, value)?.apply()
                "Success"
            } else "***"
        }
        return ""
    }
}
