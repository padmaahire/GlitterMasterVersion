package com.goglitter.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Prefs(context: Context) {
    companion object{
        private const val PREFS_FILENAME = "app_prefs"
        private const val KEY_MY_STRING = "my_string"
        private const val KEY_MY_BOOLEAN = "my_boolean"
        private const val KEY_MY_ARRAY = "string_array"
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var myString: String
        get() = sharedPrefs.getString(KEY_MY_STRING, "") ?: ""
        set(value) = sharedPrefs.edit { putString(KEY_MY_STRING, value) }

    var myBoolean: Boolean
        get() = sharedPrefs.getBoolean(KEY_MY_BOOLEAN, false)
        set(value) = sharedPrefs.edit { putBoolean(KEY_MY_BOOLEAN, value) }

    var myStringArray: Array<String>
        get() = sharedPrefs.getStringSet(KEY_MY_ARRAY, emptySet())?.toTypedArray()
            ?: emptyArray()
        set(value) = sharedPrefs.edit { putStringSet(KEY_MY_ARRAY, value.toSet()) }
}