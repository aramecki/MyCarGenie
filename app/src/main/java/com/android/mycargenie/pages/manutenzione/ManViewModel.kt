package com.android.mycargenie.pages.manutenzione

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mycargenie.data.Man
import com.android.mycargenie.data.ManDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManViewModel(
    private val dao: ManDao
) : ViewModel() {

    /*
    The viewmodel code bas has been created with the use of AI and then customized and optimized.
    The performance optimization to load elements in ManScreen when required has ben made through the use of AI.
     */

    private val _lastInsertedId = MutableStateFlow<Int?>(null)

    private val _state = MutableStateFlow(ManState())
    private val isSortedByDateAdded = MutableStateFlow(true)
    private val _mans = MutableStateFlow<List<Man>>(emptyList())

    private val pageSize = 10
    private var currentPage = 0
    private var isLoading = false

    val state = combine(
        _state,
        isSortedByDateAdded,
        _mans
    ) { state, _, men ->
        state.copy(
            men = men
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ManState())

    init {
        viewModelScope.launch {
            dao.getLastInsertedId().collect { lastId ->
                println("Ultimo ID inserito: $lastId")
                _lastInsertedId.value = lastId
            }
        }
        loadMoreMen()
    }

    fun loadMoreMen() {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true
            _state.update { it.copy(isLoading = true) }

            println("Caricamento pagina: $currentPage, Offset: ${currentPage * pageSize}")

            val newMen = if (isSortedByDateAdded.value) {
                dao.getMenPaginatedOrderedByDate(currentPage * pageSize, pageSize)
            } else {
                dao.getMenPaginatedOrderedByDateAdded(currentPage * pageSize, pageSize)
            }

            if (newMen.isEmpty()) {
                println("Nessun dato da caricare.")
                isLoading = false
                _state.update { it.copy(isLoading = false) }
                return@launch
            }

            _mans.update { currentList -> currentList + newMen }
            currentPage++

            println("Nuovi elementi caricati: ${newMen.size}, Totale: ${_mans.value.size}")

            isLoading = false
            _state.update { it.copy(isLoading = false) }
        }
    }


    fun onEvent(event: ManEvent) {
        when (event) {
            is ManEvent.DeleteMan -> {
                viewModelScope.launch {
                    dao.deleteMan(event.man)
                    _mans.update { currentList -> currentList.filter { it.id != event.man.id } }
                }
            }

            is ManEvent.SaveMan -> {
                val man = Man(
                    title = _state.value.title.value,
                    type = _state.value.type.value,
                    place = _state.value.place.value,
                    date = _state.value.date.value,
                    kmt = _state.value.kmt.value,
                    description = _state.value.description.value,
                    price = _state.value.price.value,
                )

                viewModelScope.launch {
                    dao.insertMan(man)

                    val updatedMenList = dao.getMenPaginatedOrderedByDate(0, (currentPage + 1) * pageSize)
                    _mans.update { updatedMenList }

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
            }

            is ManEvent.UpdateMan -> {
                val man = Man(
                    id = _state.value.id.value,
                    title = _state.value.title.value,
                    type = _state.value.type.value,
                    place = _state.value.place.value,
                    date = _state.value.date.value,
                    kmt = _state.value.kmt.value,
                    description = _state.value.description.value,
                    price = _state.value.price.value,
                )

                viewModelScope.launch {
                    dao.updateMan(man)

                    val updatedMenList =
                        dao.getMenPaginatedOrderedByDate(0, (currentPage + 1) * pageSize)
                    _mans.update { updatedMenList }


                    _state.update {
                        it.copy(
                            id = mutableIntStateOf(0),
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
            }


            is ManEvent.SortMan -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
                currentPage = 0
                _mans.value = emptyList()
                loadMoreMen()
            }
        }
    }
}

