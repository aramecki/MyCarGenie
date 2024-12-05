package com.android.mycargenie.pages.scadenze

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpirationsViewModel(private val application: Application) : ViewModel() {
    private val _expSettings = MutableStateFlow(Expirations(inscheck = false, insstart = "", insend = "", insdues = 0 , insprice = 0.0f, insplace = "", insnot = false, taxcheck = false, taxdate = "", taxprice = 0.0f, taxnot = false, revcheck = false, revlast = "", revnext = "", revplace = "", revnot = false))
    val expSettings: StateFlow<Expirations> = _expSettings

    init {
        viewModelScope.launch {
            getExpSettings(application).collect { profile ->
                _expSettings.value = profile
            }
        }
    }

    fun updateExpSettings(updatedExpSettings: Expirations) {
        _expSettings.value = updatedExpSettings
        viewModelScope.launch {
            saveExpSettings(application, updatedExpSettings)
        }
    }
}