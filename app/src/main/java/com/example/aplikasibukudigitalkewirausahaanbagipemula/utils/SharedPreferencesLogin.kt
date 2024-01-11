package com.example.aplikasibukudigitalkewirausahaanbagipemula.utils

import android.content.Context
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel

class SharedPreferencesLogin(val context: Context) {
    val keyIdUser = "keyIdUser"
    val keyNama = "keyNama"
    val keyUmur = "keyUmur"
    val keyNomorHp = "keyNomorHp"
    val keyUsername = "keyUsername"
    val keyPassword = "keyPassword"
    val keySebagai = "keySebagai"

    var sharedPref = context.getSharedPreferences("sharedpreference_login", Context.MODE_PRIVATE)
    var editPref = sharedPref.edit()

    fun setLogin(id_user:Int, idBlok:String,  nama:String, alamat:String, nomorHp:String, username:String, password:String, sebagai:String){
        editPref.apply{
            putInt(keyIdUser, id_user)
            putString(keyNama, nama)
            putString(keyUmur, alamat)
            putString(keyNomorHp, nomorHp)
            putString(keyUsername, username)
            putString(keyPassword, password)
            putString(keySebagai, sebagai)
            apply()
        }
    }

    fun getIdUser(): Int{
        return sharedPref.getInt(keyIdUser, 0)
    }
    fun getNama():String{
        return sharedPref.getString(keyNama, "").toString()
    }
    fun getUmur():String{
        return sharedPref.getString(keyUmur, "").toString()
    }
    fun getNomorHp():String{
        return sharedPref.getString(keyNomorHp, "").toString()
    }
    fun getUsername():String{
        return sharedPref.getString(keyUsername, "").toString()
    }
    fun getPassword(): String{
        return sharedPref.getString(keyPassword, "").toString()
    }
    fun getSebagai(): String{
        return sharedPref.getString(keySebagai, "").toString()
    }
}