package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.adapter.admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.R
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriListModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.ListDataSemuaBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.Constant
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem

class AdminListMateriAdapter(
    private var listMateri: ArrayList<MateriListModel>,
    private var click: OnClickItem.ClickMateri
) : RecyclerView.Adapter<AdminListMateriAdapter.ViewHolder>() {

    var tempMateri = listMateri
//    fun searchData(kata: String){
//        val vKata = kata.toLowerCase().trim()
//        var data = listMateri.filter {
//            it.namaMateri!!.lowercase().trim().contains(vKata)
//        } as ArrayList
//        tempMateri = data
//        notifyDataSetChanged()
//    }

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
                    .load("${Constant.BASE_URL}${Constant.GAMBAR_URL}${dataMateri.gambar}") // URL Gambar
                    .error(R.drawable.image_book)
                    .into(ivGambarMateri) // imageView mana yang akan diterapkan

                tvJudulMateri.text = dataMateri.judul
                btnBuka.text = "Settings"
                btnBuka.setOnClickListener {
                    click.clickItemMateri(dataMateri, it)
                }
            }
        }
    }

}