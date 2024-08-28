package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.video

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.admin.AdminVideoAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminVideoBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogKonfirmasiBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogVideoBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminVideoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminVideoBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var adapter: AdminVideoAdapter
    private val viewModel: AdminVideoViewModel by viewModels()
    @Inject lateinit var loading: LoadingAlertDialog
    private var tempAlertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchDataVideo()
        getDataVideo()
        setKontrolNavigationDrawer()
        setButton()
        getTambahVideo()
        getUpdateVideo()
        getHapusVideo()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminVideoActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminVideoActivity)
        }
    }
    private fun fetchDataVideo() {
        viewModel.fetchDataVideo()
    }

    private fun getDataVideo() {
        viewModel.getDataVideo().observe(this@AdminVideoActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminVideoActivity)
                is UIState.Failure-> setFailureDataVideo(result.message)
                is UIState.Success-> setSuccessDataVideo(result.data)
            }
        }
    }

    private fun setFailureDataVideo(message: String) {
        Toast.makeText(this@AdminVideoActivity, message, Toast.LENGTH_SHORT).show()
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
        adapter = AdminVideoAdapter(data, object: OnClickItem.ClickVideo{
            override fun clickItemVideo(video: VideoModel, it: View) {
                viewModel.postWatchVideo(video.noVideo!!)
                setClickSetting(video, it)
            }
        })
        binding.apply {
            rvVideo.layoutManager = LinearLayoutManager(
                this@AdminVideoActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvVideo.adapter = adapter
        }
    }

    private fun setClickSetting(data: VideoModel, it: View) {
        val popupMenu = PopupMenu(this@AdminVideoActivity, it)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.setOnMenuItemClickListener(object :
            PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                when (menuItem!!.itemId) {
                    R.id.rincian -> {
                        adapter.setToYoutube(this@AdminVideoActivity, data.urlVideo)
                        return true
                    }
                    R.id.edit -> {
                        setDialogEditVideo(data)
                        return true
                    }
                    R.id.hapus -> {
                        setDialogHapusVideo(data)
                        return true
                    }
                }
                return true
            }

        })
        popupMenu.show()
    }

    private fun setDialogEditVideo(data: VideoModel) {
        val view = AlertDialogVideoBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminVideoActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tVTitle.text = "Update Video"
            etEditNamaVideo.setText(data.namaVideo)
            etEditUrlVideo.setText(data.urlVideo)
            etEditJumlahPelihat.setText(data.jumlahPelihat)

            btnSimpan.setOnClickListener {
                var check = true
                if(etEditNamaVideo.text.isEmpty()){
                    etEditNamaVideo.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditUrlVideo.text.isEmpty()){
                    etEditNamaVideo.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditJumlahPelihat.text.isEmpty()){
                    etEditNamaVideo.error = "Tidak Boleh Kosong"
                    check = false
                }

                if(check){
                    tempAlertDialog = dialogInputan
                    postUpdateVideo(
                        data.noVideo!!,
                        etEditNamaVideo.text.toString().trim(),
                        etEditUrlVideo.text.toString().trim(),
                        etEditJumlahPelihat.text.toString().trim()
                    )
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateVideo(noVideo:String, namaVideo: String, urlVideo: String, jumlahPelihat: String) {
        viewModel.postUpdateData(noVideo, namaVideo, urlVideo, jumlahPelihat)
    }

    private fun getUpdateVideo(){
        viewModel.getResponseUpdateVideo().observe(this@AdminVideoActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminVideoActivity)
                is UIState.Failure-> setFailureUpdateData(result.message)
                is UIState.Success-> setSuccessUpdateData(result.data)
            }
        }
    }

    private fun setFailureUpdateData(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminVideoActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessUpdateData(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                if(tempAlertDialog!=null){
                    tempAlertDialog!!.dismiss()
                }
                Toast.makeText(this@AdminVideoActivity, "Berhasil Update Data", Toast.LENGTH_SHORT).show()
                fetchDataVideo()
            }
        }
        tempAlertDialog = null
        loading.alertDialogCancel()
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                dialogTambahData()
            }
        }
    }

    private fun setDialogHapusVideo(data: VideoModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminVideoActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Konfirmasi Penghapusan."
            tvBodyKonfirmasi.text = data.namaVideo
        }

        view.apply {
            btnKonfirmasi.setOnClickListener {
                tempAlertDialog = dialogInputan
                postHapusVideo(data.noVideo!!)
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusVideo(noVideo: String) {
        viewModel.postHapusData(noVideo)
    }

    private fun getHapusVideo(){
        viewModel.getResponseHapusVideo().observe(this@AdminVideoActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminVideoActivity)
                is UIState.Failure-> setFailureHapusVideo(result.message)
                is UIState.Success-> setSuccessHapusVideo(result.data)
            }
        }
    }

    private fun setFailureHapusVideo(message: String) {
        Toast.makeText(this@AdminVideoActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessHapusVideo(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@AdminVideoActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                if(tempAlertDialog!=null){
                    tempAlertDialog!!.dismiss()
                }
                fetchDataVideo()
            } else{
                Toast.makeText(this@AdminVideoActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this@AdminVideoActivity, "Ada masalah di API", Toast.LENGTH_SHORT).show()
        }

        tempAlertDialog = null
        loading.alertDialogCancel()
    }

    private fun dialogTambahData() {
        val view = AlertDialogVideoBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminVideoActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            btnSimpan.setOnClickListener {
                var check = true
                if(etEditNamaVideo.text.isEmpty()){
                    etEditNamaVideo.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditUrlVideo.text.isEmpty()){
                    etEditNamaVideo.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditJumlahPelihat.text.isEmpty()){
                    etEditNamaVideo.error = "Tidak Boleh Kosong"
                    check = false
                }

                if(check){
                    tempAlertDialog = dialogInputan
                    postTambahVideo(
                        etEditNamaVideo.text.toString().trim(),
                        etEditUrlVideo.text.toString().trim(),
                        etEditJumlahPelihat.text.toString().trim()
                    )
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }


    private fun postTambahVideo(namaVideo: String, urlVideo: String, jumlahPelihat: String) {
        viewModel.postTambahData(namaVideo, urlVideo, jumlahPelihat)
    }

    private fun getTambahVideo(){
        viewModel.getResponseTambahVideo().observe(this@AdminVideoActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminVideoActivity)
                is UIState.Failure-> setFailureTambahVideo(result.message)
                is UIState.Success-> setSuccessTambahVideo(result.data)
            }
        }
    }

    private fun setFailureTambahVideo(message: String) {
        Toast.makeText(this@AdminVideoActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessTambahVideo(data: ArrayList<ResponseModel>) {
        if(data[0].status == "0"){
            Toast.makeText(this@AdminVideoActivity, "Berhasil Tambah Data", Toast.LENGTH_SHORT).show()
            fetchDataVideo()

            if(tempAlertDialog!=null){
                tempAlertDialog!!.dismiss()
            }
        } else{
            Toast.makeText(this@AdminVideoActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
        }
        tempAlertDialog = null
        loading.alertDialogCancel()
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
        startActivity(Intent(this@AdminVideoActivity, AdminMainActivity::class.java))
        finish()
    }

}