package com.android.mycargenie.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rif(

    val type: String,
    val place: String,
    val price: Double,
    val uvalue: Double,
    val totunit: Double,
    val date: String,
    val note: String,
    val kmt: Int,


    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)