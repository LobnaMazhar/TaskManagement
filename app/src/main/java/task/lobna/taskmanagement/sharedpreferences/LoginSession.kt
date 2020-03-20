package com.shaiik.authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.google.gson.Gson
import task.lobna.taskmanagement.data.UserModel
import task.lobna.taskmanagement.ui.LoginActivity

object LoginSession {
    private const val USER_DATA_KEY = "userData"
    private const val IS_LOGGED_IN_KEY = "isLogin"
    private var loginFile: SharedPreferences? = null

    private fun initLoginSharedPreference(context: Context) {
        loginFile = context.getSharedPreferences("loginFile", Context.MODE_PRIVATE)
    }

    fun setUserData(activity: Context, user: UserModel) {
        initLoginSharedPreference(activity)
        val editor = loginFile!!.edit()
        val json = Gson().toJson(user)
        editor.putString(USER_DATA_KEY, json)
        editor.putBoolean(IS_LOGGED_IN_KEY, true)
        editor.apply()
    }

    fun getUserData(activity: Context): UserModel {
        initLoginSharedPreference(activity)
        val json = loginFile!!.getString(USER_DATA_KEY, "")
        return Gson().fromJson(json, UserModel::class.java)
    }

    fun isLoggedIn(activity: Context): Boolean {
        initLoginSharedPreference(activity)
        return loginFile!!.getBoolean(IS_LOGGED_IN_KEY, false)
    }

    fun clearData(activity: Context) {
        initLoginSharedPreference(activity)
        val editor = loginFile!!.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    }
}
