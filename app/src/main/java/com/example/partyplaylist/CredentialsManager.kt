package com.example.partyplaylist

import android.content.Context

object CredentialsManager {
    private val PREFERENCES_NAME = "SpotifyUserToken"
    private val ACCESS_TOKEN = "user_access_token"

    fun saveCredentials(context: Context, token: String) {
        val sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        sp!!.edit().putString(ACCESS_TOKEN, token).apply()
    }

    fun getAccessToken(context: Context): String? {
        val sp = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sp!!.getString(ACCESS_TOKEN, null)
    }
}