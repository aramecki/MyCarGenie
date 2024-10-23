package com.android.mycargenie.shared

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android.mycargenie.pages.profile.CarProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "car_profile")

// Campi del profilo
private val BRAND_KEY = stringPreferencesKey("brand")
private val MODEL_KEY = stringPreferencesKey("model")
private val ENGINE_KEY = stringPreferencesKey("engine")
private val POWER_KEY = stringPreferencesKey("power")
private val HORSEPOWER_KEY = stringPreferencesKey("horsepower")
private val IMAGE_URI_KEY = stringPreferencesKey("image_uri")

// Salvare il profilo
suspend fun saveCarProfile(context: Context, carProfile: CarProfile) {
    context.dataStore.edit { preferences ->
        preferences[BRAND_KEY] = carProfile.brand
        preferences[MODEL_KEY] = carProfile.model
        preferences[ENGINE_KEY] = carProfile.engine
        preferences[POWER_KEY] = carProfile.power
        preferences[HORSEPOWER_KEY] = carProfile.horsepower
        preferences[IMAGE_URI_KEY] = carProfile.imageUri ?: ""
    }
}

// Leggere il progilo
fun getCarProfile(context: Context): Flow<CarProfile> {
    return context.dataStore.data.map { preferences ->
        CarProfile(
            brand = preferences[BRAND_KEY] ?: "",
            model = preferences[MODEL_KEY] ?: "",
            engine = preferences[ENGINE_KEY] ?: "",
            power = preferences[POWER_KEY] ?: "",
            horsepower = preferences[HORSEPOWER_KEY] ?: "",
            imageUri = preferences[IMAGE_URI_KEY]?.takeIf { it.isNotEmpty() }
        )
    }
}