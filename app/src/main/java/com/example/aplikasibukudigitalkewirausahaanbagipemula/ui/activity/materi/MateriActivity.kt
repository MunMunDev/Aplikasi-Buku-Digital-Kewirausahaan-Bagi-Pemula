package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.materi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.SemuaMateriAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.main.MainActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.search.SearchViewModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_search_data.btnBack
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class MateriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMateriBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    @Inject lateinit var loading: LoadingAlertDialog
    private val viewModel: MateriViewModel by viewModels()
    private lateinit var adapter: SemuaMateriAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNavigationDrawer()
        fetchDataMateri()
        getDataMateri()
    }

    private fun setNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@MateriActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@MateriActivity)
        }
    }

    private fun fetchDataMateri() {
        viewModel.fetchDataMateri()
    }

    private fun getDataMateri() {
        viewModel.getDataMateri().observe(this@MateriActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@MateriActivity)
                is UIState.Failure-> setFailureDataMateri(result.message)
                is UIState.Success-> setSuccessDataMateri(result.data)
            }
        }
    }

    private fun setFailureDataMateri(message: String) {
        Toast.makeText(this@MateriActivity, message, Toast.LENGTH_SHORT).show()
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
        adapter = SemuaMateriAdapter(data)
        binding.apply {
            rvMateri.layoutManager = LinearLayoutManager(
                this@MateriActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvMateri.adapter = adapter
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
        startActivity(Intent(this@MateriActivity, MainActivity::class.java))
        finish()
    }
}