package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminMainBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.AdminMateriActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.video.AdminVideoActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminMainActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminMainActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            cvMateri.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminMateriActivity::class.java))
                finish()
            }
            cvVideo.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminVideoActivity::class.java))
                finish()
            }
            cvAkun.setOnClickListener {

            }
        }
    }
}