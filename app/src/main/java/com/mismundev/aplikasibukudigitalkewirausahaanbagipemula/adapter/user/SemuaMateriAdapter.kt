package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.adapter.user

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

class SemuaMateriAdapter(
    private var listMateri: ArrayList<MateriListModel>,
    private var click: OnClickItem.ClickMateri
): RecyclerView.Adapter<SemuaMateriAdapter.ViewHolder>() {

    var tempMateri = listMateri
    fun searchData(kata: String){
        val vKata = kata.toLowerCase().trim()
        var data = listMateri.filter {
            it.judul!!.lowercase().trim().contains(vKata)
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
                    .load("${Constant.BASE_URL}${Constant.GAMBAR_URL}${dataMateri.gambar}") // URL Gambar
                    .error(R.drawable.image_book)
                    .into(ivGambarMateri) // imageView mana yang akan diterapkan

                tvJudulMateri.text = dataMateri.judul
                btnBuka.text = "Read Now"
                btnBuka.setOnClickListener {
                    click.clickItemMateri(dataMateri, it)
                }
            }
        }
    }
}