package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.AdminMateriAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KataAcak
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class AdminMateriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMateriBinding
    @Inject lateinit var loading: LoadingAlertDialog
    @Inject lateinit var kataAcak: KataAcak
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var adapter: AdminMateriAdapter
    private val viewModel: AdminMateriViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchDataMateri()
        getDataMateri()

        setKontrolNavigationDrawer()
        setButton()
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminMateriActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminMateriActivity)
        }
    }
    private fun fetchDataMateri() {
        viewModel.fetchDataMateri()
    }

    private fun getDataMateri() {
        viewModel.getDataMateri().observe(this@AdminMateriActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriActivity)
                is UIState.Failure-> setFailureDataMateri(result.message)
                is UIState.Success-> setSuccessDataMateri(result.data)
            }
        }
    }

    private fun setFailureDataMateri(message: String) {
        Toast.makeText(this@AdminMateriActivity, message, Toast.LENGTH_SHORT).show()
        setStopShimmer()
        loading.alertDialogCancel()
    }

    private fun setSuccessDataMateri(data: ArrayList<MateriModel>) {
        loading.alertDialogCancel()
        setStopShimmer()
        val sort = data.sortedWith(compareBy { it.namaMateri })
        val dataArrayList = arrayListOf<MateriModel>()
        dataArrayList.addAll(sort)
        setRecyclerViewMateri(dataArrayList)
    }

    private fun setRecyclerViewMateri(data: ArrayList<MateriModel>) {
        adapter = AdminMateriAdapter(data, object: AdminMateriAdapter.ClickButton{
            override fun clickItem(data: MateriModel, it: View) {
                setClickSetting(data, it)
            }

        })
        binding.apply {
            rvMateri.layoutManager = LinearLayoutManager(
                this@AdminMateriActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvMateri.adapter = adapter
        }
    }

    private fun setClickSetting(data: MateriModel, it: View) {
        val popupMenu = PopupMenu(this@AdminMateriActivity, it)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.setOnMenuItemClickListener(object :
            PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                when (menuItem!!.itemId) {
                    R.id.rincian -> {

                        return true
                    }
                    R.id.edit -> {
                        setDialogEditMateri(data)
                        return true
                    }
                    R.id.hapus -> {

                        return true
                    }
                }
                return true
            }

        })
        popupMenu.show()
    }

    private fun setDialogEditMateri(data: MateriModel) {
        val view = AlertDialogMateriBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tVTitle.text = "Update Materi"
            etEditNamaMateri.setText(data.namaMateri)
            etEditNamaPenulis.setText(data.namaPenulis)
            etEditJumlahPelihat.setText(data.jumlahPelihat)

            btnSimpan.setOnClickListener {
                var check = true
                if(etEditNamaMateri.text.isEmpty()){
                    etEditNamaMateri.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditNamaPenulis.text.isEmpty()){
                    etEditNamaMateri.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditFile.text.isEmpty()){
                    etEditNamaMateri.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditJumlahPelihat.text.isEmpty()){
                    etEditNamaMateri.error = "Tidak Boleh Kosong"
                    check = false
                }

                if(check){
                    postUpdateData(
                        data.idMateri!!,
                        etEditNamaMateri.text.toString().trim(),
                        etEditNamaPenulis.text.toString().trim(),
                        etEditFile.text.toString().trim(),
                        etEditJumlahPelihat.text.toString().trim()
                    )
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateData(idMateri:String, namaMateri: String, namaPenulis: String, file: String, jumlahPelihat: String) {

    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                dialogTambahData()
            }
        }
    }

    private fun dialogTambahData() {
        val view = AlertDialogMateriBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etEditFile.setOnClickListener {
                uploadPdf()
            }

            btnSimpan.setOnClickListener {
                var check = true
                if(etEditNamaMateri.text.isEmpty()){
                    etEditNamaMateri.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditNamaPenulis.text.isEmpty()){
                    etEditNamaMateri.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditFile.text.isEmpty()){
                    etEditNamaMateri.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditJumlahPelihat.text.isEmpty()){
                    etEditNamaMateri.error = "Tidak Boleh Kosong"
                    check = false
                }

                if(check){
                    postTambahData(
                        etEditNamaMateri.text.toString().trim(),
                        etEditNamaPenulis.text.toString().trim(),
                        etEditFile.text.toString().trim(),
                        etEditJumlahPelihat.text.toString().trim()
                    )
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun uploadPdf(): MultipartBody.Part{
        requestForStoragePermissions()

        val file = File("/download")
        val requestFile = RequestBody.create(MediaType.parse("image/jpg"), file)
        val body = MultipartBody.Part.createFormData("files[0]", file.name, requestFile)

        Toast.makeText(this@AdminMateriActivity, "hai", Toast.LENGTH_SHORT).show()


        return body
    }

    private val STORAGE_PERMISSION_CODE = 23

    private fun requestForStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11 (R) or above
            Environment.isExternalStorageManager()
        } else {
            //Below android 11
            val write =
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
        }

        //Android is 11 (R) or above
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            try {
//                val intent = Intent()
//                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
//                val uri = Uri.fromParts("package", this.packageName, null)
//                intent.data = uri
//
//            } catch (e: Exception) {
//                val intent = Intent()
//                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
////                storageActivityResultLauncher.launch(intent)
//            }
//        } else {
//            //Below android 11
//            ActivityCompat.requestPermissions(
//                this, arrayOf<String>(
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                ),
//                STORAGE_PERMISSION_CODE
//            )
//        }
    }

    private fun postTambahData(namaMateri: String, namaPenulis: String, file: String, jumlahPelihat: String) {

    }

    private fun setStopShimmer(){
        binding.apply {
            rvMateri.visibility = View.VISIBLE

            smMateri.stopShimmer()
            smMateri.visibility = View.GONE
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminMateriActivity, AdminMainActivity::class.java))
        finish()
    }
}