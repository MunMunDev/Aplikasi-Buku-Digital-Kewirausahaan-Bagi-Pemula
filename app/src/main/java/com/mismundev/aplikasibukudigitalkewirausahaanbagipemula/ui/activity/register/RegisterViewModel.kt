package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.register

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
class RegisterViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _registerUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun postRegisterUser(nama:String, nomorHp:String, username:String, password:String, sebagai:String){
        viewModelScope.launch(Dispatchers.IO) {
            _registerUser.postValue(UIState.Loading)
            try {
                val registerUser = api.addUser("", nama, nomorHp, username, password, sebagai)
                _registerUser.postValue(UIState.Success(registerUser))
            } catch (ex: Exception){
                _registerUser.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun getRegisterUser(): LiveData<UIState<ArrayList<ResponseModel>>> = _registerUser

}