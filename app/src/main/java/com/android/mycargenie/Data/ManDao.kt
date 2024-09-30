package com.android.mycargenie.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ManDao {

    @Upsert
    suspend fun upsertMan(man: Man)

    @Delete
    suspend fun deleteMan(man: Man)

    @Query("SELECT * FROM man ORDER BY dateAdded")
    fun getManOrderdByDateAdded(): Flow<List<Man>>


    @Query("SELECT * FROM man ORDER BY title ASC")
    fun getManOrderdByTitle(): Flow<List<Man>>

}