package com.example.androidlabs.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteRepoDao {
    @Query("SELECT * FROM favorite_repos")
    fun getAllFavorites(): Flow<List<FavoriteRepoEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_repos WHERE id = :repoId)")
    fun isFavorite(repoId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(repo: FavoriteRepoEntity)

    @Query("DELETE FROM favorite_repos WHERE id = :repoId")
    suspend fun deleteFavoriteById(repoId: Long)
}