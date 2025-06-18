package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.akun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAkunBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogAkunBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.SharedPreferencesLogin
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AkunActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAkunBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferences: SharedPreferencesLogin
    private val viewModel: AkunViewModel by viewModels()
    @Inject lateinit var loading: LoadingAlertDialog
    private var tempUser: UsersModel = UsersModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferences()
        setKontrolNavigationDrawer()
        setData()
        button()
        getPostUpdateData()
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(this@AkunActivity)
    }

    private fun setKontrolNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AkunActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AkunActivity)
        }
    }

    private fun setData(){
        binding.apply {
            etNama.text = sharedPreferences.getNama()
            etNomorHp.text = sharedPreferences.getNomorHp()
            etUsername.text = sharedPreferences.getUsername()
            etPassword.text = sharedPreferences.getPassword()
        }
    }

    private fun button() {
        binding.btnUbahData.setOnClickListener {
            setDialogUpdateData()
        }
    }

    private fun setDialogUpdateData() {
        val view = AlertDialogAkunBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@AkunActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            etEditNama.setText(sharedPreferences.getNama())
            etEditNomorHp.setText(sharedPreferences.getNomorHp())
            etEditUsername.setText(sharedPreferences.getUsername())
            etEditPassword.setText(sharedPreferences.getPassword())

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
                    tempUser = UsersModel(
                        sharedPreferences.getIdUser().toString(),
                        etEditNama.text.toString(),
                        etEditNomorHp.text.toString(),
                        etEditUsername.text.toString(),
                        etEditPassword.text.toString(),
                        sharedPreferences.getUsername()
                    )
                    postUpdateData(
                        sharedPreferences.getIdUser().toString(),
                        etEditNama.text.toString(),
                        etEditNomorHp.text.toString(),
                        etEditUsername.text.toString(),
                        etEditPassword.text.toString(),
                        sharedPreferences.getUsername()
                    )

                    dialogInputan.dismiss()
                }

            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateData(idUser: String, nama: String, nomorHp: String, username: String, password: String, usernameLama: String) {
        Log.d("AkunActivityTAG", "postUpdateData: $idUser, $nama, $nomorHp, $username, $password, $usernameLama")
        viewModel.postUpdateUser(idUser, nama, nomorHp, username, password, usernameLama)
    }

    private fun getPostUpdateData(){
        viewModel.getUpdateData().observe(this@AkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AkunActivity)
                is UIState.Success-> setSuccessUpdateData(result.data)
                is UIState.Failure-> setFailureUpdateData(result.message)
            }
        }
    }

    private fun setFailureUpdateData(message: String) {
        Toast.makeText(this@AkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@AkunActivity, "Berhasil Update Akun", Toast.LENGTH_SHORT).show()
                sharedPreferences.setLogin(
                    tempUser.idUser!!.trim().toInt(),
                    tempUser.nama!!,
                    tempUser.nomorHp!!,
                    tempUser.username!!,
                    tempUser.password!!,
                    "user"
                )
                tempUser = UsersModel()
                setData()
            } else{
                Toast.makeText(this@AkunActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AkunActivity, "Gagal: Ada kesalahan di API", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }
}