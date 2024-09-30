package com.android.mycargenie.pages.manutenzione

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.android.mycargenie.data.Man

data class ManState(

    val men: List<Man> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val date: MutableState<String> = mutableStateOf(""),
    val description: MutableState<String> = mutableStateOf("")

)