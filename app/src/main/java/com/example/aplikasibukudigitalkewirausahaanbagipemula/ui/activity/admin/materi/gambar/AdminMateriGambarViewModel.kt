package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.gambar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriGambarModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AdminMateriGambarViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _materiGambar = MutableLiveData<UIState<ArrayList<MateriGambarModel>>>()
    private val _postTambahMateriGambar = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateMateriGambar = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusMateriGambar = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchMateriGambar(idMateri: String) {
        viewModelScope.launch {
            _materiGambar.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getMateriGambar("", idMateri)
                _materiGambar.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _materiGambar.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }
    fun postTambahMateriGambar(
        post:RequestBody, idMateri: RequestBody, gambar: MultipartBody.Part, deskripsi: RequestBody
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahMateriGambar.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahMateriGambar = api.postAdminTambahMateriGambar(post, idMateri, gambar, deskripsi)
                _postTambahMateriGambar.postValue(UIState.Success(postTambahMateriGambar))
            } catch (ex: Exception) {
                _postTambahMateriGambar.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateMateriGambar(
        post:RequestBody, idMateriGambar: RequestBody, gambar: MultipartBody.Part, deskripsi: RequestBody
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateMateriGambar.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahMateriGambar =
                    api.postAdminUpdateMateriGambar(post, idMateriGambar, gambar, deskripsi)
                _postUpdateMateriGambar.postValue(UIState.Success(postTambahMateriGambar))
            } catch (ex: Exception) {
                _postUpdateMateriGambar.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateMateriGambarNoImage(
        idMateriGambar: String, deskripsi: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateMateriGambar.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahMateriGambar =
                    api.postAdminUpdateMateriGambarNoImage("", idMateriGambar, deskripsi)
                _postUpdateMateriGambar.postValue(UIState.Success(postTambahMateriGambar))
            } catch (ex: Exception) {
                _postUpdateMateriGambar.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusMateriGambar(idMateriGambar: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusMateriGambar.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahMateriGambar = api.postAdminHapusMateriGambar("", idMateriGambar)
                _postHapusMateriGambar.postValue(UIState.Success(postTambahMateriGambar))
            } catch (ex: Exception) {
                _postHapusMateriGambar.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getMateriGambar(): LiveData<UIState<ArrayList<MateriGambarModel>>> = _materiGambar
    fun getTambahMateriGambar(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahMateriGambar
    fun getUpdateMateriGambar(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateMateriGambar
    fun getUpdateMateriGambarNoImage(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateMateriGambar
    fun getHapusMateriGambar(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusMateriGambar
}