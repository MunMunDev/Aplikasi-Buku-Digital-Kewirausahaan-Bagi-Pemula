package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.SemuaVideoAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityVideoBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import javax.inject.Inject

class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding
    @Inject lateinit var loading: LoadingAlertDialog
    private val viewModel: VideoViewModel by viewModels()
    private lateinit var adapter: SemuaVideoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        fetchDataVideo()
        getDataVideo()
    }
    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun fetchDataVideo() {
        viewModel.fetchDataVideo()
    }

    private fun getDataVideo() {
        viewModel.getDataVideo().observe(this@VideoActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@VideoActivity)
                is UIState.Failure-> setFailureDataVideo(result.message)
                is UIState.Success-> setSuccessDataVideo(result.data)
            }
        }
    }

    private fun setFailureDataVideo(message: String) {
        Toast.makeText(this@VideoActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessDataVideo(data: ArrayList<VideoModel>) {
        loading.alertDialogCancel()
        val sort = data.sortedWith(compareBy { it.namaVideo })
        val dataArrayList = arrayListOf<VideoModel>()
        dataArrayList.addAll(sort)
        setRecyclerViewVideo(dataArrayList)
    }

    private fun setRecyclerViewVideo(data: ArrayList<VideoModel>) {
        adapter = SemuaVideoAdapter(data)
        binding.apply {
            rvVideo.layoutManager = LinearLayoutManager(
                this@VideoActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvVideo.adapter = adapter
        }
    }
}