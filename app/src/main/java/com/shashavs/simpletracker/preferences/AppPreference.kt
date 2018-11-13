package com.shashavs.simpletracker.preferences

import android.content.SharedPreferences
import java.util.*
import javax.inject.Inject

class AppPreference @Inject constructor(val preference: SharedPreferences) {

    fun setPreference(key: String, value: Boolean) {
        preference.edit()
                .putBoolean(key, value)
                .apply()
    }

    fun setPreference(key: String, value: Int) {
        preference.edit()
                .putInt(key, value)
                .apply()
    }

    fun setPreference(key: String, value: String) {
        preference.edit()
            .putString(key, value)
            .apply()
    }

    fun getBoleanPreference(key: String) = preference.getBoolean(key, true)

    fun getIntPreference(key: String) = preference.getInt(key, 0)

    fun getStringPreference(key: String) = preference.getString(key, null)

    fun getId(): String {
        var id = getStringPreference("id")
        if(id == null) {
            id = UUID.randomUUID().toString()
            setPreference("id", id)
        }
        return id
    }
}