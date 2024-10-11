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

    @Query("SELECT * FROM rif ORDER BY substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) DESC")
    fun getRifOrderedByDate(): Flow<List<Rif>>


    @Query("SELECT * FROM rif ORDER BY id DESC")
    fun getRifOrderedByDateAdded(): Flow<List<Rif>>

    @Query("SELECT MAX(id) FROM rif")
    fun getLastInsertedId(): Flow<Int?>

}