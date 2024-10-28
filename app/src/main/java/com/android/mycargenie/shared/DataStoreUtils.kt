package com.android.mycargenie.shared

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android.mycargenie.pages.profile.CarProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "car_profile")

// Campi del profilo
private val BRAND_KEY = stringPreferencesKey("brand")
private val MODEL_KEY = stringPreferencesKey("model")
private val DISPLACEMENT_KEY = intPreferencesKey("displacement")
private val POWER_KEY = floatPreferencesKey("power")
private val HORSEPOWER_KEY = floatPreferencesKey("horsepower")
private val SAVEDIMAGE_URI_KEY = stringPreferencesKey("savedimagepath")
private val TYPE_KEY = stringPreferencesKey("type")
private val FUEL_KEY = stringPreferencesKey("fuel")
private val YEAR_KEY = intPreferencesKey("year")
private val ECO_KEY = stringPreferencesKey("eco")
private val CONF_KEY = stringPreferencesKey("conf")

// Salvare il profilo
suspend fun saveCarProfile(context: Context, carProfile: CarProfile) {
    context.dataStore.edit { preferences ->
        preferences[BRAND_KEY] = carProfile.brand
        preferences[MODEL_KEY] = carProfile.model
        preferences[DISPLACEMENT_KEY] = carProfile.displacement
        preferences[POWER_KEY] = carProfile.power
        preferences[HORSEPOWER_KEY] = carProfile.horsepower
        preferences[SAVEDIMAGE_URI_KEY] = carProfile.savedImagePath
        preferences[TYPE_KEY] = carProfile.type
        preferences[FUEL_KEY] = carProfile.fuel
        preferences[YEAR_KEY] = carProfile.year
        preferences[ECO_KEY] = carProfile.eco
        preferences[CONF_KEY] = carProfile.conf
    }
}

// Leggere il progilo
fun getCarProfile(context: Context): Flow<CarProfile> {
    return context.dataStore.data.map { preferences ->
        CarProfile(
            brand = preferences[BRAND_KEY] ?: "",
            model = preferences[MODEL_KEY] ?: "",
            displacement = preferences[DISPLACEMENT_KEY] ?: 0,
            power = preferences[POWER_KEY] ?: 0.0f,
            horsepower = preferences[HORSEPOWER_KEY] ?: 0.0f,
            savedImagePath = preferences[SAVEDIMAGE_URI_KEY] ?: "",
            type = preferences[TYPE_KEY] ?: "",
            fuel = preferences[FUEL_KEY] ?: "",
            year = preferences[YEAR_KEY] ?: 0,
            eco = preferences[ECO_KEY] ?: "",
            conf = preferences[CONF_KEY] ?: ""
        )
    }
}