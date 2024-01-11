package com.example.aplikasibukudigitalkewirausahaanbagipemula.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.load.engine.Resource
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.main.MainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.materi.MateriActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.video.VideoActivity

class KontrolNavigationDrawer(var context: Context) {
    var sharedPreferences = SharedPreferencesLogin(context)
    fun cekSebagai(navigation: com.google.android.material.navigation.NavigationView){
        if(sharedPreferences.getSebagai() == "user"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_user)
        }
        else if(sharedPreferences.getSebagai() == "admin"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_admin)
        }
    }
    @SuppressLint("ResourceAsColor")
    fun onClickItemNavigationDrawer(navigation: com.google.android.material.navigation.NavigationView, navigationLayout: DrawerLayout, igNavigation:ImageView, activity: Activity){
        navigation.setNavigationItemSelectedListener {
            when(it.itemId) {
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
            }
            if(sharedPreferences.getSebagai() == "user"){
                when(it.itemId){
//                    R.id.userNavDrawerHome ->{
//                        val intent = Intent(context, MainActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.userNavDrawerRiwayatPembayaran ->{
//                        val intent = Intent(context, RiwayatPembayaranActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.userNavDrawerAkun ->{
//                        val intent = Intent(context, AkunActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.userBtnKeluar ->{
//                        logout(activity)
//                    }
                }
            }
            else if(sharedPreferences.getSebagai() == "admin"){
                when(it.itemId){
//                    R.id.adminNavDrawerHome ->{
//                        val intent = Intent(context, AdminMainActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.adminNavDrawerPerumahan->{
//                        val intent = Intent(context, AdminPerumahanActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.adminNavDrawerBiaya->{
//                        val intent = Intent(context, AdminBiayaActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.adminNavDrawerPembayaran->{
//                        val intent = Intent(context, AdminPembayaranActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.adminNavDrawerAkun ->{
//                        val intent = Intent(context, AdminUsersActivity::class.java)
//                        context.startActivity(intent)
//                        activity.finish()
//                    }
//                    R.id.btnAdminKeluar ->{
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
//            sharedPreferences.setLogin(0, "", "","", "","", "","")
////            sharedPreferences.setLogin(0, "","", "","", "","", "")
//            context.startActivity(Intent(context, LoginActivity::class.java))
//            activity.finish()
//        }
//        btnBatalLogout.setOnClickListener {
//            dialog.dismiss()
//        }
    }
}