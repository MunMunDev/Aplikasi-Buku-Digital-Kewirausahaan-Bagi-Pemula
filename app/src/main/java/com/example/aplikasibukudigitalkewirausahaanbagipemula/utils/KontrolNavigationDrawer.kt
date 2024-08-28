package com.example.aplikasibukudigitalkewirausahaanbagipemula.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.akun.AdminSemuaAkunActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.main.AdminMateriActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.video.AdminVideoActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.login.LoginActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.akun.AkunActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.main.MainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.materi.MateriActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.video.VideoActivity

class KontrolNavigationDrawer(var context: Context) {
    var sharedPreferences = SharedPreferencesLogin(context)
    fun cekSebagai(navigation: com.google.android.material.navigation.NavigationView){
        if(sharedPreferences.getSebagai() == "admin"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_admin)
        } else{
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_user)
        }
    }
    @SuppressLint("ResourceAsColor")
    fun onClickItemNavigationDrawer(navigation: com.google.android.material.navigation.NavigationView, navigationLayout: DrawerLayout, igNavigation:ImageView, activity: Activity){
        navigation.setNavigationItemSelectedListener {
            if(sharedPreferences.getSebagai() == "admin"){
                when(it.itemId){
                    R.id.adminNavDrawerHome -> {
                        val intent = Intent(context, AdminMainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerMateri -> {
                        val intent = Intent(context, AdminMateriActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerVideo -> {
                        val intent = Intent(context, AdminVideoActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
//                    R.id.adminNavDrawerAkun -> {
//                        val intent = Intent(context, AdminSemuaAkunActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
                    R.id.adminBtnKeluar ->{
                        logout(activity)
                    }
                }

            } else{
                when(it.itemId){
                    R.id.userNavDrawerHome -> {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerMateri -> {
                        val intent = Intent(context, MateriActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerVideo -> {
                        val intent = Intent(context, VideoActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
//                    R.id.userNavDrawerAkun -> {
//                        val intent = Intent(context, AkunActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.userBtnKeluar ->{
//                        logout(activity)
//                    }
                }
            }
            navigationLayout.setBackgroundColor(R.color.white)
            navigationLayout.closeDrawer(GravityCompat.START)
            true
        }
        // garis 3 navigasi
        igNavigation.setOnClickListener {
            navigationLayout.openDrawer(GravityCompat.START)
        }
    }

    fun logout(activity: Activity){
        sharedPreferences.setLogin(0, "", "","", "","")
        context.startActivity(Intent(context, MainActivity::class.java))
        activity.finish()

//        val viewAlertDialog = View.inflate(context, R.layout.alert_dialog_logout, null)
//        val btnLogout = viewAlertDialog.findViewById<Button>(R.id.btnLogout)
//        val btnBatalLogout = viewAlertDialog.findViewById<Button>(R.id.btnBatalLogout)
//
//        val alertDialog = AlertDialog.Builder(context)
//        alertDialog.setView(viewAlertDialog)
//        val dialog = alertDialog.create()
//        dialog.show()
//
//        btnLogout.setOnClickListener {
//            sharedPreferences.setLogin(0, "", "","", "","")
//            context.startActivity(Intent(context, LoginActivity::class.java))
//            activity.finish()
//        }
//        btnBatalLogout.setOnClickListener {
//            dialog.dismiss()
//        }
    }
}