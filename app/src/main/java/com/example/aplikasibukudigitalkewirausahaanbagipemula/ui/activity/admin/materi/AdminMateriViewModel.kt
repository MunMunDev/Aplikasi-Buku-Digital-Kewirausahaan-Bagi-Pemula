package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AdminMateriViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _materi = MutableLiveData<UIState<ArrayList<MateriModel>>>()
    private val _postTambahMateri = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateMateri = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusMateri = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchDataMateri() {
        viewModelScope.launch {
            _materi.postValue(UIState.Loading)
            try {
                val dataMateri = api.getMateri("")
                _materi.postValue(UIState.Success(dataMateri))
            } catch (ex: Exception) {
                _materi.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun postTambahData(
        post: RequestBody, idMateri: RequestBody, namaMateri: RequestBody, namaPenulis: RequestBody, filePdf: MultipartBody.Part,
        fileImage: MultipartBody.Part, lokasiFile: RequestBody, urlMateri: RequestBody, urlImage: RequestBody, jumlahPelihat: RequestBody
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahMateri.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.postAdminTambahMateri(post, idMateri, namaMateri, namaPenulis, filePdf, fileImage,lokasiFile, urlMateri, urlImage, jumlahPelihat)
                _postTambahMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postTambahMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatehData(
        post: RequestBody, noMateri: RequestBody, idMateri: RequestBody, namaMateri: RequestBody, namaPenulis: RequestBody, filePdf: MultipartBody.Part,
        fileImage: MultipartBody.Part, lokasiFile: RequestBody, urlMateri: RequestBody, urlImage: RequestBody, jumlahPelihat: RequestBody
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateMateri.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.postAdminUpdateMateri(post, noMateri, idMateri, namaMateri, namaPenulis, filePdf, fileImage,lokasiFile, urlMateri, urlImage, jumlahPelihat)
                _postUpdateMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postUpdateMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatehDataNoImage(
        post: RequestBody, noMateri: RequestBody, idMateri: RequestBody, namaMateri: RequestBody, namaPenulis: RequestBody, filePdf: MultipartBody.Part,
        lokasiFile: RequestBody, urlMateri: RequestBody, urlImage: RequestBody, jumlahPelihat: RequestBody
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateMateri.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.postAdminUpdateMateriNoImage(post, noMateri, idMateri, namaMateri, namaPenulis, filePdf, lokasiFile, urlMateri, urlImage, jumlahPelihat)
                _postUpdateMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postUpdateMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatehDataNoImageAndPdf(
        post: RequestBody, noMateri: RequestBody, idMateri: RequestBody, namaMateri: RequestBody, namaPenulis: RequestBody,
        lokasiFile: RequestBody, urlMateri: RequestBody, urlImage: RequestBody, jumlahPelihat: RequestBody
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateMateri.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.postAdminUpdateMateriNoImageAndPdf(post, noMateri, idMateri, namaMateri, namaPenulis, lokasiFile, urlMateri, urlImage, jumlahPelihat)
                _postUpdateMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postUpdateMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatehDataNoPdf(
        post: RequestBody, noMateri: RequestBody, idMateri: RequestBody, namaMateri: RequestBody, namaPenulis: RequestBody,
        fileImage: MultipartBody.Part, lokasiFile: RequestBody, urlMateri: RequestBody, urlImage: RequestBody, jumlahPelihat: RequestBody
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateMateri.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.postAdminUpdateMateriNoPdf(post, noMateri, idMateri, namaMateri, namaPenulis, fileImage,lokasiFile, urlMateri, urlImage, jumlahPelihat)
                _postUpdateMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postUpdateMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusMateri(noMateri: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusMateri.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.postAdminHapusMateri("", noMateri)
                _postHapusMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postHapusMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getDataMateri(): LiveData<UIState<ArrayList<MateriModel>>> = _materi
    fun getResponseTambahMateri(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahMateri
    fun getResponseUpdateMateri(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateMateri
    fun getResponseHapusMateri(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusMateri
}