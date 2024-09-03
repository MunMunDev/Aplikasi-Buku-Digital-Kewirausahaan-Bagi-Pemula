package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.gambar

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.admin.AdminListMateriGambarAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriGambarModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminMateriGambarBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogAdminGambarBabBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogKeteranganBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogKonfirmasiBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogShowImageBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.Constant
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.TanggalDanWaktu
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@AndroidEntryPoint
class AdminMateriGambarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMateriGambarBinding
    private lateinit var adapter: AdminListMateriGambarAdapter
    private var idMateri = ""

    private var STORAGE_PERMISSION_CODE = 10
    private var IMAGE_CODE = 10

    private val viewModel: AdminMateriGambarViewModel by viewModels()

    private var tempView: AlertDialogAdminGambarBabBinding? = null
    private var fileImage: MultipartBody.Part? = null
    private var tanggalDanWaktu = TanggalDanWaktu()

    private var image: String? = null
    private var deskripsi: String? = null
    private var nameImage: String? = null
    private var waktu: String? = null

    @Inject lateinit var loading: LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMateriGambarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setButton()
        getData()
        getTambahMateriGambar()
        getUpdateMateriGambar()
        getUpdateMateriGambarNoImage()
        getDeleteMateriGambar()
    }

    private fun setDataSebelumnya() {
        val bundle = intent.extras
        if(bundle != null){
            idMateri = bundle.getString("idMateri")!!
            val judul = bundle.getString("judul")!!
            binding.title.text = judul
            fetchData(idMateri)
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambahData.setOnClickListener{
                showDialogTambahData()
            }
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun showDialogTambahData() {
        val view = AlertDialogAdminGambarBabBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriGambarActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view

        view.apply {
            etGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            btnSimpan.setOnClickListener{
                var check = true
                if(etKeterangan.text.toString().isEmpty()){
                    etKeterangan.error = "Tidak Boleh Kosong"
                    check = false
                }
                if(etGambar.text.toString().trim() == resources.getString(R.string.ket_klik_file)){
                    etGambar.error = "Masukkan Data"
                    check = false
                }

                if(check){
                    deskripsi = etKeterangan.text.toString()
                    waktu = "${tanggalDanWaktu.tanggalSekarangZonaMakassar()}-${tanggalDanWaktu.waktuSekarangZonaMakassar()}"
                    nameImage = "materi-$idMateri-$waktu"

                    val post = convertStringToMultipartBody("post_gambar_chat")
                    postTambahMateriGambar(post, convertStringToMultipartBody(idMateri), fileImage!!, convertStringToMultipartBody(deskripsi!!))
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahMateriGambar(
        post: RequestBody, idMateri: RequestBody, fileImage: MultipartBody.Part, deskripsi: RequestBody
    ) {
        viewModel.postTambahMateriGambar(
            post, idMateri, fileImage, deskripsi
        )
    }

    private fun getTambahMateriGambar(){
        viewModel.getTambahMateriGambar().observe(this@AdminMateriGambarActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriGambarActivity)
                is UIState.Success-> setSuccessTambahMateriGambar(result.data)
                is UIState.Failure-> setFailureTambahMateriGambar(result.message)
            }
        }
    }

    private fun setFailureTambahMateriGambar(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminMateriGambarActivity, message, Toast.LENGTH_SHORT).show()
        Log.d("DetailTAG", "error: $message")
    }

    private fun setSuccessTambahMateriGambar(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@AdminMateriGambarActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                fetchData(idMateri)
            } else{
                Toast.makeText(this@AdminMateriGambarActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMateriGambarActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }


    private fun fetchData(idMateri: String) {
        viewModel.fetchMateriGambar(idMateri)
    }

    private fun getData(){
        viewModel.getMateriGambar().observe(this@AdminMateriGambarActivity){result->
            when(result){
                is UIState.Loading->{}
                is UIState.Success-> setSuccessFetchData(result.data)
                is UIState.Failure-> setFailureFetchData(result.message)
            }
        }
    }

    private fun setFailureFetchData(message: String) {
        Toast.makeText(this@AdminMateriGambarActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessFetchData(data: ArrayList<MateriGambarModel>) {
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminMateriGambarActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(list: ArrayList<MateriGambarModel>) {
        adapter = AdminListMateriGambarAdapter(list, object: OnClickItem.ClickAdminMateriGambar{
            override fun clickItemSetting(materi: MateriGambarModel, it: View) {
                val popupMenu = PopupMenu(this@AdminMateriGambarActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                showDialogEditMateri(materi)
                                return true
                            }
                            R.id.hapus -> {
                                showDialogHapusMateri(materi)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

            override fun clickItemKeterangan(keterangan: String, it: View) {
                showClickText(keterangan, "Judul Gambar")
            }

            override fun clickItemGambar(gambar: String, keterangan:String, it: View) {
                setShowImage(gambar, keterangan)
            }

        })
        binding.apply {
            rvMateri.layoutManager = LinearLayoutManager(
                this@AdminMateriGambarActivity, LinearLayoutManager.VERTICAL, false
            )
            rvMateri.adapter = adapter
        }
    }

    private fun showDialogEditMateri(materi: MateriGambarModel) {
        val view = AlertDialogAdminGambarBabBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriGambarActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tempView = view

        view.apply {
            etKeterangan.setText(materi.deskripsi)

            etGambar.setOnClickListener {
                if(checkPermission()){
                    pickImageFile()
                } else{
                    requestPermission()
                }
            }

            btnSimpan.setOnClickListener{
                var check = true
                if(etKeterangan.text.toString().isEmpty()){
                    etKeterangan.error = "Tidak Boleh Kosong"
                    check = false
                }

                if(check){
                    deskripsi = etKeterangan.text.toString()

                    if(etGambar.text.toString().trim() == resources.getString(R.string.ket_klik_file)){
                        postUpdateMateriGambarNoImage(materi.id_materi_gambar!!, deskripsi!!)
                    } else{
                        postUpdateMateriGambar(
                            convertStringToMultipartBody("post"), convertStringToMultipartBody(materi.id_materi_gambar!!),
                            fileImage, convertStringToMultipartBody(deskripsi!!)
                        )
                    }
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateMateriGambar(
        post: RequestBody, idMateriGambar: RequestBody, fileImage: MultipartBody.Part?, deskripsi: RequestBody
    ) {
        viewModel.postUpdateMateriGambar(
            post, idMateriGambar, fileImage!!, deskripsi
        )
    }

    private fun getUpdateMateriGambar(){
        viewModel.getUpdateMateriGambar().observe(this@AdminMateriGambarActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriGambarActivity)
                is UIState.Success-> setSuccessUpdateGambar(result.data)
                is UIState.Failure-> setFailureUpdateGambar(result.message)
            }
        }
    }

    private fun setFailureUpdateGambar(message: String) {
        Toast.makeText(this@AdminMateriGambarActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateGambar(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                fetchData(idMateri)
            } else{
                Toast.makeText(this@AdminMateriGambarActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMateriGambarActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun postUpdateMateriGambarNoImage(
        idMateriGambar: String, deskripsi: String
    ) {
        viewModel.postUpdateMateriGambarNoImage(idMateriGambar, deskripsi)
    }

    private fun getUpdateMateriGambarNoImage(){
        viewModel.getUpdateMateriGambarNoImage().observe(this@AdminMateriGambarActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriGambarActivity)
                is UIState.Success-> setSuccessUpdateGambarNoImage(result.data)
                is UIState.Failure-> setFailureUpdateGambarNoImage(result.message)
            }
        }
    }

    private fun setFailureUpdateGambarNoImage(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminMateriGambarActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessUpdateGambarNoImage(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].response == "0"){
                fetchData(idMateri)
            } else{
                Toast.makeText(this@AdminMateriGambarActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMateriGambarActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showDialogHapusMateri(materi: MateriGambarModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriGambarActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus Data Ini?"
            tvBodyKonfirmasi.text = "Setelah menghapus, data tidak dapat dikembalikan. "

            btnKonfirmasi.setOnClickListener{
                binding.apply {
                    val id = materi.id_materi!!
                    postDeleteMateriGambar(id)
                    dialogInputan.dismiss()
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postDeleteMateriGambar(id:String) {
        viewModel.postHapusMateriGambar(id)
    }

    private fun getDeleteMateriGambar(){
        viewModel.getHapusMateriGambar().observe(this@AdminMateriGambarActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriGambarActivity)
                is UIState.Success-> setSuccessDeleteMateriGambar(result.data)
                is UIState.Failure-> setFailureDeleteMateriGambar(result.message)
            }
        }
    }

    private fun setFailureDeleteMateriGambar(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminMateriGambarActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessDeleteMateriGambar(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@AdminMateriGambarActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                fetchData(idMateri)
            } else{
                Toast.makeText(this@AdminMateriGambarActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMateriGambarActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showClickText(deskripsi:String, judul: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriGambarActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = deskripsi
            tvBodyKeterangan.text = judul
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowImage(gambar: String, title:String) {
        val view = AlertDialogShowImageBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriGambarActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitle.text = "$title"
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }

        Glide.with(this@AdminMateriGambarActivity)
            .load("${Constant.BASE_URL}${Constant.GAMBAR_URL}/$gambar") // URL Gambar
            .error(R.drawable.gambar_error_image)
            .into(view.ivShowImage) // imageView mana yang akan diterapkan

    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()) {
                startActivity(Intent(this@AdminMateriGambarActivity, AdminMateriGambarActivity::class.java))
            } else { //request for the permission
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        } else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    private fun pickImageFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
        }

        startActivityForResult(intent, IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Mendapatkan URI file PDF yang dipilih
            val fileUri = data.data!!

            val nameImage = getNameFile(fileUri)
            Log.d("DetailTAG", "data: $nameImage")

            tempView?.let {
                it.etGambar.text = nameImage
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
    private fun uploadImageToStorage(pdfUri: Uri?, pdfFileName: String, nameAPI:String): MultipartBody.Part? {
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

    private fun convertFileToMultipartBody(file: ByteArray, pdfFileName: String, nameAPI:String): MultipartBody.Part?{
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

    private fun convertStringToMultipartBody(data: String): RequestBody {
        return RequestBody.create("multipart/form-data".toMediaTypeOrNull(), data)
    }
}