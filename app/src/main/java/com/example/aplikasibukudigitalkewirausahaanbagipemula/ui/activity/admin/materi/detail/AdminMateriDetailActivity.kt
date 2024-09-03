package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.admin.AdminMateriAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityAdminMateriDetailBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogAdminMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogKeteranganBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.AlertDialogKonfirmasiBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.gambar.AdminMateriGambarActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.main.AdminMateriViewModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class AdminMateriDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMateriDetailBinding
    private val viewModel: AdminMateriDetailViewModel by viewModels()
    private lateinit var adapter: AdminMateriAdapter
    @Inject
    lateinit var loading: LoadingAlertDialog
    private var idListMateri = "1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMateriDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setButton()
        fetchData()
        getMateri()
        getTambahMateri()
        getUpdateMateri()
        getHapusMateri()
    }

    private fun setDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            idListMateri = extras.getString("idListMateri")!!
            val judul = extras.getString("judul")!!
            binding.titleHeader.text = judul
        }
    }

    private fun setButton() {
        binding.apply {
            btnTambah.setOnClickListener {
                setShowDialogTambah()
            }
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setShowDialogTambah() {
        val view = AlertDialogAdminMateriBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {

            val tambahDialogHalaman = binding.titleHeader.text.toString().trim()
            val listTambahDialog : ArrayList<String> = arrayListOf()

            listTambahDialog.add(tambahDialogHalaman)

            btnSimpan.setOnClickListener {
                val judul = etEditJudul.text.toString().trim()
                val deskripsi = etEditDeskripsi.text.toString().trim()

                Log.d("DetailTAG", "id: $idListMateri, judul: $judul, deskripsi: $deskripsi")
                postTambahMateri(
                    idListMateri, deskripsi, judul
                )
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postTambahMateri(
        idListMateri: String, judul: String, deskripsi: String
    ) {
        viewModel.postTambahMateri(
            idListMateri, judul, deskripsi
        )
    }

    private fun getTambahMateri(){
        viewModel.getTambahMateri().observe(this@AdminMateriDetailActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriDetailActivity)
                is UIState.Failure-> setFailureTambahMateri(result.message)
                is UIState.Success-> setSuccessTambahMateri(result.data)
            }
        }
    }

    private fun setSuccessTambahMateri(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminMateriDetailActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                fetchData()
            } else{
                Toast.makeText(this@AdminMateriDetailActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMateriDetailActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureTambahMateri(message: String) {
        Toast.makeText(this@AdminMateriDetailActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun fetchData() {
        viewModel.fetchMateri(idListMateri)
    }

    private fun getMateri(){
        viewModel.getTentangStrokeList().observe(this@AdminMateriDetailActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureFetchMateri(result.message)
                is UIState.Success-> setSuccessFetchMateri(result.data)
            }
        }
    }

    private fun setFailureFetchMateri(message: String) {
        Toast.makeText(this@AdminMateriDetailActivity, message, Toast.LENGTH_SHORT).show()
        setStopShimmer()
    }

    private fun setSuccessFetchMateri(data: ArrayList<MateriModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@AdminMateriDetailActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<MateriModel>) {
        adapter = AdminMateriAdapter(data, object: OnClickItem.ClickAdminMateri{
            override fun clickItemHalaman(halaman: String, it: View) {
                showClickHalaman(halaman)
            }

            override fun clickItemJudul(judul: String, it: View) {
                showClickJudul(judul)
            }

            override fun clickItemDeskripsi(deskripsi: String, it: View) {
                showClickDeskripsi(deskripsi)
            }

            override fun clickItemGambar(idMateri: String, judul: String, it: View) {
                val i = Intent(this@AdminMateriDetailActivity, AdminMateriGambarActivity::class.java)
                i.putExtra("idMateri", idMateri)
                i.putExtra("judul", judul)
                startActivity(i)
            }

            override fun clickItemSetting(materi: MateriModel, it: View) {
                val popupMenu = PopupMenu(this@AdminMateriDetailActivity, it)
                popupMenu.inflate(R.menu.popup_edit_hapus)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                setShowDialogEdit(materi)
                                return true
                            }
                            R.id.hapus -> {
                                setShowDialogHapus(materi)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }
        })

        binding.apply {
            rvValTentangStroke.layoutManager = LinearLayoutManager(
                this@AdminMateriDetailActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvValTentangStroke.adapter = adapter
        }

    }

    private fun showClickHalaman(halaman: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Jenis Halaman"
            tvBodyKeterangan.text = halaman
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun showClickJudul(judul: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Judul Isi"
            tvBodyKeterangan.text = judul
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun showClickDeskripsi(deskripsi: String) {
        val view = AlertDialogKeteranganBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKeterangan.text = "Isi Penjelasan"
            tvBodyKeterangan.text = deskripsi
            btnClose.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setShowDialogEdit(tentangStroke: MateriModel) {
        val view = AlertDialogAdminMateriBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.tVTitle.text = "Edit Value Tentang Stroke"

        view.apply {
            etEditJudul.setText(tentangStroke.judul)
            etEditDeskripsi.setText(tentangStroke.deskripsi)

            btnSimpan.setOnClickListener {
                val idMateri = tentangStroke.idMateri!!
                val judul = etEditJudul.text.toString().trim()
                val deskripsi = etEditDeskripsi.text.toString().trim()

                postUpdateMateri(
                    idMateri, idListMateri, judul, deskripsi
                )
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateMateri(
        idMateri: String, idListMateri: String, judul: String, deskripsi: String,
    ) {
        viewModel.postUpdateMateri(
            idMateri, idListMateri, judul, deskripsi
        )
    }

    private fun getUpdateMateri(){
        viewModel.getUpdateMateri().observe(this@AdminMateriDetailActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriDetailActivity)
                is UIState.Failure-> setFailureUpdateMateri(result.message)
                is UIState.Success-> setSuccessUpdateMateri(result.data)
            }
        }
    }

    private fun setSuccessUpdateMateri(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminMateriDetailActivity, "Berhasil Update", Toast.LENGTH_SHORT).show()
                fetchData()
            } else{
                Toast.makeText(this@AdminMateriDetailActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMateriDetailActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureUpdateMateri(message: String) {
        Toast.makeText(this@AdminMateriDetailActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setShowDialogHapus(materi: MateriModel) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this@AdminMateriDetailActivity)
        alertDialog.setView(view.root)
            .setCancelable(false)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            tvTitleKonfirmasi.text = "Hapus ${materi.judul!!} ?"
            tvBodyKonfirmasi.text = "Judul dan Isi ini akan hapus dan data tidak dapat dipulihkan"

            btnKonfirmasi.setOnClickListener {
                val idMateri = materi.idMateri!!
                postHapusMateri(idMateri)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusMateri(idMateri: String) {
        viewModel.postHapusMateri(idMateri)
    }

    private fun getHapusMateri(){
        viewModel.getHapusMateri().observe(this@AdminMateriDetailActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@AdminMateriDetailActivity)
                is UIState.Failure-> setFailureHapusMateri(result.message)
                is UIState.Success-> setSuccessHapusMateri(result.data)
            }
        }
    }

    private fun setSuccessHapusMateri(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@AdminMateriDetailActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                fetchData()
            } else{
                Toast.makeText(this@AdminMateriDetailActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@AdminMateriDetailActivity, "Gagal di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFailureHapusMateri(message: String) {
        Toast.makeText(this@AdminMateriDetailActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setStopShimmer(){
        binding.apply {
            rvValTentangStroke.visibility = View.VISIBLE

            smValTentangStroke.stopShimmer()
            smValTentangStroke.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smValTentangStroke.startShimmer()
            smValTentangStroke.visibility = View.VISIBLE

            rvValTentangStroke.visibility = View.GONE
        }
    }
}