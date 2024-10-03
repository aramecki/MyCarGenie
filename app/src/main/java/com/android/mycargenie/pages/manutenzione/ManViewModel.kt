package com.android.mycargenie.pages.manutenzione

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mycargenie.data.Man
import com.android.mycargenie.data.ManDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val mans = isSortedByDateAdded.flatMapLatest { sort: Boolean ->
        if (sort) {
            dao.getManOrderedByDate() // Make sure method name is correct
        } else {
            dao.getManOrderedByDateAdded() // Make sure method name is correct
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ManState())
    val state = combine(
        _state,
        isSortedByDateAdded,
        mans
    ) { state: ManState, _: Boolean, men: List<Man> ->
        state.copy(
            men = men
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
                    type = state.value.type.value,
                    place = state.value.place.value,
                    date = state.value.date.value,
                    kmt = state.value.kmt.value,
                    description = state.value.description.value,
                    price = state.value.price.value,
                    dateAdded = System.currentTimeMillis()
                )

                viewModelScope.launch {
                    dao.upsertMan(man)
                }

                _state.update {
                    it.copy(
                        title = mutableStateOf(""),
                        type = mutableStateOf(""),
                        place = mutableStateOf(""),
                        date = mutableStateOf(""),
                        kmt = mutableIntStateOf(0),
                        description = mutableStateOf(""),
                        price = mutableDoubleStateOf(0.0)
                    )
                }
            }

            is ManEvent.SortMan -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
        }
    }
}