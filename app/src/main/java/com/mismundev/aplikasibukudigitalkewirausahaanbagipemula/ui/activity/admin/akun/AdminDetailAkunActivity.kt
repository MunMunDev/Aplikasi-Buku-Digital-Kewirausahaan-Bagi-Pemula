package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.akun

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminDetailAkunBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogAkunBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogKonfirmasiBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminDetailAkunActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDetailAkunBinding
    private lateinit var user: UsersModel
    @Inject lateinit var loading: LoadingAlertDialog
    private val viewModel: AdminSemuaAkunViewModel by viewModels()
    private var tempAlertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        setDataSebelumnya()
        getPostUpdateData()
        getPostHapusUser()
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                startActivity(Intent(this@AdminDetailAkunActivity, AdminSemuaAkunActivity::class.java))
                finish()
            }
            btnUbahData.setOnClickListener {
                dialogUbahData()
            }
            btnHapusData.setOnClickListener {
                dialogHapusUser(user.idUser!!, user.nama!!)
            }
        }
    }

    private fun setDataSebelumnya() {
        user = UsersModel()
        try{
            val intent = intent.getParcelableExtra<UsersModel>("data")
            user = intent!!
            setData(user)
        } catch (ex: Exception){
            Toast.makeText(this@AdminDetailAkunActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setData(user: UsersModel) {
        binding.apply {
            etNama.text = user.nama
            etNomorHp.text = user.nomorHp
            etUsername.text = user.username
            etPassword.text = user.password
        }
    }

    private fun dialogUbahData() {
        val view = AlertDialogAkunBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminDetailAkunActivity)
        alertDialog.setView(view.root)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply{
            etEditNama.setText(user.nama)
            etEditNomorHp.setText(user.nomorHp)
            etEditUsername.setText(user.username)
            etEditPassword.setText(user.password)

            btnSimpan.setOnClickListener {
                var cek = false
                if(etEditNama.toString().isEmpty()){
                    etEditNama.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etEditNomorHp.toString().isEmpty()){
                    etEditNomorHp.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etEditUsername.toString().isEmpty()){
                    etEditUsername.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if(etEditPassword.toString().isEmpty()){
                    etEditPassword.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if(!cek){
                    tempAlertDialog = dialogInputan
                    val idUser = user.idUser
                    user = UsersModel(
                        idUser = idUser,
                        nama = etEditNama.text.toString(),
                        nomorHp = etEditNomorHp.text.toString(),
                        username = etEditUsername.text.toString(),
                        password = etEditPassword.text.toString(),
                        sebagai = "user"
                    )
                    postUpdateData(
                        user.idUser!!,
                        etEditNama.text.toString(),
                        etEditNomorHp.text.toString(),
                        etEditUsername.text.toString(),
                        etEditPassword.text.toString(),
                        user.username!!,
                    )
                }

            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateData(idUser:String, nama: String, nomorHp: String, username: String, password: String, usernameLama:String){
        viewModel.postUpdateUser(idUser, nama, nomorHp, username, password, usernameLama)
    }

    private fun getPostUpdateData(){
        viewModel.getUpdateData().observe(this@AdminDetailAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminDetailAkunActivity)
                is UIState.Success-> setSuccessUpdateData(result.data)
                is UIState.Failure-> setFailureUpdateData()
            }
        }
    }

    private fun setFailureUpdateData() {
        Toast.makeText(this@AdminDetailAkunActivity, "Gagal", Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(responseModel: ArrayList<ResponseModel>) {
        if(responseModel.isNotEmpty()){
            if(responseModel[0].status == "0"){
                Toast.makeText(this@AdminDetailAkunActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                setData(user)
                tempAlertDialog!!.dismiss()
            } else{
                Toast.makeText(this@AdminDetailAkunActivity, "Gagal", Toast.LENGTH_SHORT).show()
            }
        }
        tempAlertDialog = null
        loading.alertDialogCancel()
    }

    private fun dialogHapusUser(idUser: String, namaUser:String){
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminDetailAkunActivity)
        alertDialog.setView(view.root)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply{
            tvTitleKonfirmasi.text = "Hapus $namaUser?"
            tvBodyKonfirmasi.text =
                "Menghapus Data User Akan menghapus menghilangkan Akun Ini. Yakin Hapus?"

            btnKonfirmasi.setOnClickListener {
                dialogInputan.dismiss()
                postHapusUser(idUser)
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusUser(idUser: String){
        viewModel.postHapusUser(idUser)
    }

    private fun getPostHapusUser(){
        viewModel.getHapusUser().observe(this@AdminDetailAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminDetailAkunActivity)
                is UIState.Success-> setDataSuccessHapusUser(result.data)
                is UIState.Failure-> setDataFailureHapusUser(result.message)
            }
        }
    }

    private fun setDataSuccessHapusUser(data: ArrayList<ResponseModel>) {
        if(data[0].status == "0"){
            Toast.makeText(this@AdminDetailAkunActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@AdminDetailAkunActivity, AdminSemuaAkunActivity::class.java))
            finish()
        } else{
            Toast.makeText(this@AdminDetailAkunActivity, "Gagal hapus", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setDataFailureHapusUser(message: String) {
        Toast.makeText(this@AdminDetailAkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AdminDetailAkunActivity, AdminSemuaAkunActivity::class.java))
        finish()
    }

}