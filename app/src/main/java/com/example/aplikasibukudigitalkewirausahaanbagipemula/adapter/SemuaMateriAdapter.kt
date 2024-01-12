package com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ListDataSemuaBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.ReadPdfActivity
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.Constant

class SemuaMateriAdapter(private var listMateri: ArrayList<MateriModel>): RecyclerView.Adapter<SemuaMateriAdapter.ViewHolder>() {

    var tempMateri = listMateri
    fun searchData(kata: String){
        val vKata = kata.toLowerCase().trim()
        var data = listMateri.filter {
            it.namaMateri!!.lowercase().trim().contains(vKata)
        } as ArrayList
        tempMateri = data
        notifyDataSetChanged()
    }
    class ViewHolder(val binding: ListDataSemuaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListDataSemuaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tempMateri.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataMateri = tempMateri[position]
        holder.apply {
            binding.apply {
                Glide.with(itemView.context)
                    .load("${Constant.BASE_URL}${dataMateri.lokasiFile}${dataMateri.urlImage}") // URL Gambar
                    .error(R.drawable.image_book)
                    .into(ivGambarMateri) // imageView mana yang akan diterapkan

                var namaPenulis = ""
                namaPenulis = try {
                    val arrayNamaPenulis = dataMateri.namaPenulis!!.split(";-")
                    arrayNamaPenulis[0]
                } catch (ex: Exception){
                    dataMateri.namaPenulis!!
                }
                tvNamaPemateri.text = namaPenulis
                tvJudulMateri.text = dataMateri.namaMateri
                tvJumlahDilihat.text = "${dataMateri.jumlahPelihat}x Di Baca"
                btnBuka.text = "Read Now"
                btnBuka.setOnClickListener {
                    itemView.context.startActivity(Intent(itemView.context, ReadPdfActivity::class.java))
                }
            }
            itemView.apply {
                this.setOnClickListener{
//                    Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
//                    context.startActivity(Intent(context, ReadPdfActivity::class.java))
                }
            }
        }
    }
}