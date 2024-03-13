package com.example.intaketracker

import android.content.Context
import android.content.SharedPreferences

class session(context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val SHARED_PREF_NAME = "session"
    private val SESSION_KEY = "is_logged_in"
    private val PREF_KEY_ID_TOKEN = "id_token"
    private val PREF_KEY_USERNAME = "username"

    init {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }
    fun saveLoginStatus(isLoggedIn: Boolean) {
        editor.putBoolean(SESSION_KEY, isLoggedIn).apply()
    }
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(SESSION_KEY, false)
    }

    fun saveUsername(username: String) {
        editor.putString(PREF_KEY_USERNAME, username).apply()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(PREF_KEY_USERNAME, null)
    }
    fun saveAuthToken(authToken: String?) {
        editor.putString(PREF_KEY_ID_TOKEN, authToken)
        editor.apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString(PREF_KEY_ID_TOKEN, null)
    }
    fun clearSession() {
        editor.remove(PREF_KEY_USERNAME)
        editor.remove(SESSION_KEY)
        editor.clear().apply()
    }
}