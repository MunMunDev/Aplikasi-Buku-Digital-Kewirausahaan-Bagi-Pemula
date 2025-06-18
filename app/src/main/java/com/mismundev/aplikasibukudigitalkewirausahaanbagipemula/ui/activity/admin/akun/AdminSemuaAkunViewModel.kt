package com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.ui.activity.admin.akun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.database.api.ApiService
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.ResponseModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.data.model.UsersModel
import com.mismundev.aplikasibukudigitalkewirausahaanbagipemula.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminSemuaAkunViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    var _user = MutableLiveData<UIState<ArrayList<UsersModel>>>()
    var _responsePostTambahUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    var _responsePostUpdateUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    var _responsePostHapusUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchDataUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(UIState.Loading)
            delay(1_000)
            try {
                val dataUser = api.getAllUser("")
                _user.postValue(UIState.Success(dataUser))
            } catch (ex: Exception){
                _user.postValue(UIState.Failure("Error User: ${ex.message}"))
            }
        }
    }

    fun postTambahUser(nama: String, nomorHp: String, username: String, password: String, sebagai:String){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostTambahUser.postValue(UIState.Loading)
            try {
                val data = api.addUser("", nama, nomorHp, username, password, sebagai)
                _responsePostTambahUser.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostTambahUser.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateUser(
        idUser:String, nama: String, nomorHp: String, username: String,
        password: String, usernameLama: String
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

    fun postHapusUser(idUser: String){
        viewModelScope.launch(Dispatchers.IO){
            _responsePostHapusUser.postValue(UIState.Loading)
            try {
                val hapusUser = api.postHapusUser("", idUser)
                _responsePostHapusUser.postValue(UIState.Success(hapusUser))
            } catch (ex: Exception){
                _responsePostHapusUser.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getDataUsers(): LiveData<UIState<ArrayList<UsersModel>>> = _user
    fun getTambahData(): LiveData<UIState<ArrayList<ResponseModel>>> = _responsePostTambahUser
    fun getUpdateData(): LiveData<UIState<ArrayList<ResponseModel>>> = _responsePostUpdateUser
    fun getHapusUser(): LiveData<UIState<ArrayList<ResponseModel>>> = _responsePostHapusUser
}