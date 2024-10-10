package com.goglitter.utils

import android.content.Context
import com.goglitter.utils.Constants.AUTH_TOKEN
import com.goglitter.utils.Constants.EMAIL
import com.goglitter.utils.Constants.EMAILOTP
import com.goglitter.utils.Constants.EVERIFIED
import com.goglitter.utils.Constants.FCM_TOKEN
import com.goglitter.utils.Constants.GLITTER_ID
import com.goglitter.utils.Constants.IMAGE_URL
import com.goglitter.utils.Constants.NAME
import com.goglitter.utils.Constants.OTP
import com.goglitter.utils.Constants.PREFS_TOKEN_FILE
import com.goglitter.utils.Constants.VALUE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * @Author: Padma Ahire
 * @Date: 16/06/23
 */

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(string: String) {
        val editor = prefs.edit()
        editor.putString(AUTH_TOKEN, string)
        editor.apply()
    }
    fun getToken(): String? {
        return prefs.getString(AUTH_TOKEN, null)
    }
    fun saveSalesId(string: String) {
        val editor = prefs.edit()
        editor.putString(GLITTER_ID, string)
        editor.apply()
    }
    fun getSalesId(): String? {
        return prefs.getString(GLITTER_ID, null)
    }
    fun saveMobile(string: String) {
        val editor = prefs.edit()
        editor.putString(EVERIFIED, string)
        editor.apply()
    }
    fun getMobile(): String? {
        return prefs.getString(EVERIFIED, null)
    }
    fun saveValue(string: String) {
        val editor = prefs.edit()
        editor.putString(VALUE, string)
        editor.apply()
    }
    fun getValue(): String? {
        return prefs.getString(VALUE, null)
    }
    fun saveEmail(string: String) {
        val editor = prefs.edit()
        editor.putString(EMAIL, string)
        editor.apply()
    }
    fun getEmail(): String? {
        return prefs.getString(EMAIL, null)
    }
    fun saveFcmToken(string: String) {
        val editor = prefs.edit()
        editor.putString(FCM_TOKEN, string)
        editor.apply()
    }
    fun getFcmToken(): String? {
        return prefs.getString(FCM_TOKEN, null)
    }
    fun saveFullName(string: String) {
        val editor = prefs.edit()
        editor.putString(NAME, string)
        editor.apply()
    }
    fun getFullName(): String? {
        return prefs.getString(NAME, null)
    }
    fun saveMobileOTP(string: String) {
        val editor = prefs.edit()
        editor.putString(OTP, string)
        editor.apply()
    }

    fun getMobileOTP(): String? {
        return prefs.getString(OTP, null)
    }
    fun saveEmailOTP(string: String) {
        val editor = prefs.edit()
        editor.putString(EMAILOTP, string)
        editor.apply()
    }
    fun getEmailOTP(): String? {
        return prefs.getString(EMAILOTP, null)
    }
    fun saveImageUrl(string: String) {
        val editor = prefs.edit()
        editor.putString(IMAGE_URL, string)
        editor.apply()
    }
    fun getImageUrl(): String? {
        return prefs.getString(IMAGE_URL, null)
    }
    fun clear(){
        val editor = prefs.edit()
        editor.clear()
    }

}