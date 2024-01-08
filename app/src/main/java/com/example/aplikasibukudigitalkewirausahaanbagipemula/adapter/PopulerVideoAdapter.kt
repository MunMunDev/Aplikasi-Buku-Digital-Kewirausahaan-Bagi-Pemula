package com.example.aplikasibukudigitalkewirausahaanbagipemula.adapter

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aplikasibukudigitalkewirausahaanbagipemula.R
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.databinding.ListDataPopulerBinding

class PopulerVideoAdapter(private var listVideo: ArrayList<VideoModel>): RecyclerView.Adapter<PopulerVideoAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListDataPopulerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListDataPopulerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if(listVideo.size>2){
            3
        } else{
            listVideo.size
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataMateri = listVideo[position]
        holder.apply {
            setImageFromYoutube(binding, itemView, dataMateri.urlVideo)
            binding.apply {
                tvJudulMateri.text = dataMateri.namaVideo
                var namaPenulis = ""
                tvNamaPemateri.text = namaPenulis
            }
            itemView.apply {
                this.setOnClickListener{
                    setToYoutube(context, dataMateri.urlVideo)
                }
            }
        }
    }

    private fun setToYoutube(context: Context, urlVideo: String?) {
        val id = searchIdUrlVideo(urlVideo!!)
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    }

    private fun setImageFromYoutube(
        binding: ListDataPopulerBinding,
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