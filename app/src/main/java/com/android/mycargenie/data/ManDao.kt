package com.android.mycargenie.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ManDao {

    @Insert
    suspend fun insertMan(man: Man)

    @Update
    suspend fun updateMan(man: Man)

    @Delete
    suspend fun deleteMan(man: Man)

    @Query("SELECT MAX(id) FROM man")
    fun getLastInsertedId(): Flow<Int?>

    @Query("SELECT * FROM man ORDER BY substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) DESC, id DESC LIMIT :limit OFFSET :offset")
    suspend fun getMenPaginatedOrderedByDate(offset: Int, limit: Int): List<Man>

    @Query("SELECT * FROM man ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getMenPaginatedOrderedByDateAdded(offset: Int, limit: Int): List<Man>

    @Query("SELECT * FROM man WHERE date <= :today ORDER BY date DESC, id DESC LIMIT 1")
    fun getLastManBeforeToday(today: String): Flow<Man?>

}