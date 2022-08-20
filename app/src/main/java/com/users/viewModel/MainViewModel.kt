package com.users.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.users.Api.ApiService
import com.users.database.UserDao
import com.users.model.Data
import com.users.model.UserResponse
import com.users.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val apiService: ApiService) : ViewModel() {

    val errorData = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val userData = MutableLiveData<UserResponse>()
    val userList = MutableLiveData<List<Data>>()

    fun getUsers() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiService.getUsers(20)
            isLoading.postValue(false)
            if (response.isSuccessful) {
                if (response.code() == Constants.SUCCESS_RESPONSE) {
                    userData.postValue(response.body())
                } else {
                    errorData.postValue(response.errorBody().toString())
                }
            } else {
                errorData.postValue(response.errorBody().toString())
            }
        }
    }

    fun updateDatabase(userDao: UserDao, userResponse: UserResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.addUser(userResponse.data)
        }
    }

    fun getUserList(userDao: UserDao): LiveData<List<Data>> {
        viewModelScope.launch(Dispatchers.IO) {
            userList.postValue(userDao.getAllUsers())
        }
        return userList
    }
}