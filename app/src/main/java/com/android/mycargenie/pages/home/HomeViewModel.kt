package com.android.mycargenie.pages.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mycargenie.data.Man
import com.android.mycargenie.data.ManDao
import com.android.mycargenie.data.Rif
import com.android.mycargenie.data.RifDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(
    private val manDao: ManDao,
    private val rifDao: RifDao
) : ViewModel() {

    private val _lastManutenzione = MutableStateFlow<Man?>(null)
    val lastManutenzione: StateFlow<Man?> = _lastManutenzione

    private val _lastRifornimento = MutableStateFlow<Rif?>(null)
    val lastRifornimento: StateFlow<Rif?> = _lastRifornimento

    init {
        observeLatestEntries()
    }

    private fun observeLatestEntries() {
        val today = getCurrentDateString()

        // Osserva gli aggiornamenti di Manutenzione
        viewModelScope.launch {
            manDao.getLastManBeforeToday(today)
                .distinctUntilChanged()  // Evita aggiornamenti inutili
                .collect { man ->
                    _lastManutenzione.value = man
                }
        }

        // Osserva gli aggiornamenti di Rifornimento
        viewModelScope.launch {
            rifDao.getLastRifBeforeToday(today)
                .distinctUntilChanged()  // Evita aggiornamenti inutili
                .collect { rif ->
                    _lastRifornimento.value = rif
                }
        }
    }

    private fun getCurrentDateString(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date())
    }
}