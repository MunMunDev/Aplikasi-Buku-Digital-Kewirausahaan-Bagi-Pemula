package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.video

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.adapter.user.SemuaVideoAdapter
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityVideoBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.main.MainActivity
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    @Inject lateinit var loading: LoadingAlertDialog
    private val viewModel: VideoViewModel by viewModels()
    private lateinit var adapter: SemuaVideoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationDrawer()
        fetchDataVideo()
        getDataVideo()
    }

    private fun setNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@VideoActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@VideoActivity)
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
        setStopShimmer()
        loading.alertDialogCancel()
    }

    private fun setSuccessDataVideo(data: ArrayList<VideoModel>) {
        loading.alertDialogCancel()
        setStopShimmer()
        val sort = data.sortedWith(compareBy { it.namaVideo })
        val dataArrayList = arrayListOf<VideoModel>()
        dataArrayList.addAll(sort)
        setRecyclerViewVideo(dataArrayList)
    }

    private fun setRecyclerViewVideo(data: ArrayList<VideoModel>) {
        adapter = SemuaVideoAdapter(data, object : OnClickItem.ClickVideo{
            override fun clickItemVideo(video: VideoModel, it: View) {
                viewModel.postWatchVideo(video.noVideo!!)
                setToYoutube(video.urlVideo)
            }

        })
        binding.apply {
            rvVideo.layoutManager = LinearLayoutManager(
                this@VideoActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvVideo.adapter = adapter
        }
    }
    private fun setToYoutube( urlVideo: String?) {
        val id = adapter.searchIdUrlVideo(urlVideo!!)
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            rvVideo.visibility = View.VISIBLE

            smVideo.stopShimmer()
            smVideo.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@VideoActivity, MainActivity::class.java))
        finish()
    }
}