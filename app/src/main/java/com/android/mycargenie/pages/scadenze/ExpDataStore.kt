package com.android.mycargenie.pages.scadenze

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Expirations(
    var assstart: String,
    var assend: String,
    var assdues: Int,
    var assprice: Float,
    var assplace: String,
    var taxdate: String,
    var taxprice: Float,
    var revlast: String,
    var revnext: String,
    var revplace: String
)

val Context.dataStore by preferencesDataStore(name = "exp_settings")

// Campi delle scadenze
private val ASS_START_KEY = stringPreferencesKey("assstart")
private val ASS_END_KEY = stringPreferencesKey("assend")
private val ASS_DUES_KEY = intPreferencesKey("assdues")
private val ASS_PRICE_KEY = floatPreferencesKey("assprice")
private val ASS_PLACE_KEY = stringPreferencesKey("assplace")
private val TAX_DATE_KEY = stringPreferencesKey("taxdate")
private val TAX_PRICE_KEY = floatPreferencesKey("taxprice")
private val REV_LAST_KEY = stringPreferencesKey("revlast")
private val REV_NEXT_KEY = stringPreferencesKey("revnext")
private val REV_PLACE = stringPreferencesKey("revplace")



// Salvare il profilo
suspend fun saveExpSettings(context: Context, expirations: Expirations) {
    context.dataStore.edit { preferences ->
        preferences[ASS_START_KEY] = expirations.assstart
        preferences[ASS_END_KEY] = expirations.assend
        preferences[ASS_DUES_KEY] = expirations.assdues
        preferences[ASS_PRICE_KEY] = expirations.assprice
        preferences[ASS_PLACE_KEY] = expirations.assplace
        preferences[TAX_DATE_KEY] = expirations.taxdate
        preferences[TAX_PRICE_KEY] = expirations.taxprice
        preferences[REV_LAST_KEY] = expirations.revlast
        preferences[REV_NEXT_KEY] = expirations.revnext
        preferences[REV_PLACE] = expirations.revplace
    }
}

// Leggere il progilo
fun getExpSettings(context: Context): Flow<Expirations> {
    return context.dataStore.data.map { preferences ->
        Expirations(
            assstart = preferences[ASS_START_KEY] ?: "",
            assend = preferences[ASS_END_KEY] ?: "",
            assdues = preferences[ASS_DUES_KEY] ?: 0,
            assprice = preferences[ASS_PRICE_KEY] ?: 0.0f,
            assplace = preferences[ASS_PLACE_KEY] ?: "",
            taxdate = preferences[TAX_DATE_KEY] ?: "",
            taxprice = preferences[TAX_PRICE_KEY] ?: 0.0f,
            revlast = preferences[REV_LAST_KEY] ?: "",
            revnext = preferences[REV_NEXT_KEY] ?: "",
            revplace = preferences[REV_PLACE] ?: ""
        )
    }
}