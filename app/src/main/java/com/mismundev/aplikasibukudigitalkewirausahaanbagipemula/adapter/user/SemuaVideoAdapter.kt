package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.adapter.user

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.R
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.databinding.ListDataSemuaBinding
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.OnClickItem

class SemuaVideoAdapter(
    private var listVideo: ArrayList<VideoModel>,
    private var click: OnClickItem.ClickVideo
): RecyclerView.Adapter<SemuaVideoAdapter.ViewHolder>() {

    var tempVideo = listVideo
    fun searchData(kata: String){
        val vKata = kata.toLowerCase().trim()
        var data = listVideo.filter {
            it.namaVideo!!.lowercase().trim().contains(vKata)
        } as ArrayList
        tempVideo = data
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ListDataSemuaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListDataSemuaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tempVideo.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataVideo = tempVideo[position]
        holder.apply {
            setImageFromYoutube(binding, itemView, dataVideo.urlVideo)
            binding.apply {
                tvJudulMateri.text = dataVideo.namaVideo
//                tvNamaPemateri.text = "YouTube"
//                tvJumlahDilihat.text = "${dataVideo.jumlahPelihat}x Di Tonton"
                btnBuka.text = "Watch Now"
                btnBuka.setOnClickListener {
                    click.clickItemVideo(dataVideo, it)
//                    setToYoutube(itemView.context, dataVideo.urlVideo)
                }
            }
            itemView.apply {
                this.setOnClickListener{
//                    setToYoutube(context, dataVideo.urlVideo)
                }
            }
        }
    }

//    private fun setToYoutube(context: Context, urlVideo: String?) {
//        val id = searchIdUrlVideo(urlVideo!!)
//        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
//        val webIntent = Intent(
//            Intent.ACTION_VIEW,
//            Uri.parse("http://www.youtube.com/watch?v=$id")
//        )
//        try {
//            context.startActivity(appIntent)
//        } catch (ex: ActivityNotFoundException) {
//            context.startActivity(webIntent)
//        }
//    }

    private fun setImageFromYoutube(
        binding: ListDataSemuaBinding,
        itemView: View,
        urlVideo: String?
    ) {
        val id = searchIdUrlVideo(urlVideo!!)
        binding.apply {
            Glide.with(itemView.context)
                .load("https://img.youtube.com/vi/$id/0.jpg") // URL Gambar
                .error(R.drawable.image_book)
                .into(ivGambarMateri) // imageView mana yang akan diterapkan
        }
    }

    fun searchIdUrlVideo(urlVideo: String): String {
        return try {
            val arrayUrlImageVideo = urlVideo.split("v=")
            arrayUrlImageVideo[1]
        } catch (ex: Exception){
            val arrayUrlImageVideo = urlVideo.split("si=")
            arrayUrlImageVideo[1]
        }
    }
}