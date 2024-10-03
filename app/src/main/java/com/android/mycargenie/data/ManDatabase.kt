package com.android.mycargenie.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Man::class],
    version = 1
)
abstract class ManDatabase: RoomDatabase(){
    abstract fun dao(): ManDao
}