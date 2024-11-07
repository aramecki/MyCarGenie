package com.android.mycargenie.pages.manutenzione

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.android.mycargenie.data.Man

data class ManState(

    val men: List<Man> = emptyList(),
    val isLoading: Boolean = false,
    val id: MutableState<Int>  = mutableIntStateOf(0),
    val title: MutableState<String> = mutableStateOf(""),
    val type: MutableState<String> = mutableStateOf(""),
    val place: MutableState<String> = mutableStateOf(""),
    val date: MutableState<String> = mutableStateOf(""),
    val kmt: MutableState<Int> = mutableIntStateOf(0),
    val description: MutableState<String> = mutableStateOf(""),
    val price: MutableState<Double> = mutableDoubleStateOf(0.0)

)