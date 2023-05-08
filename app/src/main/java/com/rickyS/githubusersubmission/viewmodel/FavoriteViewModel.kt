package com.rickyS.githubusersubmission.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rickyS.githubusersubmission.database.FavoriteRepository
import com.rickyS.githubusersubmission.database.favorite

class FavoriteViewModel(application: Application) : ViewModel() {

    private val _favoriteRepository: FavoriteRepository = FavoriteRepository(application)
    fun getAllFav() : LiveData<List<favorite>> = _favoriteRepository.getAllFav()

    fun checkUserFav(login:String) = _favoriteRepository.checkUserFav(login)

    fun insert(mUsername: String, mAvatar: String){
        val mFavorite = favorite(mAvatar, mUsername)
        _favoriteRepository.insert(mFavorite)
    }

    fun delete(mUsername: String){
        _favoriteRepository.delete(mUsername)
    }
}