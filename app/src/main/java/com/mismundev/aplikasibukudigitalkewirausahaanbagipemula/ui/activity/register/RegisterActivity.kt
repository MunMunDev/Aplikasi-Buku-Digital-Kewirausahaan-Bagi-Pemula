package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityRegisterBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.login.LoginActivity
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    @Inject lateinit var loading: LoadingAlertDialog
    private val viewModel : RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        postRegisterUser()
    }

    private fun setButton() {
        binding.apply {
            tvLogin.setOnClickListener{
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
            btnRegistrasi.setOnClickListener {
                buttonRegistrasi()
            }
        }
    }

    private fun buttonRegistrasi() {
        binding.apply {
            var cek = false
            if (etEditNama.toString().isEmpty()) {
                etEditNama.error = "Tidak Boleh Kosong"
                cek = true
            }
            if (etEditNomorHp.toString().isEmpty()) {
                etEditNomorHp.error = "Tidak Boleh Kosong"
                cek = true
            }
            if (etEditUsername.toString().isEmpty()) {
                etEditUsername.error = "Tidak Boleh Kosong"
                cek = true
            }
            if (etEditPassword.toString().isEmpty()) {
                etEditPassword.error = "Tidak Boleh Kosong"
                cek = true
            }

            if (!cek) {
                postTambahData(
                    etEditNama.text.toString().trim(),
                    etEditNomorHp.text.toString().trim(),
                    etEditUsername.text.toString().trim(),
                    etEditPassword.text.toString().trim()
                )
            }
        }
    }

    private fun postTambahData(nama: String, nomorHp: String, username: String, password: String){
        viewModel.postRegisterUser(nama, nomorHp, username, password, "user")
    }

    private fun postRegisterUser(){
        viewModel.getRegisterUser().observe(this@RegisterActivity){ result ->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@RegisterActivity)
                is UIState.Failure-> responseFailureRegisterUser(result.message)
                is UIState.Success-> responseSuccessRegiserUser(result.data)
            }
        }
    }

    private fun responseFailureRegisterUser(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun responseSuccessRegiserUser(data: ArrayList<ResponseModel>) {
        if (data.isNotEmpty()){
            if (data[0].status == "0"){
                Toast.makeText(this@RegisterActivity, "Berhasil melakukan registrasi", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(this@RegisterActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this@RegisterActivity, "Maaf gagal", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }
}