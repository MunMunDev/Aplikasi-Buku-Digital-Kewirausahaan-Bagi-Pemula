package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.PopulerMateriAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.PopulerVideoAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityMainBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.materi.MateriActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.search.SearchDataActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.video.VideoActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    @Inject lateinit var loading: LoadingAlertDialog
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var adapter: PopulerMateriAdapter
    private lateinit var adapterVideoPopuler: PopulerVideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        setKontrolNavigationDrawer()
        fetchDataMateri()
        getDataMateri()
        fetchDataVideo()
        getDataVideo()
    }

    private fun setButton() {
        binding.apply {
            srcData.setOnClickListener {
                startActivity(Intent(this@MainActivity, SearchDataActivity::class.java))
            }
            btnMateri.setOnClickListener {
                startActivity(Intent(this@MainActivity, MateriActivity::class.java))
            }
            btnVideo.setOnClickListener {
                startActivity(Intent(this@MainActivity, VideoActivity::class.java))
            }
            btnAkun.setOnClickListener {

            }
            tvViewAllMateri.setOnClickListener {
                startActivity(Intent(this@MainActivity, MateriActivity::class.java))
            }
            tvViewAllVideo.setOnClickListener {
                startActivity(Intent(this@MainActivity, VideoActivity::class.java))
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

    private fun fetchDataMateri() {
        viewModel.fetchDataMateriPopuler()
    }

    private fun getDataMateri() {
        viewModel.getDataMateri().observe(this@MainActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@MainActivity)
                is UIState.Failure-> setFailureDataMateri(result.message)
                is UIState.Success-> setSuccessDataMateri(result.data)
            }
        }
    }

    private fun setFailureDataMateri(message: String) {
        loading.alertDialogCancel()
        setOffShimmerMateri()
        setNoHaveMateri()
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessDataMateri(data: ArrayList<MateriModel>) {
        loading.alertDialogCancel()
        setOffShimmerMateri()

        if(data.isNotEmpty()){
            setHaveMateri()
            val sort = data.sortedWith(compareBy { it.jumlahPelihat })
            val dataArrayList = arrayListOf<MateriModel>()
            dataArrayList.addAll(sort)
            setAdapter(dataArrayList)

//            Toast.makeText(this@MainActivity, "ada data ${dataArrayList.size}", Toast.LENGTH_SHORT).show()
//            for (value in dataArrayList){
//                Log.d("MainActivityTAG", "oke: ${value.namaMateri} \n jumlah: ${value.jumlahPelihat}")
//            }
        } else{
            setNoHaveMateri()
        }
    }

    private fun setAdapter(data: ArrayList<MateriModel>) {
        adapter = PopulerMateriAdapter(data)
        binding.apply {
            rvTrendsMateri.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            rvTrendsMateri.adapter = adapter
        }
    }

    private fun setOffShimmerMateri(){
        binding.apply {
            smTitleTrendsMateri.visibility = View.GONE
            smViewAllMateri.visibility = View.GONE
            smMateri.visibility = View.GONE

            tvTitleTrendsMateri.visibility = View.VISIBLE
            tvViewAllMateri.visibility = View.VISIBLE
            rvTrendsMateri.visibility = View.VISIBLE
        }
    }

    private fun setHaveMateri(){
        binding.apply {
            tvTitleTrendsMateri.visibility = View.VISIBLE
            tvViewAllMateri.visibility = View.VISIBLE
            rvTrendsMateri.visibility = View.VISIBLE
        }
    }
    private fun setNoHaveMateri() {
        binding.apply {
            tvTitleTrendsMateri.visibility = View.GONE
            tvViewAllMateri.visibility = View.GONE
            rvTrendsMateri.visibility = View.GONE
        }
    }

    private fun fetchDataVideo() {
        viewModel.fetchDataVideoPopuler()
    }

    private fun getDataVideo() {
        viewModel.getDataVideo().observe(this@MainActivity){result->
            when(result){
                is UIState.Loading-> {

                }
                is UIState.Failure-> setFailureDataVideo(result.message)
                is UIState.Success-> setSuccessDataVideo(result.data)
            }
        }
    }

    private fun setFailureDataVideo(message: String) {
        setOffShimmerVideo()
        setNoHaveVideo()
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessDataVideo(data: ArrayList<VideoModel>) {
        setOffShimmerVideo()

        if(data.isNotEmpty()){
            setHaveVideo()
            val sort = data.sortedWith(compareBy { it.jumlahPelihat })
            val dataArrayList = arrayListOf<VideoModel>()
            dataArrayList.addAll(sort)
            setAdapterVideoPopuler(dataArrayList)
        } else{
            setNoHaveVideo()
        }
    }

    private fun setAdapterVideoPopuler(data: ArrayList<VideoModel>) {
        adapterVideoPopuler = PopulerVideoAdapter(data)
        binding.apply {
            rvTrendsVideo.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            rvTrendsVideo.adapter = adapterVideoPopuler
        }
    }

    private fun setOffShimmerVideo(){
        binding.apply {
            smTitleTrendsVideo.visibility = View.GONE
            smViewAllVideo.visibility = View.GONE
            smVideo.visibility = View.GONE

            tvTitleTrendsVideo.visibility = View.VISIBLE
            tvViewAllVideo.visibility = View.VISIBLE
            rvTrendsVideo.visibility = View.VISIBLE
        }
    }

    private fun setHaveVideo() {
        binding.apply {
            tvTitleTrendsVideo.visibility = View.VISIBLE
            tvViewAllVideo.visibility = View.VISIBLE
            rvTrendsVideo.visibility = View.VISIBLE
        }
    }

    private fun setNoHaveVideo() {
        binding.apply {
            tvTitleTrendsVideo.visibility = View.GONE
            tvViewAllVideo.visibility = View.GONE
            rvTrendsVideo.visibility = View.GONE
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