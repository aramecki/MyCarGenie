package com.android.mycargenie.pages.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mycargenie.pages.profile.CarProfile
import com.android.mycargenie.shared.getCarProfile
import com.android.mycargenie.shared.saveCarProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SetViewModel(private val application: Application) : ViewModel() {
    private val _carProfile = MutableStateFlow(CarProfile("Subaru", "Baracca", "2.0", "170kW", "230CV"))
    val carProfile: StateFlow<CarProfile> = _carProfile

    init {
        viewModelScope.launch {
            getCarProfile(application).collect { profile ->
                _carProfile.value = profile
            }
        }
    }

    fun updateCarProfile(updatedProfile: CarProfile) {
        _carProfile.value = updatedProfile
        viewModelScope.launch {
            // Salva il profilo aggiornato
            saveCarProfile(application, updatedProfile)
        }
    }
}