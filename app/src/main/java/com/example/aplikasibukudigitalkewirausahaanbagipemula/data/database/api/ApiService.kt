package com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api

import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("buku-digital-kewirausahaan/api/get.php")
    suspend fun getAllUser(@Query("get_all_user")getAllUser: String
    ): ArrayList<UsersModel>

    @GET("buku-digital-kewirausahaan/api/get.php")
    suspend fun getUser(@Query("get_user") getUser: String,
                        @Query("username") username: String,
                        @Query("password") password: String
    ): ArrayList<UsersModel>

    @GET("buku-digital-kewirausahaan/api/get.php")
    suspend fun cekUser(@Query("cek_user") cekUser: String,
                        @Query("username") username: String,
                        @Query("password") password: String
    ): ArrayList<UsersModel>

    @GET("buku-digital-kewirausahaan/api/get.php")
    suspend fun getMateri(@Query("get_all_materi") getAllMateri: String
    ): ArrayList<MateriModel>

    @GET("buku-digital-kewirausahaan/api/get.php")
    suspend fun getVideo(@Query("get_all_video") getAllVideo: String
    ): ArrayList<VideoModel>


    // POST
    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun addUser(
        @Field("add_user") addUser:String,
        @Field("nama") nama:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("sebagai") sebagai:String
    ): ArrayList<ResponseModel>
}