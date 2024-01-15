package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.AdminMateriAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.Constant
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

    private val TAG = "AdminMateriActivity"
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
            var file: MultipartBody.Part? = null
            etEditFile.setOnClickListener {
                file = uploadPdf()

                if(checkPermission()){
                    Log.d(TAG, "dialogTambahData: ${Environment.getExternalStorageDirectory()}")
                    try {
//                        val file2 = File("${Environment.getExternalStorageDirectory()}")
//                        file2.outputStream()
//                        RequestBody.create(MediaType.parse("image/*"), file2)
                        val inte = Intent(Intent.ACTION_GET_CONTENT).also {
                            it.type = "application/pdf"
                            val mineTypes = arrayOf("application/pdf")
                            it.putExtra(Intent.EXTRA_MIME_TYPES, mineTypes)
                            startActivityForResult(it, Constant.STORAGE_PERMISSION_CODE)
                        }
                        Log.d(TAG, "result: ${inte.data}")
                    }catch (ex: Exception){
                        Log.d(TAG, "error: ${ex.message}")
                    }
//                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//                    var body = MultipartBody.Part.createFormData("gambar", file.name, requestFile)
                } else{
                    requestPermission()
                }
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
                if(file==null){
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
                        file!!,
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
        var body: MultipartBody.Part? = null
        if(checkPermission()){
            Toast.makeText(this@AdminMateriActivity, "berhasil", Toast.LENGTH_SHORT).show()
            val file = File("${Environment.getExternalStorageDirectory()}")
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            body = MultipartBody.Part.createFormData("gambar", file.name, requestFile)
        } else{
            requestPermission()
        }

//        val file = File("/download")
//        val requestFile = RequestBody.create(MediaType.parse("image/jpg"), file)
//        var body = MultipartBody.Part.createFormData("files[0]", file.name, requestFile)
//
//        Toast.makeText(this@AdminMateriActivity, "hai", Toast.LENGTH_SHORT).show()

        return body!!
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constant.STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty()){
                //check each permission if granted or not
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (write && read){
                    //External Storage Permission granted
                    Log.d(TAG, "onRequestPermissionsResult: External Storage Permission granted")
                    Toast.makeText(this@AdminMateriActivity, "reques granted......", Toast.LENGTH_SHORT).show()
                }
                else{
                    //External Storage Permission denied...
                    Log.d(TAG, "onRequestPermissionsResult: External Storage Permission denied...")
                    Toast.makeText(this@AdminMateriActivity, "External Storage Permission denied...", Toast.LENGTH_SHORT).show()
                    requestPermission()
                }
            }
        }
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
    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        Log.d(TAG, "storageActivityResultLauncher: ")
        //here we will handle the result of our intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            if (Environment.isExternalStorageManager()){
                //Manage External Storage Permission is granted
                Log.d(TAG, "storageActivityResultLauncher: Manage External Storage Permission is granted")
                Toast.makeText(this@AdminMateriActivity, "granted......", Toast.LENGTH_SHORT).show()
            }
            else{
                //Manage External Storage Permission is denied....
                Log.d(TAG, "storageActivityResultLauncher: Manage External Storage Permission is denied....")
                Toast.makeText(this@AdminMateriActivity, "Manage External Storage Permission is denied....", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            //Android is below 11(R)
        }
    }

    private fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            try {
                Log.d(TAG, "requestPermission: try")
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", this.packageName, null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)
            }
            catch (e: Exception){
                Log.e(TAG, "requestPermission: ", e)
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        }
        else{
            //Android is below 11(R)
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                Constant.STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }


    private fun postTambahData(namaMateri: String, namaPenulis: String, file: MultipartBody.Part, jumlahPelihat: String) {
        viewModel.postTambahData(
            namaMateri = convertStringToMultipartBody(namaMateri),
            namaPenulis = convertStringToMultipartBody(namaPenulis),
            file = file,
            lokasiFile = convertStringToMultipartBody(Constant.MATERI_URL),
            urlMateri = convertStringToMultipartBody(""),
            urlImage = convertStringToMultipartBody(""),
            jumlahPelihat = convertStringToMultipartBody(jumlahPelihat),

        )
    }

    private fun convertStringToMultipartBody(data: String): RequestBody{
        return RequestBody.create(MediaType.parse("multipart/form-data"), data)
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