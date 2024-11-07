package com.android.mycargenie.pages.rifornimento

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mycargenie.data.Rif
import com.android.mycargenie.data.RifDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RifViewModel(
    private val dao: RifDao
) : ViewModel() {

    /*
    The viewmodel code base has been created with the use of AI and then customized and optimized.
    The performance optimization to load elements in RifScreen when required has been made through the use of AI.
    */

    private val _lastInsertedId = MutableStateFlow<Int?>(null)
    private val _state = MutableStateFlow(RifState())
    private val isSortedByDateAdded = MutableStateFlow(true)
    private val _rifs = MutableStateFlow<List<Rif>>(emptyList())

    private val pageSize = 10
    private var currentPage = 0
    private var isLoading = false

    val state = combine(
        _state,
        isSortedByDateAdded,
        _rifs
    ) { state, _, rifs ->
        state.copy(
            rifs = rifs
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RifState())

    init {
        viewModelScope.launch {
            dao.getLastInsertedId().collect { lastId ->
                println("Ultimo ID inserito: $lastId")
                _lastInsertedId.value = lastId
            }
        }
        loadMoreRifs()
    }

    fun loadMoreRifs() {
        // Evita richieste multiple contemporaneamente
        if (isLoading) return

        isLoading = true
        _state.update { it.copy(isLoading = true) }

        println("Caricamento pagina: $currentPage, Offset: ${currentPage * pageSize}")

        viewModelScope.launch {
            val newRifs = if (isSortedByDateAdded.value) {
                dao.getRifPaginatedOrderedByDate(currentPage * pageSize, pageSize)
            } else {
                dao.getRifPaginatedOrderedByDateAdded(currentPage * pageSize, pageSize)
            }

            if (newRifs.isEmpty()) {
                println("Nessun dato da caricare.")
                isLoading = false
                _state.update { it.copy(isLoading = false) }
                return@launch
            }

            // Aggiungi nuovi dati alla lista esistente
            _rifs.update { currentList -> currentList + newRifs }

            // Incrementa il contatore della pagina
            currentPage++
            println("Nuovi rifornimenti caricati: ${newRifs.size}, Totale: ${_rifs.value.size}")

            // Termina il caricamento
            isLoading = false
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onEvent(event: RifEvent) {
        when (event) {
            is RifEvent.DeleteRif -> {
                viewModelScope.launch {
                    dao.deleteRif(event.rif)
                    // Dopo la cancellazione, aggiorniamo la lista rimuovendo l'elemento
                    _rifs.update { currentList -> currentList.filter { it.id != event.rif.id } }
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
                    // Aggiungi il nuovo rifornimento alla lista esistente
                    _rifs.update { currentList -> currentList + rif }
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
                    // Dopo l'aggiornamento, sostituire il vecchio elemento nella lista
                    _rifs.update { currentList ->
                        currentList.map { if (it.id == rif.id) rif else it }
                    }
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
                // Gestisci l'ordinamento e ricarica i dati
                isSortedByDateAdded.value = !isSortedByDateAdded.value
                currentPage = 0
                _rifs.value = emptyList()  // Svuota la lista e ricarica
                loadMoreRifs()
            }
        }
    }
}
