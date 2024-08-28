package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityMainBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.login.LoginActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.akun.AkunActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.materi.MateriActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.search.SearchDataActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.video.VideoActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var loading: LoadingAlertDialog
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        setKontrolNavigationDrawer()
    }

    private fun setButton() {
        binding.apply {
            srcData.setOnClickListener {
                startActivity(Intent(this@MainActivity, SearchDataActivity::class.java))
            }
            btnMateri.setOnClickListener {
                startActivity(Intent(this@MainActivity, MateriActivity::class.java))
                finish()
            }
            btnVideo.setOnClickListener {
                startActivity(Intent(this@MainActivity, VideoActivity::class.java))
                finish()
            }
            btnLogin.setOnClickListener {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@MainActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@MainActivity)
        }
    }


    private var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@MainActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)
    }
}