package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.materi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.adapter.user.SemuaMateriAdapter
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriListModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.ActivityMateriBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.main.MainActivity
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.read_materi.ReadMateriActivity
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.KontrolNavigationDrawer
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
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

    private fun setSuccessDataMateri(data: ArrayList<MateriListModel>) {
        loading.alertDialogCancel()
        setStopShimmer()
        val sort = data.sortedWith(compareBy { it.judul })
        val dataArrayList = arrayListOf<MateriListModel>()
        dataArrayList.addAll(sort)
        setRecyclerViewMateri(dataArrayList)
    }

    private fun setRecyclerViewMateri(data: ArrayList<MateriListModel>) {
        adapter = SemuaMateriAdapter(data, object : OnClickItem.ClickMateri {
            override fun clickItemMateri(materi: MateriListModel, it: View) {
                val intent = Intent(this@MateriActivity, ReadMateriActivity::class.java)
                intent.putExtra("idListMateri", materi.idListMateri)
                intent.putExtra("judul", materi.judul)
                startActivity(intent)
            }
        })

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