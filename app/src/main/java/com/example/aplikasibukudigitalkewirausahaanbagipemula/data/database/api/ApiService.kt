package com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api

import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postUpdateUser(
        @Field("update_akun") updateAkun:String,
        @Field("id_user") idUser: String,
        @Field("nama") nama:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("username_lama") usernameLama: String
    ): ArrayList<ResponseModel>

//    @FormUrlEncoded
//    @POST("iuran-kebersihan/api/post.php")
//    suspend fun postUpdateUser(
//        @Field("update_akun") update_akun: String,
//        @Field("id_user") id_user: String,
//        @Field("nama") nama: String,
//        @Field("nomor_hp") nomor_hp: String,
//        @Field("username") username: String,
//        @Field("password") password: String,
//        @Field("username_lama") usernameLama: String
//    ): ArrayList<ResponseModel>

    @Headers("Content-Type: application/json")
    @Multipart
    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun adminTambahMateri(
        @Part("nama_materi") namaMateri: RequestBody,
        @Part("nama_penulis") namaPenulis: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("lokasi_file") lokasiFile: RequestBody,
        @Part("url_materi") urlMateri: RequestBody,
        @Part("url_image") urlImage: RequestBody,
        @Part("jumlah_pelihat") jumlahPelihat: RequestBody
    ): ArrayList<ResponseModel>

    @Headers("Content-Type: application/json")
    @Multipart
    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun adminUpdateMateri(
        @Field("id_materi") idMateri:String,
        @Field("nama_materi") namaMateri:String,
        @Field("nama_penulis") namaPenulis:String,
        @Part file: MultipartBody.Part,
        @Field("jumlah_pelihat") jumlahPelihat:String
    ): ArrayList<ResponseModel>
}