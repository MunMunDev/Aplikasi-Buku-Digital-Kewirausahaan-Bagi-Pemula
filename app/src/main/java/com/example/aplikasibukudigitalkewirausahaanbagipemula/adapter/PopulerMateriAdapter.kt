package com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ListDataPopulerBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.read_pdf.ReadPdfActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.Constant

class PopulerMateriAdapter(private var listMateri: ArrayList<MateriModel>): RecyclerView.Adapter<PopulerMateriAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListDataPopulerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListDataPopulerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if(listMateri.size>2){
            3
        } else{
            listMateri.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataMateri = listMateri[position]
        holder.apply {
            binding.apply {
                Glide.with(itemView.context)
                    .load("${Constant.BASE_URL}${dataMateri.lokasiFile}${dataMateri.urlImage}") // URL Gambar
                    .error(R.drawable.image_book)
                    .into(ivGambarMateri) // imageView mana yang akan diterapkan

                tvJudulMateri.text = dataMateri.namaMateri
                var namaPenulis = ""
                namaPenulis = try {
                    val arrayNamaPenulis = dataMateri.namaPenulis!!.split(";-")
                    arrayNamaPenulis[0]
                } catch (ex: Exception){
                    dataMateri.namaPenulis!!
                }
                tvNamaPemateri.text = namaPenulis
            }
            itemView.apply {
                this.setOnClickListener{
                    Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, ReadPdfActivity::class.java))
                }
            }
        }
    }
}