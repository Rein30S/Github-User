package com.rickyS.githubusersubmission.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rickyS.githubusersubmission.api.ApiConfig
import com.rickyS.githubusersubmission.api.DetailGitHubResponse
import com.rickyS.githubusersubmission.api.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailGitHubViewModel: ViewModel() {

    private var _detailUser = MutableLiveData<DetailGitHubResponse>()
    val detailUser: LiveData<DetailGitHubResponse> = _detailUser

    private val _user = MutableLiveData<List<ItemsItem>>()
    val user: LiveData<List<ItemsItem>> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setDetailUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailGitHubResponse> {
            override fun onResponse(
                call: Call<DetailGitHubResponse>,
                response: Response<DetailGitHubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailGitHubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getFollowerUser(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowerUser(user)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, "Response : ${response.body()}")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _user.value = responseBody
                    }
                } else {
                    Log.d(TAG, "onFailure: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowingUser(user: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowingUser(user)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    Log.d(TAG, "Response : ${response.body()}")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _user.value = responseBody
                    }
                } else {
                    Log.d(TAG, "onFailure: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}