package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.R
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriGambarModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.ListGambarBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.Constant
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem

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