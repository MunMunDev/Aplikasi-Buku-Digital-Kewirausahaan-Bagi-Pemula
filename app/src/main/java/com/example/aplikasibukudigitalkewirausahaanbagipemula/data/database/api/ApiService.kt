package com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api

import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("buku-digital-kewirausahaan/api/get.php")
    suspend fun getUser(@Query("get_user")getUser: String
    ): ArrayList<UsersModel>

    @GET("buku-digital-kewirausahaan/api/get.php")
    suspend fun getMateri(@Query("get_all_materi") getAllMateri: String
    ): ArrayList<MateriModel>

    @GET("buku-digital-kewirausahaan/api/get.php")
    suspend fun getVideo(@Query("get_all_video") getAllVideo: String
    ): ArrayList<VideoModel>

}