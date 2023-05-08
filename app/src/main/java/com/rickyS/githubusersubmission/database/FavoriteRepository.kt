package com.rickyS.githubusersubmission.database

import android.app.Application
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val rFavoriteDAO: FavoriteDAO
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        rFavoriteDAO = db.favoriteDAO()
    }

    fun getAllFav() = rFavoriteDAO.getAllFavorite()
    fun checkUserFav(login:String) = rFavoriteDAO.getFavoriteByLogin(login)

    fun insert (favorite: favorite){
        executorService.execute{ rFavoriteDAO.insert(favorite)}
    }

    fun delete (rFavorite: String){
        executorService.execute{ rFavoriteDAO.delete(rFavorite)}
    }
}