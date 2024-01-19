package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.akun

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.AdminUsersAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminSemuaAkunBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogAkunBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminSemuaAkunActivity : AppCompatActivity() {
    private val TAG = "AdminSemuAkunActivityTAG"
    private lateinit var binding: ActivityAdminSemuaAkunBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    @Inject lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminUsersAdapter
    private val viewModel: AdminSemuaAkunViewModel by viewModels()
    private var tempAlertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSemuaAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawerSetting()
        button()
        fetchDataUsers()
        getDataUsers()
        getTambahData()
    }

    private fun button() {
        binding.btnTambah.setOnClickListener{
            dialogTambahData()
        }
    }

    private fun setKontrolNavigationDrawerSetting() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminSemuaAkunActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminSemuaAkunActivity)
        }
    }

    fun fetchDataUsers() {
        viewModel.fetchDataUsers()
    }

    private fun getDataUsers() {
        viewModel.getDataUsers().observe(this@AdminSemuaAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminSemuaAkunActivity)
                is UIState.Success-> setDataSuccess(result.data)
                is UIState.Failure-> setDataFailure(result.message)
            }
        }
    }

    private fun setDataSuccess(data: ArrayList<UsersModel>) {
        if(data.isNotEmpty()){
            val tempData = arrayListOf<UsersModel>()
            val sortedDataUser = data.sortedWith(
                compareBy {
                    it.idUser
                }
            )

            for (value in sortedDataUser){
                tempData.add(value)
            }

            adapter = AdminUsersAdapter(data, object: AdminUsersAdapter.ClickItemListener{
                override fun onClickItem(user: UsersModel, it: View, position:Int) {
                    val i = Intent(this@AdminSemuaAkunActivity, AdminDetailAkunActivity::class.java)
                    i.putExtra("data", user)
                    startActivity(i)
                    finish()
                }
            })

            binding.rvAkun.layoutManager = LinearLayoutManager(this@AdminSemuaAkunActivity, LinearLayoutManager.VERTICAL, false)
            binding.rvAkun.adapter = adapter
        } else{
            Toast.makeText(this@AdminSemuaAkunActivity, "Ada kesalahan", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setDataFailure(message: String) {
        Toast.makeText(this@AdminSemuaAkunActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun dialogTambahData(){
        val view = AlertDialogAkunBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminSemuaAkunActivity)
        alertDialog.setView(view.root)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tVTitle.text = "Tambah User"
            btnSimpan.setOnClickListener {
                var cek = false
                if (etEditNama.text.toString().isEmpty()) {
                    etEditNama.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditNomorHp.text.toString().isEmpty()) {
                    etEditNomorHp.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditUsername.text.toString().isEmpty()) {
                    etEditUsername.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditPassword.text.toString().isEmpty()) {
                    etEditPassword.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    tempAlertDialog = dialogInputan
                    postTambahData(
                        etEditNama.text.toString().trim(),
                        etEditNomorHp.text.toString(),
                        etEditUsername.text.toString().trim(),
                        etEditPassword.text.toString().trim()
                    )
                }
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahData(nama: String, nomorHp: String, username: String, password: String){
        viewModel.postTambahUser(nama, nomorHp, username, password, "user")
    }

    private fun getTambahData(){
        viewModel.getTambahData().observe(this@AdminSemuaAkunActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminSemuaAkunActivity)
                is UIState.Success-> setTambahDataSuccess(result.data)
                is UIState.Failure-> setTambahDataFailure(result.message)
            }
        }
    }

    private fun setTambahDataSuccess(responseModel: ArrayList<ResponseModel>) {
        if(responseModel.isNotEmpty()){
            if(responseModel[0].status=="0"){
                Toast.makeText(this@AdminSemuaAkunActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                fetchDataUsers()
                tempAlertDialog!!.dismiss()
            } else{
                Toast.makeText(this@AdminSemuaAkunActivity, responseModel[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminSemuaAkunActivity, "Gagal Tambah Data", Toast.LENGTH_SHORT).show()
        }
        tempAlertDialog = null
        loading.alertDialogCancel()
    }


    private fun setTambahDataFailure(message: String) {
        Toast.makeText(this@AdminSemuaAkunActivity, message, Toast.LENGTH_SHORT).show()
        Log.d(TAG, "error: $message")
        loading.alertDialogCancel()
    }

    override fun onBackPressed() {
        startActivity(Intent(this@AdminSemuaAkunActivity, AdminMainActivity::class.java))
        finish()
        super.onBackPressed()
    }
}