package com.example.note.data.preferences

import android.content.Context

private const val PREFS_NAME = "auth_prefs"
private const val KEY_TOKEN = "jwt_token"
private const val KEY_IS_SUPER = "is_super"

class TokenStorage(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun hasToken(): Boolean = getToken() != null

    fun saveIsSuper(isSuper: Boolean) {
        prefs.edit().putBoolean(KEY_IS_SUPER, isSuper).apply()
    }

    fun isSuper(): Boolean = prefs.getBoolean(KEY_IS_SUPER, false)

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_IS_SUPER).apply()
    }
}
