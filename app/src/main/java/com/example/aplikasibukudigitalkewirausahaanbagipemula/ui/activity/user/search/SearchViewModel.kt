package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriListModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _materi = MutableLiveData<UIState<ArrayList<MateriListModel>>>()
    val _video = MutableLiveData<UIState<ArrayList<VideoModel>>>()

    fun fetchDataMateri(){
        viewModelScope.launch {
            _materi.postValue(UIState.Loading)
            try {
                val dataMateri = api.getListMateri("")
                _materi.postValue(UIState.Success(dataMateri))
            } catch (ex: Exception){
                _materi.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun fetchDataVideo(){
        viewModelScope.launch {
            _video.postValue(UIState.Loading)
            try {
                val dataVideo = api.getVideo("")
                _video.postValue(UIState.Success(dataVideo))
            } catch (ex: Exception){
                _video.postValue(UIState.Failure("Error ${ex.message}"))
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

    fun postWatchVideo(noVideo: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                api.postWatchVideo("", noVideo)
            } catch (ex: Exception){

            }
        }
    }

    fun getDataMateri(): LiveData<UIState<ArrayList<MateriListModel>>> = _materi
    fun getDataVideo(): LiveData<UIState<ArrayList<VideoModel>>> = _video
}