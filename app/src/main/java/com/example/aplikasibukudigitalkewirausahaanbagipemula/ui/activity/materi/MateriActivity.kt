package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.materi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.SemuaMateriAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.search.SearchViewModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_search_data.btnBack
import java.util.ArrayList
import javax.inject.Inject

@AndroidEntryPoint
class MateriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMateriBinding
    @Inject lateinit var loading: LoadingAlertDialog
    private val viewModel: MateriViewModel by viewModels()
    private lateinit var adapter: SemuaMateriAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButton()
        fetchDataMateri()
        getDataMateri()
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
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
        loading.alertDialogCancel()
    }

    private fun setSuccessDataMateri(data: ArrayList<MateriModel>) {
        loading.alertDialogCancel()
        val sort = data.sortedWith(compareBy { it.namaMateri })
        val dataArrayList = arrayListOf<MateriModel>()
        dataArrayList.addAll(sort)
        setRecyclerViewMateri(dataArrayList)
    }

    private fun setRecyclerViewMateri(data: ArrayList<MateriModel>) {
        adapter = SemuaMateriAdapter(data)
        binding.apply {
            rvData.layoutManager = LinearLayoutManager(
                this@MateriActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            rvData.adapter = adapter
        }
    }
}