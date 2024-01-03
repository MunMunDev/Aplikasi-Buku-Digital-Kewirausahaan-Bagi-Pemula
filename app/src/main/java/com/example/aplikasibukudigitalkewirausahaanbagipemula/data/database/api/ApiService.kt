package com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api

import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("iuran-kebersihan/api/get.php")
    suspend fun getUser(@Query("get_user")getUser: String
    ): ArrayList<UsersModel>


}