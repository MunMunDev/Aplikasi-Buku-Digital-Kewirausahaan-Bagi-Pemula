package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    val _video = MutableLiveData<UIState<ArrayList<VideoModel>>>()

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

    fun postWatchVideo(noVideo: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                api.postWatchVideo("", noVideo)
            } catch (ex: Exception){

            }
        }
    }

    fun getDataVideo(): LiveData<UIState<ArrayList<VideoModel>>> = _video
}