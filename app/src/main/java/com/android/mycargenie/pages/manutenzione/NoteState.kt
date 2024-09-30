package com.android.mycargenie.pages.manutenzione

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.android.mycargenie.Data.Note

data class NoteState(

    val notes: List<Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val description: MutableState<String> = mutableStateOf("")

)