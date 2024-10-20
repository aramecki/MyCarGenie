package com.android.mycargenie.shared

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android.mycargenie.pages.profile.CarProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Estensione per ottenere un'istanza di DataStore
val Context.dataStore by preferencesDataStore(name = "car_profile")

// Chiavi per i campi di CarProfile
private val BRAND_KEY = stringPreferencesKey("brand")
private val MODEL_KEY = stringPreferencesKey("model")
private val ENGINE_KEY = stringPreferencesKey("engine")
private val POWER_KEY = stringPreferencesKey("power")
private val HORSEPOWER_KEY = stringPreferencesKey("horsepower")

// Funzione per salvare il profilo
suspend fun saveCarProfile(context: Context, carProfile: CarProfile) {
    context.dataStore.edit { preferences ->
        preferences[BRAND_KEY] = carProfile.brand
        preferences[MODEL_KEY] = carProfile.model
        preferences[ENGINE_KEY] = carProfile.engine
        preferences[POWER_KEY] = carProfile.power
        preferences[HORSEPOWER_KEY] = carProfile.horsepower
    }
}

// Funzione per leggere il profilo
fun getCarProfile(context: Context): Flow<CarProfile> {
    return context.dataStore.data.map { preferences ->
        CarProfile(
            brand = preferences[BRAND_KEY] ?: "Subaru",
            model = preferences[MODEL_KEY] ?: "Baracca",
            engine = preferences[ENGINE_KEY] ?: "2.0",
            power = preferences[POWER_KEY] ?: "170kW",
            horsepower = preferences[HORSEPOWER_KEY] ?: "230CV"
        )
    }
}