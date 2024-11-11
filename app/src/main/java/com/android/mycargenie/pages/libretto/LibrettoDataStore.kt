package com.android.mycargenie.pages.libretto

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class CarProfile(
    var brand: String,
    var model: String,
    var displacement: Int,
    var power: Float,
    var horsepower: Float,
    var savedImagePath: String,
    var type: String,
    var fuel: String,
    var year: Int,
    var eco: String,
    var conf: String
)

val Context.dataStore by preferencesDataStore(name = "car_profile")

// Profile fields
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

// Save profile
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

// Read profile
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