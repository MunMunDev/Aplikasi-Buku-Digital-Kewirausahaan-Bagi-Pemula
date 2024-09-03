package com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api

import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriGambarModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriListModel
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
    suspend fun getListMateri(@Query("get_all_materi") getAllMateri: String
    ): ArrayList<MateriListModel>

    @GET("buku-digital-kewirausahaan/api/get.php")
    suspend fun getMateri(@Query("get_materi") getAllMateri: String,
                          @Query("id_list_materi") id_list_materi: String
    ): ArrayList<MateriModel>

    @GET("buku-digital-kewirausahaan/api/get.php")
    suspend fun getMateriGambar(@Query("get_materi_gambar") getAllMateriGambar: String,
                                @Query("id_materi") id_materi: String
    ): ArrayList<MateriGambarModel>

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
    suspend fun postAdminTambahListMateri(
        @Part("tambah_materi") tambahMateri: RequestBody,
        @Part("judul") judul: RequestBody,
        @Part file_image: MultipartBody.Part,
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminUpdateListMateri(
        @Part("update_materi") updateMateri: RequestBody,
        @Part("id_list_materi") idListMateri: RequestBody,
        @Part("judul") judul: RequestBody,
        @Part file_image: MultipartBody.Part,
    ): ArrayList<ResponseModel>


    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminUpdateListMateriNoImage(
        @Field("update_materi_no_image") updateMateriNoImage: String,
        @Field("id_list_materi") idListMateri: String,
        @Field("judul") judul: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminHapusListMateri(
        @Field("hapus_materi") hapusMateri:String,
        @Field("id_list_materi") idListMateri:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postTambahMateri(
        @Field("post_tambah_materi") post_tambah_materi:String,
        @Field("id_list_materi") id_list_materi:String,
        @Field("judul") judul:String,
        @Field("deskripsi") deskripsi:String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postUpdateMateri(
        @Field("post_update_materi") post_update_materi:String,
        @Field("id_materi") id_materi:String,
        @Field("id_list_materi") id_list_materi:String,
        @Field("judul") judul:String,
        @Field("deskripsi") deskripsi:String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postHapusMateri(
        @Field("post_hapus_materi") post_hapus_materi:String,
        @Field("id_materi") id_materi: String
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminTambahMateriGambar(
        @Part("post_tambah_materi_gambar") post_tambah_materi_gambar: RequestBody,
        @Part("id_materi") id_materi:RequestBody,
        @Part file_image: MultipartBody.Part,
        @Part("deskripsi") deskripsi: RequestBody,
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminUpdateMateriGambar(
        @Part("post_update_materi_gambar") post_update_materi_gambar: RequestBody,
        @Part("id_materi_gambar") id_materi_gambar: RequestBody,
        @Part file_image: MultipartBody.Part,
        @Part("deskripsi") deskripsi: RequestBody,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminUpdateMateriGambarNoImage(
        @Field("post_update_materi_gambar_no_image") post_update_materi_gambar_no_image:String,
        @Field("id_materi_gambar") id_materi_gambar:String,
        @Field("deskripsi") deskripsi:String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("buku-digital-kewirausahaan/api/post.php")
    suspend fun postAdminHapusMateriGambar(
        @Field("post_hapus_materi_gambar") post_hapus_materi_gambar:String,
        @Field("id_materi_gambar") id_materi_gambar: String
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