package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.materi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.VideoModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import kotlinx.coroutines.launch
import javax.inject.Inject

class MateriViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _materi = MutableLiveData<UIState<ArrayList<MateriModel>>>()

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

    fun getDataMateri(): LiveData<UIState<ArrayList<MateriModel>>> = _materi

}