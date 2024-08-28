package com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ListReadMateriBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem


class ReadMateriAdapter(
    private val listReadMateri: ArrayList<MateriModel>,
): RecyclerView.Adapter<ReadMateriAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListReadMateriBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListReadMateriBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listReadMateri.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val materi = listReadMateri[position]
        holder.binding.apply {
            tvJudul.text = materi.judul
            tvDeskripsil.text = materi.deskripsi
        }
    }
}