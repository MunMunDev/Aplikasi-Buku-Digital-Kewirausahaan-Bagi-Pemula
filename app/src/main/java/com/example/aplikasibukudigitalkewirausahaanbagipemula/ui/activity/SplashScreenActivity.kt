package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.login.LoginActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.main.MainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.SharedPreferencesLogin

class SplashScreenActivity : AppCompatActivity() {
    lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.let {
            it.hide()
        }

        sharedPreferencesLogin = SharedPreferencesLogin(this@SplashScreenActivity)

        Handler(Looper.getMainLooper()).postDelayed({
            if(sharedPreferencesLogin.getIdUser() == 0){
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
//                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
            }
            else{
                if(sharedPreferencesLogin.getSebagai() == "admin"){
                    startActivity(Intent(this@SplashScreenActivity, AdminMainActivity::class.java))
                    finish()
                }
                else {
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                }
            }
        }, 3000)
    }
}