package com.rickyS.githubusersubmission.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [favorite::class], version = 1)
abstract class FavoriteRoomDatabase : RoomDatabase() {
    abstract fun favoriteDAO(): FavoriteDAO

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavoriteRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                            FavoriteRoomDatabase::class.java, "favorite_database"
                        ).build()
                    }
                }
            }
            return INSTANCE as FavoriteRoomDatabase
        }
    }
}