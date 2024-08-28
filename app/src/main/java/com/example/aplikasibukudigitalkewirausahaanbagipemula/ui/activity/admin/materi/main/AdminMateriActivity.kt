package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.admin.AdminListMateriAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriListModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogKonfirmasiBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.detail.AdminMateriDetailActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.Constant
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KataAcak
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


@AndroidEntryPoint
class AdminMateriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMateriBinding
    @Inject lateinit var loading: LoadingAlertDialog
    @Inject lateinit var kataAcak: KataAcak
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var adapter: AdminListMateriAdapter
    private val viewModel: AdminMateriViewModel by viewModels()
    private var tempAlertDialog: AlertDialog? = null
    private var tempView: AlertDialogMateriBinding? = null
    private var filePdf: MultipartBody.Part? = null
    private var fileImage: MultipartBody.Part? = null

    private val TAG = "AdminMateriActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchDataMateri()
        getDataMateri()

        setKontrolNavigationDrawer()
        setButton()
        getPostTambahData()
        getUpdateMateri()
        getUpdateMateriNoImage()
        getHapusMateri()
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

    private fun setSuccessDataMateri(data: ArrayList<MateriListModel>) {
        loading.alertDialogCancel()
        setStopShimmer()
        val sort = data.sortedWith(compareBy { it.judul })
        val dataArrayList = arrayListOf<MateriListModel>()
        dataArrayList.addAll(sort)
        setRecyclerViewMateri(dataArrayList)
    }

    private fun setRecyclerViewMateri(data: ArrayList<MateriListModel>) {
        adapter = AdminListMateriAdapter(data, object: OnClickItem.ClickMateri{
            override fun clickItemMateri(materi: MateriListModel, it: View) {
                setClickSetting(materi, it)
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

    private fun setClickSetting(data: MateriListModel, it: View) {
        val popupMenu = PopupMenu(this@AdminMateriActivity, it)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.setOnMenuItemClickListener(object :
            PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                when (menuItem!!.itemId) {
                    R.id.rincian -> {
                        val intent = Intent(this@AdminMateriActivity, AdminMateriDetailActivity::class.java)
                        intent.putExtra("idListMateri", data.idListMateri)
                        intent.putExtra("judul", data.judul)
                        startActivity(intent)
                        return true
                    }
                    R.id.edit -> {
                        setDialogEditMateri(data)
                        return true
                    }
                    R.id.hapus -> {
                        setDialogHapusMateri(data)
                        return true
                    }
                }
                return true
            }

        })
        popupMenu.show()
    }

    private fun setDialogHapusMateri(data: MateriListModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan

        view.apply {
            tvTitleKonfirmasi.text = "Yakin Hapus Materi?. \nData yang berkaitan akan terhapus"

            btnKonfirmasi.setOnClickListener{
                postHapusMateri(data.idListMateri!!)
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
            }
        }
    }

    private fun postHapusMateri(idListMateri: String) {
        viewModel.postHapusMateri(idListMateri)
    }

    private fun getHapusMateri(){
        viewModel.getResponseHapusMateri().observe(this@AdminMateriActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriActivity)
                is UIState.Failure-> setFailureHapusMateri(result.message)
                is UIState.Success-> setSuccessHapusMateri(result.data)
            }
        }
    }

    private fun setSuccessHapusMateri(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@AdminMateriActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                fetchDataMateri()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
            } else{
                Toast.makeText(this@AdminMateriActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMateriActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setFailureHapusMateri(message: String) {
        Toast.makeText(this@AdminMateriActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setDialogEditMateri(data: MateriListModel) {
        val view = AlertDialogMateriBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempAlertDialog = dialogInputan
        tempView = view

        view.apply {
            tVTitle.text = "Update Materi"
            etEditJudulMateri.setText(data.judul)


            etEditImage.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            btnSimpan.setOnClickListener {
                var check = true
                if(etEditJudulMateri.text.isEmpty()){
                    etEditJudulMateri.error = "Tidak Boleh Kosong"
                    check = false
                }

                if(check){
                    tempAlertDialog = dialogInputan
                    Toast.makeText(this@AdminMateriActivity, "update", Toast.LENGTH_SHORT).show()
                    if(
                        etEditImage.text.toString().trim() == resources.getString(R.string.ket_klik_file)
                    ){
                        postUpdateMateriNoImage(
                            data.idListMateri!!,
                            etEditJudulMateri.text.toString().trim(),
                        )
                    }else{
                        postUpdateMateri(
                            data.idListMateri!!,
                            etEditJudulMateri.text.toString().trim(),
                            fileImage!!
                        )
                    }

//                        postUpdateData(
//                            data.idListMateri!!,
//                            etEditJudulMateri.text.toString().trim(),
//                            fileImage!!
//                        )

                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
                tempView = null
                filePdf = null
                fileImage = null
            }
        }
    }

    private fun postUpdateMateri(
        idListMateri: String, judul: String, fileImage: MultipartBody.Part,
    ) {
        viewModel.postUpdatehData(
            post = convertStringToMultipartBody("update_materi"),
            idListMateri = convertStringToMultipartBody(idListMateri),
            judul = convertStringToMultipartBody(judul),
            fileImage = fileImage
        )
    }

    private fun getUpdateMateri(){
        viewModel.getResponseUpdateMateri().observe(this@AdminMateriActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriActivity)
                is UIState.Success-> setSuccessUpdateMateri(result.data)
                is UIState.Failure-> setFailureUpdateMateri(result.message)
            }
        }
    }

    private fun setSuccessUpdateMateri(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminMateriActivity, "Berhasil Update Materi", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                tempView = null
                filePdf = null
                fileImage = null

                fetchDataMateri()
            } else{
                Toast.makeText(this@AdminMateriActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
        loading.alertDialogCancel()
    }

    private fun setFailureUpdateMateri(message: String) {
        Toast.makeText(this@AdminMateriActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun postUpdateMateriNoImage(
        idListMateri: String, judul: String
    ) {
        viewModel.postUpdateDataNoImage(
            idListMateri, judul
        )
    }

    private fun getUpdateMateriNoImage(){
        viewModel.getResponseUpdateMateriNoImage().observe(this@AdminMateriActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriActivity)
                is UIState.Success-> setSuccessUpdateMateriNoImage(result.data)
                is UIState.Failure-> setFailureUpdateMateriNoImage(result.message)
            }
        }
    }

    private fun setSuccessUpdateMateriNoImage(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminMateriActivity, "Berhasil Update Materi", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                tempView = null
                filePdf = null
                fileImage = null

                fetchDataMateri()
            } else{
                Toast.makeText(this@AdminMateriActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setFailureUpdateMateriNoImage(message: String) {
        Toast.makeText(this@AdminMateriActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
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

        tempView = view
        tempAlertDialog = dialogInputan

        view.apply {

            etEditImage.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            btnSimpan.setOnClickListener {

                var check = true
                if(etEditJudulMateri.text.isEmpty()){
                    etEditJudulMateri.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etEditImage.text.toString().trim()==resources.getString(R.string.ket_klik_file)){
                    Toast.makeText(this@AdminMateriActivity, "Upload Image", Toast.LENGTH_SHORT).show()
                    check = false
                }

                if(check){
                    tempAlertDialog = dialogInputan
                    postTambahData(
                        etEditJudulMateri.text.toString().trim(),
                        fileImage!!
                    )
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
                tempAlertDialog = null
                tempView = null
                filePdf = null
                fileImage = null
            }
        }
    }

    private fun postTambahData(
        judul: String, fileImage: MultipartBody.Part
    ) {
        viewModel.postTambahData(
            post = convertStringToMultipartBody("tambah_materi"),
            judul = convertStringToMultipartBody(judul),
            fileImage = fileImage
        )
    }

    private fun getPostTambahData(){
        viewModel.getResponseTambahMateri().observe(this@AdminMateriActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriActivity)
                is UIState.Failure-> setFailureTambahData(result.message)
                is UIState.Success-> setSuccessTambahData(result.data)
            }
        }
    }

    private fun setSuccessTambahData(data: java.util.ArrayList<ResponseModel>) {
        loading.alertDialogCancel()

        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminMateriActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                tempAlertDialog!!.dismiss()
                tempAlertDialog = null
                tempView = null
                filePdf = null
                fileImage = null
                fetchDataMateri()
            } else{
                Toast.makeText(this@AdminMateriActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMateriActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureTambahData(message: String) {
        Toast.makeText(this@AdminMateriActivity, message, Toast.LENGTH_SHORT).show()
        Log.d(TAG, "setFailureTambahData: ${message}")
        loading.alertDialogCancel()
    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()) {
                startActivity(Intent(this, AdminMateriActivity::class.java))
            } else { //request for the permission
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        } else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                Constant.STORAGE_PERMISSION_CODE
            )
        }
    }


    private fun pickImageFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }

        startActivityForResult(intent, Constant.IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constant.IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Mendapatkan URI file PDF yang dipilih
            val fileUri = data.data!!

            val nameImage = getNameFile(fileUri)

            tempView?.let {
                it.etEditImage.text = nameImage
            }

            // Mengirim file PDF ke website menggunakan Retrofit
            fileImage = uploadImageToStorage(fileUri, nameImage, "materi_image")
        }
    }

    private fun getNameFile(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor?.moveToFirst()
        val name = cursor?.getString(nameIndex!!)
        cursor?.close()
        return name!!
    }

    @SuppressLint("Recycle")
    private fun uploadImageToStorage(pdfUri: Uri?, pdfFileName: String, nameAPI:String):MultipartBody.Part? {
        var pdfPart : MultipartBody.Part? = null
        pdfUri?.let {
            val file = contentResolver.openInputStream(pdfUri)?.readBytes()

            if (file != null) {
//                // Membuat objek RequestBody dari file PDF
//                val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
//                // Membuat objek MultipartBody.Part untuk file PDF
//                pdfPart = MultipartBody.Part.createFormData("materi_pdf", pdfFileName, requestFile)

                pdfPart = convertFileToMultipartBody(file, pdfFileName, nameAPI)
            }
        }
        return pdfPart
    }

    private fun convertFileToMultipartBody(file: ByteArray, pdfFileName: String, nameAPI:String):MultipartBody.Part?{
        val requestFile = file.toRequestBody("application/pdf".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(nameAPI, pdfFileName, requestFile)

        return filePart
    }

    private fun checkPermission(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun convertStringToMultipartBody(data: String): RequestBody{
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), data)
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