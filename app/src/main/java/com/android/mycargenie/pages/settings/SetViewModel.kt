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
    private val _carProfile = MutableStateFlow(CarProfile(brand = "", model = "", displacement = 0 , power = 0.0f, horsepower = 0.0f, type = "", fuel = "", year = 0, eco = "", conf = ""))
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
            saveCarProfile(application, updatedProfile)
        }
    }
}