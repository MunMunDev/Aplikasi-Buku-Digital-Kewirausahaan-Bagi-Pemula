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
import retrofit2.http.Field
import javax.inject.Inject

@HiltViewModel
class AdminMateriViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _materi = MutableLiveData<UIState<ArrayList<MateriModel>>>()
    private val _postTambahMateri = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateMateri = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

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
        namaMateri: String, namaPenulis: String, file: MultipartBody.Part,
        lokasiFile: String, urlMateri: String, urlImage: String, jumlahPelihat: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahMateri.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.adminTambahMateri(namaMateri, namaPenulis, file, lokasiFile, urlMateri, urlImage, jumlahPelihat)
                _postTambahMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postTambahMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdatehData(idMateri: String, namaMateri: String, namaPenulis: String, file: MultipartBody.Part, jumlahPelihat: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateMateri.postValue(UIState.Loading)
            try {
                val postTambahMateri = api.adminUpdateMateri(idMateri, namaMateri, namaPenulis, file, jumlahPelihat)
                _postUpdateMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception){
                _postUpdateMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getDataMateri(): LiveData<UIState<ArrayList<MateriModel>>> = _materi
    fun getResponseTambahMateri(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahMateri
    fun getResponseUpdateMateri(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateMateri
}