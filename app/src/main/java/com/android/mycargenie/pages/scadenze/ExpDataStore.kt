package com.android.mycargenie.pages.scadenze

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Expirations(
    var inscheck: Boolean,
    var insstart: String,
    var insend: String,
    var insdues: Int,
    var insprice: Float,
    var insplace: String,
    var insnot: Boolean,
    var taxcheck: Boolean,
    var taxdate: String,
    var taxprice: Float,
    var taxnot: Boolean,
    var revcheck: Boolean,
    var revlast: String,
    var revnext: String,
    var revplace: String,
    var revnot: Boolean
)

val Context.dataStore by preferencesDataStore(name = "exp_settings")

// Campi delle scadenze
private val INS_CHECK_KEY = booleanPreferencesKey("inscheck")
private val INS_START_KEY = stringPreferencesKey("insstart")
private val INS_END_KEY = stringPreferencesKey("insend")
private val INS_DUES_KEY = intPreferencesKey("insdues")
private val INS_PRICE_KEY = floatPreferencesKey("insprice")
private val INS_PLACE_KEY = stringPreferencesKey("insplace")
private val INS_NOT_KEY = booleanPreferencesKey("insnot")
private val TAX_CHECK_KEY = booleanPreferencesKey("taxcheck")
private val TAX_DATE_KEY = stringPreferencesKey("taxdate")
private val TAX_PRICE_KEY = floatPreferencesKey("taxprice")
private val TAX_NOT_KEY = booleanPreferencesKey("taxnot")
private val REV_CHECK_KEY = booleanPreferencesKey("revcheck")
private val REV_LAST_KEY = stringPreferencesKey("revlast")
private val REV_NEXT_KEY = stringPreferencesKey("revnext")
private val REV_PLACE = stringPreferencesKey("revplace")
private val REV_NOT_KEY = booleanPreferencesKey("revnot")


// Salvare il profilo
suspend fun saveExpSettings(context: Context, expirations: Expirations) {
    context.dataStore.edit { preferences ->
        preferences[INS_CHECK_KEY] = expirations.inscheck
        preferences[INS_START_KEY] = expirations.insstart
        preferences[INS_END_KEY] = expirations.insend
        preferences[INS_DUES_KEY] = expirations.insdues
        preferences[INS_PRICE_KEY] = expirations.insprice
        preferences[INS_PLACE_KEY] = expirations.insplace
        preferences[INS_NOT_KEY] = expirations.insnot
        preferences[TAX_CHECK_KEY] = expirations.taxcheck
        preferences[TAX_DATE_KEY] = expirations.taxdate
        preferences[TAX_PRICE_KEY] = expirations.taxprice
        preferences[TAX_NOT_KEY] = expirations.taxnot
        preferences[REV_CHECK_KEY] = expirations.revcheck
        preferences[REV_LAST_KEY] = expirations.revlast
        preferences[REV_NEXT_KEY] = expirations.revnext
        preferences[REV_PLACE] = expirations.revplace
        preferences[REV_NOT_KEY] = expirations.revnot
    }
}

// Leggere il progilo
fun getExpSettings(context: Context): Flow<Expirations> {
    return context.dataStore.data.map { preferences ->
        Expirations(
            inscheck = preferences[INS_CHECK_KEY] ?: false,
            insstart = preferences[INS_START_KEY] ?: "",
            insend = preferences[INS_END_KEY] ?: "",
            insdues = preferences[INS_DUES_KEY] ?: 0,
            insprice = preferences[INS_PRICE_KEY] ?: 0.0f,
            insplace = preferences[INS_PLACE_KEY] ?: "",
            insnot = preferences[INS_NOT_KEY] ?: false,
            taxcheck = preferences[TAX_CHECK_KEY] ?: false,
            taxdate = preferences[TAX_DATE_KEY] ?: "",
            taxprice = preferences[TAX_PRICE_KEY] ?: 0.0f,
            taxnot = preferences[TAX_NOT_KEY] ?: false,
            revcheck = preferences[REV_CHECK_KEY] ?: false,
            revlast = preferences[REV_LAST_KEY] ?: "",
            revnext = preferences[REV_NEXT_KEY] ?: "",
            revplace = preferences[REV_PLACE] ?: "",
            revnot = preferences[REV_NOT_KEY] ?: false
        )
    }
}