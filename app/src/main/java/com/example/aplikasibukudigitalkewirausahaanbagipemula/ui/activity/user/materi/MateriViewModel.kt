package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.materi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriListModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MateriViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _materi = MutableLiveData<UIState<ArrayList<MateriListModel>>>()

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

    fun postWatchMateri(noMateri: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                api.postWatchMateri("", noMateri)
            } catch (ex: Exception){

            }
        }
    }

    fun getDataMateri(): LiveData<UIState<ArrayList<MateriListModel>>> = _materi

}