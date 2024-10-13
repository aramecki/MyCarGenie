package com.android.mycargenie.pages.rifornimento

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mycargenie.data.Rif
import com.android.mycargenie.data.RifDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RifViewModel(
    private val dao: RifDao
) : ViewModel() {

    private val _lastInsertedId = MutableStateFlow<Int?>(null)
    val lastInsertedId: StateFlow<Int?> = _lastInsertedId

    init {
        viewModelScope.launch {
            dao.getLastInsertedId().collect { lastId ->
                println("Ultimo ID inserito: $lastId")  // Aggiungi questo log
                _lastInsertedId.value = lastId
            }
        }
    }

    private val isSortedByDateAdded = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val rifs = isSortedByDateAdded.flatMapLatest { sort: Boolean ->
        if (sort) {
            dao.getRifOrderedByDate()
        } else {
            dao.getRifOrderedByDateAdded()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(RifState())
    val state = combine(
        _state,
        isSortedByDateAdded,
        rifs
    ) { state: RifState, _: Boolean, rif: List<Rif> ->
        state.copy(
            rif = rif
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RifState())

    fun onEvent(event: RifEvent) {
        when (event) {
            is RifEvent.DeleteRif -> {
                viewModelScope.launch {
                    dao.deleteRif(event.rif)
                }
            }

            is RifEvent.SaveRif -> {
                val rif = Rif(
                    type = state.value.type.value,
                    place = state.value.place.value,
                    price = state.value.price.value,
                    uvalue = state.value.uvalue.value,
                    totunit = state.value.totunit.value,
                    date = state.value.date.value,
                    note = state.value.note.value,
                    kmt = state.value.kmt.value,

                )

                viewModelScope.launch {
                    dao.insertRif(rif)
                }

                _state.update {
                    it.copy(
                        type = mutableStateOf(""),
                        place = mutableStateOf(""),
                        price = mutableDoubleStateOf(0.0),
                        uvalue = mutableDoubleStateOf(0.0),
                        totunit = mutableDoubleStateOf(0.0),
                        date = mutableStateOf(""),
                        note = mutableStateOf(""),
                        kmt = mutableIntStateOf(0),
                        )
                }
            }

            is RifEvent.UpdateRif -> {
                val rif = Rif(
                    id = state.value.id.value,
                    type = state.value.type.value,
                    place = state.value.place.value,
                    price = state.value.price.value,
                    uvalue = state.value.uvalue.value,
                    totunit = state.value.totunit.value,
                    date = state.value.date.value,
                    note = state.value.note.value,
                    kmt = state.value.kmt.value,
                )

                viewModelScope.launch {
                    dao.updateRif(rif)
                }

                _state.update {
                    it.copy(
                        id = mutableIntStateOf(0),
                        type = mutableStateOf(""),
                        place = mutableStateOf(""),
                        price = mutableDoubleStateOf(0.0),
                        uvalue = mutableDoubleStateOf(0.0),
                        totunit = mutableDoubleStateOf(0.0),
                        date = mutableStateOf(""),
                        note = mutableStateOf(""),
                        kmt = mutableIntStateOf(0),
                    )
                }
            }

            is RifEvent.SortRif -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
        }
    }
}