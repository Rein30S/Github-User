package com.rickyS.githubusersubmission.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import javax.xml.parsers.FactoryConfigurationError

@Dao
interface FavoriteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (favorite: favorite)

    @Query("DELETE FROM favorite WHERE login = :login")
    abstract fun delete (login: String)

    @Query("SELECT * FROM favorite ORDER BY login ASC")
    fun getAllFavorite(): LiveData<List<favorite>>

    @Query("SELECT * FROM favorite WHERE login = :login")
    fun getFavoriteByLogin(login: String): List<favorite>
}