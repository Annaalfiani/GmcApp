package com.annaalfiani.gmcapps.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.text.NumberFormat
import java.util.*

class Utilities {
    companion object {
        fun isFirstTime(context: Context) : Boolean {
            val pref = context.getSharedPreferences("UTILS", MODE_PRIVATE)
            val bool = pref.getBoolean("FIRST_TIME", true)
            return bool
        }

        fun setFirstTime(context: Context, value : Boolean){
            val pref = context.getSharedPreferences("UTILS", MODE_PRIVATE)
            pref.edit().apply{
                putBoolean("FIRST_TIME", value)
                apply()
            }
        }

        fun setToIDR(num : Int) : String {
            val localeID = Locale("in", "ID")
            val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(num)
        }


        fun getToken(c : Context) : String? {
            val s = c.getSharedPreferences("USER", MODE_PRIVATE)
//            return "Bearer m3PcfE1iUh7vxQJ6bJZKjlUy6umZhiKIHwbzHKi0P79g2t4rSfFYz30fqPVLMsl1BxhYfnI4XEmEFvSr"
            return s?.getString("TOKEN", null)
        }

        fun setToken(context: Context, token : String){
            val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
            pref.edit().apply {
                putString("TOKEN", token)
                apply()
            }
        }

        fun isValidEmail(email : String) : Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun isValidPassword(password : String) = password.length >= 8
    }
}