package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.SemuaMateriAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.SemuaVideoAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivitySearchDataBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchDataBinding
    @Inject lateinit var loading: LoadingAlertDialog
    private val viewModel: SearchViewModel by viewModels()
    private var listMateri = ArrayList<MateriModel>()
    private var listVideo = ArrayList<VideoModel>()
    private lateinit var adapterMateri: SemuaMateriAdapter
    private lateinit var adapterVideo: SemuaVideoAdapter
    private var cekSearchData = false   // Kalau false materi, kalau true video
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSearchData()
        setButton()
        setRadioButton()
        fetchDataMateri()
        getDataMateri()
        fetchDataVideo()
        getDataVideo()
    }

    private fun setSearchData() {
        binding.srcData.apply {
            requestFocus()
            onActionViewExpanded()
            queryHint = ""
        }
        // Search Data
        binding.srcData.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("MainActivityTAG", "onQueryTextSubmit:1: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("MainActivityTAG", "onQueryTextChange:2: $newText ")
                if(!cekSearchData){     // Materi
                    adapterMateri.searchData(newText!!)
                    Log.d("MainActivityTAG", "materi")
                }else {     // Video
                    adapterVideo.searchData(newText!!)
                    Log.d("MainActivityTAG", "video ")
                }
                return true
            }

        })
    }

    private fun setButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setRadioButton() {
        binding.apply {
            rbMateri.setOnClickListener {
                if(rbMateri.isChecked){
                    rbMateri.setTextColor(resources.getColor(R.color.white))
                    rbVideo.setTextColor(resources.getColor(R.color.primaryColor))
                    cekSearchData = false
                    setAdapterMateri(listMateri)
                }
            }
            rbVideo.setOnClickListener {
                if(rbVideo.isChecked){
                    rbVideo.setTextColor(resources.getColor(R.color.white))
                    rbMateri.setTextColor(resources.getColor(R.color.primaryColor))
                    cekSearchData = true
                    setAdapterVideo(listVideo)
                }
            }
        }
    }

    private fun fetchDataMateri() {
        viewModel.fetchDataMateri()
    }

    private fun getDataMateri() {
        viewModel.getDataMateri().observe(this@SearchDataActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@SearchDataActivity)
                is UIState.Failure-> setDataFailureMateri(result.message)
                is UIState.Success-> setDataSuccessMateri(result.data)
            }
        }
    }

    private fun setDataFailureMateri(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@SearchDataActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setDataSuccessMateri(data: ArrayList<MateriModel>) {
        loading.alertDialogCancel()
        val sort = data.sortedWith(compareBy { it.namaMateri })
        val dataArrayList = arrayListOf<MateriModel>()
        dataArrayList.addAll(sort)
        listMateri = dataArrayList
        setAdapterMateri(dataArrayList)
    }

    private fun setAdapterMateri(data: ArrayList<MateriModel>) {
        binding.apply {
            adapterMateri = SemuaMateriAdapter(data)
            rvData.layoutManager = LinearLayoutManager(
                this@SearchDataActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvData.adapter = adapterMateri
        }
    }

    private fun fetchDataVideo() {
        viewModel.fetchDataVideo()
    }

    private fun getDataVideo() {
        viewModel.getDataVideo().observe(this@SearchDataActivity){result->
            when(result){
                is UIState.Loading-> {

                }
                is UIState.Failure-> setDataFailureVideo(result.message)
                is UIState.Success-> setDataSuccessVideo(result.data)
            }
        }
    }

    private fun setDataFailureVideo(message: String) {
        Toast.makeText(this@SearchDataActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setDataSuccessVideo(data: ArrayList<VideoModel>) {
        val sort = data.sortedWith(compareBy { it.namaVideo })
        val dataArrayList = arrayListOf<VideoModel>()
        dataArrayList.addAll(sort)
        listVideo = dataArrayList
//        setAdapterVideo(dataArrayList)
    }

    private fun setAdapterVideo(data: ArrayList<VideoModel>) {
        binding.apply {
            adapterVideo = SemuaVideoAdapter(data)
            rvData.layoutManager = LinearLayoutManager(
                this@SearchDataActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvData.adapter = adapterVideo
        }
    }
}