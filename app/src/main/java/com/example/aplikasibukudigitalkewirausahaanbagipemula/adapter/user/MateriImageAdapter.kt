package com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriGambarModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ListGambarBinding
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.Constant
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem

class MateriImageAdapter(
    private var listGambar: ArrayList<MateriGambarModel>,
    var onClick: OnClickItem.ClickMateriGambar
): RecyclerView.Adapter<MateriImageAdapter.ViewHolder>() {
    class ViewHolder(var binding: ListGambarBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListGambarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listGambar.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = listGambar[position]
        holder.binding.apply {
            Glide.with(holder.itemView)
                .load("${Constant.BASE_URL}/${Constant.GAMBAR_URL}${list.gambar}") // URL Gambar
                .error(R.drawable.gambar_error_image)
                .into(ivGambarInformation) // imageView mana yang akan diterapkan

            tvKeteranganGambar.text = list.deskripsi

            ivGambarInformation.setOnClickListener {
                onClick.clickItemGambar(list.gambar!!, list.deskripsi!!, it)
            }
        }
    }

//    interface OnClickItem{
//        fun clickGambar(image:String, keterangan: String, it:View)
//    }
}