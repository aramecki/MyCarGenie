package com.android.mycargenie.pages.manutenzione

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mycargenie.data.Man
import com.android.mycargenie.data.ManDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManViewModel(
    private val dao: ManDao
) : ViewModel() {

    private val isSortedByDateAdded = MutableStateFlow(true)

    private var mans =
        isSortedByDateAdded.flatMapLatest { sort ->
            if (sort) {
                dao.getManOrderByDate()
            } else {
                dao.getManOrderByDateAdded()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _state = MutableStateFlow(ManState())
    val state =
        combine(_state, isSortedByDateAdded, mans) { state, isSortedByDateAdded, mans ->
            state.copy(
                men = mans
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ManState())

    fun onEvent(event: ManEvent) {
        when (event) {
            is ManEvent.DeleteMan -> {
                viewModelScope.launch {
                    dao.deleteMan(event.man)
                }
            }

            is ManEvent.SaveMan -> {
                val man = Man(
                    title = state.value.title.value,
                    date = state.value.date.value,
                    place = state.value.place.value,
                    description = state.value.description.value,
                    dateAdded = System.currentTimeMillis()
                )

                viewModelScope.launch {
                    dao.upsertMan(man)
                }

                _state.update {
                    it.copy(
                        title = mutableStateOf(""),
                        date = mutableStateOf(""),
                        place = mutableStateOf(""),
                        description = mutableStateOf("")
                    )
                }
            }

            ManEvent.SortMan -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
        }
    }

}