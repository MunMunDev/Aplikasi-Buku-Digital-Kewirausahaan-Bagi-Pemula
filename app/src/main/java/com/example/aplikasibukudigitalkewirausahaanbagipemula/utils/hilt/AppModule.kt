package com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.hilt

import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.Constant
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.KataAcak
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.LoadingAlertDialog
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.TanggalDanWaktu
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun apiConfig(): ApiService = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(
            OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(ApiService::class.java)

//    @Provides
//    @Singleton
//    fun tanggalDanWaktu(): TanggalDanWaktu = TanggalDanWaktu()

    @Provides
    fun loading(): LoadingAlertDialog = LoadingAlertDialog()

    @Provides
    @Singleton
    fun kataAcak(): KataAcak = KataAcak()
}