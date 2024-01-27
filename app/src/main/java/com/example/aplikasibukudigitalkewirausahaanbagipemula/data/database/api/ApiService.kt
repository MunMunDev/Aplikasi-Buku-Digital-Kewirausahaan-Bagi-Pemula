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
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("buku-digital-kewirausahaan/api/get.php")
    suspend fun getAllUser(@Query("all_user") allUser: String
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

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postHapusUser(
        @Field("hapus_akun") hapusAkun:String,
        @Field("id_user") idUser: String
    ): ArrayList<ResponseModel>


    @Multipart
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminTambahMateri(
        @Part("tambah_materi") tambahMateri: RequestBody,
        @Part("id_materi") idMateri: RequestBody,
        @Part("nama_materi") namaMateri: RequestBody,
        @Part("nama_penulis") namaPenulis: RequestBody,
        @Part file_pdf: MultipartBody.Part,
        @Part file_image: MultipartBody.Part,
        @Part("lokasi_file") lokasiFile: RequestBody,
        @Part("url_materi") urlMateri: RequestBody,
        @Part("url_image") urlImage: RequestBody,
        @Part("jumlah_pelihat") jumlahPelihat: RequestBody
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminTambahMateri(
        @Part file_pdf: MultipartBody.Part,
    ): ArrayList<ResponseModel>


    @Multipart
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminUpdateMateri(
        @Part("update_materi") updateMateri: RequestBody,
        @Part("no_materi") noMateri: RequestBody,
        @Part("id_materi") idMateri: RequestBody,
        @Part("nama_materi") namaMateri: RequestBody,
        @Part("nama_penulis") namaPenulis: RequestBody,
        @Part file_pdf: MultipartBody.Part,
        @Part file_image: MultipartBody.Part,
        @Part("lokasi_file") lokasiFile: RequestBody,
        @Part("url_materi") urlMateri: RequestBody,
        @Part("url_image") urlImage: RequestBody,
        @Part("jumlah_pelihat") jumlahPelihat: RequestBody
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminUpdateMateriNoImage(
        @Part("update_materi") updateMateri: RequestBody,
        @Part("no_materi") noMateri: RequestBody,
        @Part("id_materi") idMateri: RequestBody,
        @Part("nama_materi") namaMateri: RequestBody,
        @Part("nama_penulis") namaPenulis: RequestBody,
        @Part file_pdf: MultipartBody.Part,
        @Part("lokasi_file") lokasiFile: RequestBody,
        @Part("url_materi") urlMateri: RequestBody,
        @Part("url_image") urlImage: RequestBody,
        @Part("jumlah_pelihat") jumlahPelihat: RequestBody
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminUpdateMateriNoImageAndPdf(
        @Part("update_materi") updateMateri: RequestBody,
        @Part("no_materi") noMateri: RequestBody,
        @Part("id_materi") idMateri: RequestBody,
        @Part("nama_materi") namaMateri: RequestBody,
        @Part("nama_penulis") namaPenulis: RequestBody,
        @Part("lokasi_file") lokasiFile: RequestBody,
        @Part("url_materi") urlMateri: RequestBody,
        @Part("url_image") urlImage: RequestBody,
        @Part("jumlah_pelihat") jumlahPelihat: RequestBody
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminUpdateMateriNoPdf(
        @Part("update_materi") updateMateri: RequestBody,
        @Part("no_materi") noMateri: RequestBody,
        @Part("id_materi") idMateri: RequestBody,
        @Part("nama_materi") namaMateri: RequestBody,
        @Part("nama_penulis") namaPenulis: RequestBody,
        @Part file_image: MultipartBody.Part,
        @Part("lokasi_file") lokasiFile: RequestBody,
        @Part("url_materi") urlMateri: RequestBody,
        @Part("url_image") urlImage: RequestBody,
        @Part("jumlah_pelihat") jumlahPelihat: RequestBody
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminHapusMateri(
        @Field("hapus_materi") hapusMateri:String,
        @Field("no_materi") noMateri:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminTambahVideo(
        @Field("tambah_video") tambahVideo:String,
        @Field("nama_video") namaVideo: String,
        @Field("url_video") urlVideo:String,
        @Field("jumlah_pelihat") jumlahPelihat: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminUpdateVideo(
        @Field("update_video") updateVideo:String,
        @Field("no_video") noVideo:String,
        @Field("nama_video") namaVideo: String,
        @Field("url_video") urlVideo:String,
        @Field("jumlah_pelihat") jumlahPelihat: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminHapusVideo(
        @Field("hapus_video") hapusVideo:String,
        @Field("no_video") noVideo:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postWatchVideo(
        @Field("watch_video") watchVideo:String,
        @Field("no_video") noVideo:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postWatchMateri(
        @Field("watch_materi") watchMateri:String,
        @Field("no_materi") noMateri:String
    ): ArrayList<ResponseModel>
}