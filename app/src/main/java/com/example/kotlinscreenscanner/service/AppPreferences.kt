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

    var login: String?
        get() = preferences.getString("login", "")
        set(value) = preferences.edit {
            it.putString("login", value)
        }

    var number: String?
        get() = preferences.getString("number", "")
        set(value) = preferences.edit {
            it.putString("number", value)
        }

    var isLogined: Boolean
        get() = preferences.getBoolean("isLogined", false)
        set(value) = preferences.edit {
            it.putBoolean("isLogined", value)
        }

    var isRemember: Boolean
        get() = preferences.getBoolean("isRemember", false)
        set(value) = preferences.edit {
            it.putBoolean("isRemember", value)
        }

    var isValid: Boolean
        get() = preferences.getBoolean("isValid", false)
        set(value) = preferences.edit {
            it.putBoolean("isValid", value)
        }

    var id: Int
        get() = preferences.getInt("id", id)
        set(value) = preferences.edit {
            it.putInt("id", value)
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

    fun clearLogin() {
        preferences.edit {
            it.putString("email", "")
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

    fun String.toFullPhone(): String {
        return "996" + this.replace(" ", "")
    }
}