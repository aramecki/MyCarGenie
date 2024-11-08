package com.android.mycargenie.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RifDao {

    @Insert
    suspend fun insertRif(rif: Rif)

    @Update
    suspend fun updateRif(rif: Rif)

    @Delete
    suspend fun deleteRif(rif: Rif)

    @Query("SELECT MAX(id) FROM rif")
    fun getLastInsertedId(): Flow<Int?>

    @Query("SELECT * FROM rif ORDER BY substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) DESC, id DESC LIMIT :limit OFFSET :offset")
    suspend fun getRifPaginatedOrderedByDate(offset: Int, limit: Int): List<Rif>

    @Query("SELECT * FROM rif ORDER BY id DESC LIMIT :limit OFFSET :offset")
    suspend fun getRifPaginatedOrderedByDateAdded(offset: Int, limit: Int): List<Rif>

    @Query("SELECT * FROM rif WHERE date <= :today ORDER BY date DESC, id DESC LIMIT 1")
    fun getLastRifBeforeToday(today: String): Flow<Rif?>

}