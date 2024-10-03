package com.android.mycargenie.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Man(

    val title: String,
    val type: String,
    val place: String,
    val date: String,
    val kmt: Int,
    val description: String,
    val price: Double,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)