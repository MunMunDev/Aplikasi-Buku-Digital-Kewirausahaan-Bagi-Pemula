package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminVideoViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _video = MutableLiveData<UIState<ArrayList<VideoModel>>>()
    private val _postTambahVideo = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postUpdateVideo = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private val _postHapusVideo = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchDataVideo() {
        viewModelScope.launch {
            _video.postValue(UIState.Loading)
            try {
                val dataVideo = api.getVideo("")
                _video.postValue(UIState.Success(dataVideo))
            } catch (ex: Exception) {
                _video.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun postTambahData(
        namaVideo: String, urlVideo: String, jumlahPelihat: String
    ){
        viewModelScope.launch(Dispatchers.IO) {
            _postTambahVideo.postValue(UIState.Loading)
            try {
                val postTambahVideo = api.postAdminTambahVideo("", namaVideo, urlVideo, jumlahPelihat)
                _postTambahVideo.postValue(UIState.Success(postTambahVideo))
            } catch (ex: Exception){
                _postTambahVideo.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateData(noVideo: String, namaVideo: String, urlVideo: String, jumlahPelihat: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postUpdateVideo.postValue(UIState.Loading)
            try {
                val postTambahVideo = api.postAdminUpdateVideo("", noVideo, namaVideo, urlVideo, jumlahPelihat)
                _postUpdateVideo.postValue(UIState.Success(postTambahVideo))
            } catch (ex: Exception){
                _postUpdateVideo.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postHapusData(noVideo: String){
        viewModelScope.launch(Dispatchers.IO) {
            _postHapusVideo.postValue(UIState.Loading)
            try {
                val postTambahVideo = api.postAdminHapusVideo("", noVideo)
                _postHapusVideo.postValue(UIState.Success(postTambahVideo))
            } catch (ex: Exception){
                _postHapusVideo.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getDataVideo(): LiveData<UIState<ArrayList<VideoModel>>> = _video
    fun getResponseTambahVideo(): LiveData<UIState<ArrayList<ResponseModel>>> = _postTambahVideo
    fun getResponseUpdateVideo(): LiveData<UIState<ArrayList<ResponseModel>>> = _postUpdateVideo
    fun getResponseHapusVideo(): LiveData<UIState<ArrayList<ResponseModel>>> = _postHapusVideo
}