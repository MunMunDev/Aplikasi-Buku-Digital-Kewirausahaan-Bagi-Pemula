package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriListModel
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
    private val _materi = MutableLiveData<UIState<ArrayList<MateriListModel>>>()
    private val _postTambahMateri = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateMateri = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateMateriNoImage = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusMateri = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchDataMateri() {
        viewModelScope.launch {
            _materi.postValue(UIState.Loading)
            try {
                val dataMateri = api.getListMateri("")
                _materi.postValue(UIState.Success(dataMateri))
            } catch (ex: Exception) {
                _materi.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun postTambahData(
        post: RequestBody, judul: RequestBody, fileImage: MultipartBody.Part
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahMateri.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.postAdminTambahListMateri(post, judul, fileImage)
                _postTambahMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postTambahMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatehData(
        post: RequestBody, idListMateri: RequestBody, judul: RequestBody, fileImage: MultipartBody.Part?
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateMateri.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.postAdminUpdateListMateri(post, idListMateri, judul, fileImage!!)
                _postUpdateMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postUpdateMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateDataNoImage(
        idListMateri: String, judul: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateMateriNoImage.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.postAdminUpdateListMateriNoImage("", idListMateri, judul)
                _postUpdateMateriNoImage.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postUpdateMateriNoImage.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusMateri(isListMateri: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusMateri.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.postAdminHapusListMateri("", isListMateri)
                _postHapusMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postHapusMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postWatchMateri(noMateri: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                api.postWatchMateri("", noMateri)
            } catch (ex: Exception){

            }
        }
    }

    fun getDataMateri(): LiveData<UIState<ArrayList<MateriListModel>>> = _materi
    fun getResponseTambahMateri(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahMateri
    fun getResponseUpdateMateri(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateMateri
    fun getResponseUpdateMateriNoImage(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateMateriNoImage
    fun getResponseHapusMateri(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusMateri
}