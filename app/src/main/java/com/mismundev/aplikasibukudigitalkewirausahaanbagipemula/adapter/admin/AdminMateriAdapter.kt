package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.adapter.admin

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.R
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.ListAdminMateriBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem

class AdminMateriAdapter(
    private var listValTentangStroke: ArrayList<MateriModel>,
    private var onClick: OnClickItem.ClickAdminMateri
): RecyclerView.Adapter<AdminMateriAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListAdminMateriBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListAdminMateriBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return (listValTentangStroke.size+1)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            if(position==0){
                tvNo.text = "NO"
                tvJudul.text = "Judul Isi"
                tvDeskripsi.text = "Isi Penjelasan"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvJudul.setBackgroundResource(R.drawable.bg_table_title)
                tvDeskripsi.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvJudul.setTextColor(Color.parseColor("#ffffff"))
                tvDeskripsi.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvJudul.setTypeface(null, Typeface.BOLD)
                tvDeskripsi.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val materi = listValTentangStroke[(position-1)]

                tvNo.text = "$position"
                tvJudul.text = materi.judul
                tvDeskripsi.text = materi.deskripsi
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvJudul.setBackgroundResource(R.drawable.bg_table)
                tvDeskripsi.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvJudul.setTextColor(Color.parseColor("#000000"))
                tvDeskripsi.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvJudul.setTypeface(null, Typeface.NORMAL)
                tvDeskripsi.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvJudul.gravity = Gravity.CENTER_VERTICAL

                tvGambar.visibility = View.GONE
                ivGambar.visibility = View.VISIBLE

                tvJudul.setOnClickListener{
                    onClick.clickItemJudul(materi.judul!!, it)
                }
                tvDeskripsi.setOnClickListener {
                    onClick.clickItemDeskripsi(materi.deskripsi!!, it)
                }
                ivGambar.setOnClickListener {
                    onClick.clickItemGambar(materi.idMateri!!, materi.judul!!, it)
                }
                tvSetting.setOnClickListener {
                    onClick.clickItemSetting(materi, it)
                }
            }
        }
    }
}