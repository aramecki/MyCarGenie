package com.android.mycargenie.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Rif::class],
    version = 1
)
abstract class RifDatabase: RoomDatabase(){
    abstract fun dao(): RifDao
}