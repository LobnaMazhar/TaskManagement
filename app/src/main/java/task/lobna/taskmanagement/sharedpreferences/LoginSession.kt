package com.shaiik.authentication

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import task.lobna.taskmanagement.data.UserModel

object LoginSession {
    private val USER_DATA_KEY = "userData"
    private var loginFile: SharedPreferences? = null

    private fun initLoginSharedPreference(context: Context) {
        loginFile = context.getSharedPreferences("loginFile", Context.MODE_PRIVATE)
    }

    fun setUserData(activity: Context, user: UserModel) {
        initLoginSharedPreference(activity)
        val editor = loginFile!!.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString(USER_DATA_KEY, json)
        editor.apply()
    }

    fun getUserData(activity: Context): UserModel {
        initLoginSharedPreference(activity)
        val gson = Gson()
        val json = loginFile!!.getString(USER_DATA_KEY, "")
        val userModel = gson.fromJson<UserModel>(json, UserModel::class.java)

        return userModel
    }

    fun clearData(activity: Context) {
        initLoginSharedPreference(activity)
        val editor = loginFile!!.edit()
        editor.clear()
        editor.apply()
    }
}
