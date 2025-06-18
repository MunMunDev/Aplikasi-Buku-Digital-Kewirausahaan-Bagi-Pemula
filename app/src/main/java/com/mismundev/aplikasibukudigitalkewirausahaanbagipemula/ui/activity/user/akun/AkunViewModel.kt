package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.user.akun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AkunViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    var _responsePostUpdateUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun postUpdateUser(
        idUser:String, nama: String, nomorHp: String,
        username: String, password: String, usernameLama:String
    ){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostUpdateUser.postValue(UIState.Loading)
            try {
                val data = api.postUpdateUser("", idUser, nama, nomorHp, username, password, usernameLama)
                _responsePostUpdateUser.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateUser.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getUpdateData(): LiveData<UIState<ArrayList<ResponseModel>>> = _responsePostUpdateUser
}