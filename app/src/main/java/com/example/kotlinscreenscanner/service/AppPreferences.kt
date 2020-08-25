package com.timelysoft.tsjdomcom.service


import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    private const val NAME = "TsjDom"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var token: String?
        get() = preferences.getString("token", "")
        set(value) = preferences.edit {
            it.putString("token", value)
        }

    var isLogined: Boolean
        get() = preferences.getBoolean("isLogined", false)
        set(value) = preferences.edit {
            it.putBoolean("isLogined", value)
        }

    var started: Boolean
        get() = preferences.getBoolean("started", false)
        set(value) = preferences.edit() {
            it.putBoolean("started", value)
        }

    var email: String?
        get() = preferences.getString("email", "")
        set(value) = preferences.edit {
            it.putString("email", value)
        }


    fun clear() {
        preferences.edit {
            it.putString("token", "")
            it.putBoolean("isLogined", false)
            it.putString("email", "")
            it.putString("refresh_token", "")
            it.apply()
        }
    }

    var licNumber: Int?
        get() = preferences.getInt("licNumber", 0)
        set(value) = preferences.edit {
            if (value != null) {
                it.putInt("licNumber", value)
            }
        }
}