package com.example.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.read_materi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.example.aplikasibukudigitalkewirausahaanbagipemula.data.model.MateriModel
import com.example.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadMateriViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private val _materi = MutableLiveData<UIState<ArrayList<MateriModel>>>()

    fun fetchMateri(idMateri: String) {
        viewModelScope.launch {
            _materi.postValue(UIState.Loading)
            delay(1_000)
            try {
                val data = api.getMateri("", idMateri)
                _materi.postValue(UIState.Success(data))
            } catch (ex: Exception) {
                _materi.postValue(UIState.Failure("Error ${ex.message}"))
            }
        }
    }

    fun getMateri(): LiveData<UIState<ArrayList<MateriModel>>> = _materi
}