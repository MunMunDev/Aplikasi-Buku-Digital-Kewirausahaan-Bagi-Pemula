package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi

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
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.AdminMateriAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.main.AdminMainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminMateriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMateriBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var adapter: AdminMateriAdapter
    private val viewModel: AdminMateriViewModel by viewModels()
    @Inject lateinit var loading: LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading.alertDialogLoading(this@AdminMateriActivity)
        setKontrolNavigationDrawer()
        setButton()
        fetchDataMateri()
        getDataMateri()
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                dialogTambahData()
            }
        }
    }

    private fun dialogTambahData() {

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
                is UIState.Loading-> {}
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
                if(etEditNamaMateri.text.isEmpty()){
                    etEditNamaMateri.error = "Tidak Boleh Kosong"
                } else{
                    loading.alertDialogLoading(this@AdminMateriActivity)

                }
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
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