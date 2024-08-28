package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.materi.detail

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminMateriDetailViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _valueTentangStroke = MutableLiveData<UIState<ArrayList<MateriModel>>>()
    private val _postTambahMateri = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateMateri = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusMateri = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchMateri(idListMateri: String) {
        viewModelScope.launch {
            _valueTentangStroke.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getMateri("", idListMateri)
                _valueTentangStroke.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _valueTentangStroke.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun postTambahMateri(
        idListMateri: String, judul: String, deskripsi: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahMateri.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahMateri = api.postTambahMateri("", idListMateri, judul, deskripsi)
                _postTambahMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception) {
                _postTambahMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateMateri(
        idMateri: String, idListMateri: String, judul: String, deskripsi: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateMateri.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahMateri =
                    api.postUpdateMateri("", idMateri, idListMateri, judul, deskripsi)
                _postUpdateMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception) {
                _postUpdateMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusMateri(id_materi: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusMateri.postValue(UIState.Loading)
            delay(1_000)
            try {
                val postTambahMateri = api.postHapusMateri("", id_materi)
                _postHapusMateri.postValue(UIState.Success(postTambahMateri))
            } catch (ex: Exception) {
                _postHapusMateri.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getTentangStrokeList(): LiveData<UIState<ArrayList<MateriModel>>> = _valueTentangStroke
    fun getTambahMateri(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahMateri
    fun getUpdateMateri(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateMateri
    fun getHapusMateri(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusMateri
}
