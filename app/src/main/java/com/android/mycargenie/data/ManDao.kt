package com.android.mycargenie.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ManDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMan(man: Man)

    @Delete
    suspend fun deleteMan(man: Man)

    @Query("SELECT * FROM man ORDER BY substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) DESC")
    fun getManOrderedByDate(): Flow<List<Man>>


    @Query("SELECT * FROM man ORDER BY id DESC")
    fun getManOrderedByDateAdded(): Flow<List<Man>>

    @Query("SELECT MAX(id) FROM man")
    fun getLastInsertedId(): Flow<Int?>

}