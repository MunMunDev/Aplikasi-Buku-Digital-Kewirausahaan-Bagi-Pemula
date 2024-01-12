package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _materiPopuler = MutableLiveData<UIState<ArrayList<MateriModel>>>()
    val _videoPopuler = MutableLiveData<UIState<ArrayList<VideoModel>>>()

    fun fetchDataMateriPopuler(){
        viewModelScope.launch {
            _materiPopuler.postValue(UIState.Loading)
            try {
                val dataMateri = api.getMateri("")
                _materiPopuler.postValue(UIState.Success(dataMateri))
            } catch (ex: Exception){
                _materiPopuler.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun fetchDataVideoPopuler(){
        viewModelScope.launch {
            _videoPopuler.postValue(UIState.Loading)
            try {
                val dataVideo = api.getVideo("")
                _videoPopuler.postValue(UIState.Success(dataVideo))
            } catch (ex: Exception){
                _videoPopuler.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun getDataMateri(): LiveData<UIState<ArrayList<MateriModel>>> = _materiPopuler
    fun getDataVideo(): LiveData<UIState<ArrayList<VideoModel>>> = _videoPopuler

}