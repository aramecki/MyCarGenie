package com.android.mycargenie.pages.rifornimento

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.android.mycargenie.data.Rif

data class RifState(

    val rifs: List<Rif> = emptyList(),
    val id: MutableState<Int>  = mutableIntStateOf(0),
    val type: MutableState<String> = mutableStateOf(""),
    val place: MutableState<String> = mutableStateOf(""),
    val price: MutableState<Double> = mutableDoubleStateOf(0.0),
    val uvalue: MutableState<Double> = mutableDoubleStateOf(0.0),
    val totunit: MutableState<Double> = mutableDoubleStateOf(0.0),
    val date: MutableState<String> = mutableStateOf(""),
    val note: MutableState<String> = mutableStateOf(""),
    val kmt: MutableState<Int> = mutableIntStateOf(0),

)