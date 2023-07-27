package com.example.calender.util

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Singleton

@Singleton
class SharedPref(context: Context) {

   private val sharedPref = context.getSharedPreferences("pref" , Context.MODE_PRIVATE)


    fun saveUserId(key : String , value : Int){
        val prefsEditor: SharedPreferences.Editor = sharedPref.edit()
        prefsEditor.putInt(key , value)
        prefsEditor.apply()
    }

    fun getUserId(key : String) =
        sharedPref.getInt(key , -1)
}