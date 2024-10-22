package com.example.playlistmaker2.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker2.media.data.db.entity.FavouritesMediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesMediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouritesMediaEntity(track: FavouritesMediaEntity)

    @Query("SELECT * FROM favourites_media_table ORDER BY dataSort DESC")
    fun getFavouritesMediaEntity(): Flow<List<FavouritesMediaEntity>>

    @Delete(entity = FavouritesMediaEntity::class)
    suspend fun deleteFavouritesMediaEntity(track: FavouritesMediaEntity)

    @Query("SELECT media_id FROM favourites_media_table ORDER BY media_id DESC")
    fun searchMoviesById(): List<String>

}