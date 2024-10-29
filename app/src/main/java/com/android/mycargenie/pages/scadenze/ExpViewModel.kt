package com.android.mycargenie.pages.scadenze

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpirationsViewModel(private val application: Application) : ViewModel() {
    private val _expSettings = MutableStateFlow(Expirations(assstart = "", assend = "", assdues = 0 , assprice = 0.0f, assplace = "", taxdate = "", taxprice = 0.0f, revlast = "", revnext = "", revplace = ""))
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