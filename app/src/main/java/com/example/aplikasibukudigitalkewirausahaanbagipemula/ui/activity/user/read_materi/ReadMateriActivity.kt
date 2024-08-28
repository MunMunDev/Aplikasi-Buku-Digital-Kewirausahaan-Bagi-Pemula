package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.read_materi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.user.ReadMateriAdapter
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityReadMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadMateriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadMateriBinding
    private val viewModel: ReadMateriViewModel by viewModels()
    private lateinit var adapter: ReadMateriAdapter
    private lateinit var idListMateri: String
    private var listData : ArrayList<MateriModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setButton()
        getMateri()
    }

    private fun setDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            idListMateri = extras.getString("idListMateri")!!
            val judul = extras.getString("judul")!!
            binding.titleHeader.text = judul

            fetchMateri()
        }
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun fetchMateri() {
        viewModel.fetchMateri(idListMateri)
    }

    private fun getMateri(){
        viewModel.getMateri().observe(this@ReadMateriActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureMateri(result.message)
                is UIState.Success-> setSuccessMateri(result.data)
            }
        }
    }

    private fun setFailureMateri(message: String) {
        setStopShimmer()
        Toast.makeText(this@ReadMateriActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessMateri(data: ArrayList<MateriModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            listData.addAll(data)
            setAdapter(data)
        } else{
            Toast.makeText(this@ReadMateriActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<MateriModel>) {
        adapter = ReadMateriAdapter(data)

        binding.apply {
            tvMateri.layoutManager = LinearLayoutManager(this@ReadMateriActivity, LinearLayoutManager.VERTICAL, false)
            tvMateri.adapter = adapter
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            tvMateri.visibility = View.VISIBLE

            smMateri.stopShimmer()
            smMateri.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smMateri.startShimmer()
            smMateri.visibility = View.VISIBLE

            tvMateri.visibility = View.GONE
        }
    }
}