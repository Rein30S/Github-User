package com.rickyS.githubusersubmission.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.rickyS.githubusersubmission.api.*
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _user = MutableLiveData<List<ItemsItem>>()
    val user: LiveData<List<ItemsItem>> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
    }

    init{
        findUser("Ricky")
    }

    fun findUser(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(user)
        client.enqueue(object : Callback<GitHubResponse> {
            override fun onResponse(
                call: Call<GitHubResponse>,
                response: Response<GitHubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {1
                    _user.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GitHubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun searchUser(user: String){
        findUser(user)
    }

}
