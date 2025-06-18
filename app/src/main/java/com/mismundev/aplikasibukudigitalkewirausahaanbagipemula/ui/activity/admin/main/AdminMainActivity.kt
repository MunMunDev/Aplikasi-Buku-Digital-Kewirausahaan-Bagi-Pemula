package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminMainBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.akun.AdminSemuaAkunActivity
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.main.AdminMateriActivity
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.video.AdminVideoActivity
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer

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
                startActivity(Intent(this@AdminMainActivity, AdminSemuaAkunActivity::class.java))
                finish()
            }
        }
    }

    private var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@AdminMainActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)
    }
}