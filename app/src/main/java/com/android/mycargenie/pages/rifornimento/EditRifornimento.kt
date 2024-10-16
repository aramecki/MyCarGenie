package com.android.mycargenie.pages.rifornimento

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R
import com.android.mycargenie.shared.formatPrice
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRifScreen(
    state: RifState,
    navController: NavController,
    onEvent: (RifEvent) -> Unit
) {
    val rifIndex = navController.currentBackStackEntry?.arguments?.getInt("rifIndex")

    val rifItem = rifIndex?.takeIf { it in state.rifs.indices }?.let { state.rifs[it] }


    Log.d("rifIndex", "rifIndex: $rifIndex")
    Log.d("rifItem", "rifItem: $rifItem")



    if (rifItem != null) {
        LaunchedEffect(rifItem) {
            state.id.value = rifItem.id ?: 0
            state.type.value = rifItem.type
            state.place.value = rifItem.place
            state.price.value = rifItem.price
            state.uvalue.value = rifItem.uvalue
            state.totunit.value = rifItem.totunit
            state.date.value = rifItem.date
            state.note.value = rifItem.note
            state.kmt.value = rifItem.kmt
        }
    }


    var showError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    // DatePickerDialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    if (selectedDateMillis != null) {
                        state.date.value = formatDate(selectedDateMillis)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("ANNULLA")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        /*
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                ) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro",
                            modifier = Modifier.size(35.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        },

         */

        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (state.date.value.isNotBlank() && state.note.value.isNotBlank()) {

                    Log.d(
                        "SaveRif",
                        "Saving: Price: ${state.price.value}, Type: ${state.type.value} id: ${state.id.value}"
                    )

                    onEvent(
                        RifEvent.UpdateRif(
                            id = state.id.value,
                            type = state.type.value,
                            place = state.place.value,
                            price = state.price.value,
                            uvalue = state.uvalue.value,
                            totunit = state.totunit.value,
                            date = state.date.value,
                            note = state.note.value,
                            kmt = state.kmt.value,
                        )
                    )
                    navController.popBackStack()
                } else {
                    showError = true
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Salva Modifiche Rifornimento"
                )
            }
        }
    ) { paddingValues ->

        val focusManager = LocalFocusManager.current

        val scrollState = rememberScrollState()

        if (rifItem != null) {


            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),

                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = paddingValues.calculateBottomPadding()
                    )
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {


                val types = listOf("Benzina", "Gasolio", "GPL", "Metano", "Elettrico", "Altro")

                Row {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                TypeDropdownMenu(
                                    types = types,
                                    selectedType = state.type
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = state.place.value,
                                    onValueChange = { newValue ->
                                        if (newValue.length <= 16) {
                                            state.place.value = newValue
                                        }
                                    },
                                    textStyle = TextStyle(
                                        fontSize = 17.sp
                                    ),
                                    placeholder = { if (state.place.value.isEmpty()) Text(text = "Luogo") },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Next,
                                        capitalization = KeyboardCapitalization.Sentences
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                                    )
                                )
                            }
                        }
                    }
                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 8.dp)
                ) {
                    //Prezzo
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(start = 16.dp)
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            value = if (state.price.value == 0.0) "" else state.price.value.toString()
                                .replace('.', ','),
                            onValueChange = { newValue ->
                                val regex = Regex("^\\d{0,5}(,\\d{0,2})?\$")
                                val formattedValue = newValue.replace(',', '.')
                                if (newValue.isEmpty()) {
                                    state.price.value = 0.0
                                } else if (regex.matches(newValue)) {
                                    formattedValue.toDoubleOrNull()?.let { doubleValue ->
                                        if (doubleValue <= 99999.99) {
                                            state.price.value = doubleValue
                                        }
                                    }
                                }
                            },
                            placeholder = { Text(text = "Importo") },
                            leadingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.euro_symbol),
                                    contentDescription = "Euro Icon",
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Next) }
                            )
                        )
                    }


                    //Prezzo per unità
                    val leadingIcon: @Composable (() -> Unit)? = if (state.uvalue.value != 0.0) {
                        {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.euro_symbol),
                                contentDescription = "Cost per fuel unit",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        null
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 16.dp),
                            value = if (state.uvalue.value == 0.0) "" else state.uvalue.value.toString()
                                .replace('.', ','),
                            onValueChange = { newValue ->
                                val regex = Regex("^\\d{0,5}(,\\d{0,2})?\$")
                                val formattedValue = newValue.replace(',', '.')
                                if (newValue.isEmpty()) {
                                    state.uvalue.value = 0.0
                                } else if (regex.matches(newValue)) {
                                    formattedValue.toDoubleOrNull()?.let { doubleValue ->
                                        if (doubleValue <= 99999.99) {
                                            state.uvalue.value = doubleValue
                                        }
                                    }
                                }
                            },
                            placeholder = {if (state.type.value == "Elettrico") Text(text = "€/kWh")
                            else if (state.type.value.isEmpty() || state.type.value == "Altro") Text(text = "€/l o €/kWh")
                            else (Text(text = "€/l")) },

                            leadingIcon = leadingIcon,

                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Next) }
                            )
                        )

                    }
                }

                //Quantità totale
                val totUnitLeadingIcon: @Composable (() -> Unit)? = when {
                    state.uvalue.value != 0.0 && state.type.value == "Elettrico" -> {
                        { Text(text = "kWh") }
                    }
                    state.uvalue.value == 0.0 || state.type.value.isEmpty() || state.type.value == "Altro" -> null
                    else -> {
                        { Text(text = "l") }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(start = 16.dp, end = 8.dp)
                    ) {
                        val totUnit = remember { mutableStateOf("") }
                        val isManualInput = remember { mutableStateOf(false) }

                        LaunchedEffect(state.price.value, state.uvalue.value) {
                            if (state.price.value > 0.0 && state.uvalue.value > 0.0) {
                                val totUnitCalc = state.price.value / state.uvalue.value
                                totUnit.value = formatPrice(totUnitCalc)
                                state.totunit.value = totUnitCalc
                                isManualInput.value = false // Se i valori sono calcolati, disabilita l'input manuale
                            } else {
                                totUnit.value = "" // Imposta totUnit a una stringa vuota quando non sono validi
                                state.totunit.value = 0.0
                                isManualInput.value = true // Permetti l'input manuale
                            }
                        }

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = totUnit.value,
                            onValueChange = { newValue ->
                                val regex = Regex("^\\d{0,5}(,\\d{0,2})?\$")
                                val formattedValue = newValue.replace(',', '.')
                                if (newValue.isEmpty()) {
                                    state.totunit.value = 0.0
                                    totUnit.value = ""
                                    isManualInput.value = true // Imposta l'input manuale
                                } else if (regex.matches(newValue)) {
                                    formattedValue.toDoubleOrNull()?.let { doubleValue ->
                                        if (doubleValue <= 9999.99) {
                                            state.totunit.value = doubleValue
                                            totUnit.value = newValue
                                            isManualInput.value = true // Imposta l'input manuale
                                        }
                                    }
                                }
                            },
                            textStyle = TextStyle(
                                fontSize = 17.sp
                            ),
                            placeholder = {
                                if (totUnit.value.isEmpty()) {
                                    // Visualizza il placeholder solo se l'input è manuale
                                    if (state.type.value == "Elettrico") Text(text = "kWh Totali")
                                    else if (state.type.value.isEmpty() || state.type.value == "Altro") Text(text = "Litri o kWh Totali")
                                    else Text(text = "Litri Totali")
                                }
                            },
                            leadingIcon = totUnitLeadingIcon,
                            enabled = !(state.price.value > 0.0 && state.uvalue.value > 0.0), // Disabilita l'input se price e uvalue sono > 0.0
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Next) }
                            )
                        )
                    }

                    //Data
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 16.dp)
                            .clickable {
                                showDatePicker = true
                            }
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DateRange,
                                contentDescription = "Calendario"
                            )
                            Text(
                                text = state.date.value.ifEmpty {
                                    com.android.mycargenie.pages.manutenzione.formatDate(
                                        Instant.now().toEpochMilli()
                                    )
                                },
                                fontSize = 17.sp,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                            )
                        }
                    }

                }


                // 3. Note
                Row {
                    Column {
                        // OutlinedTextField
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                            value = state.note.value,
                            onValueChange = { newValue ->
                                if (newValue.length <= 500) {
                                    state.note.value = newValue
                                }
                            },
                            placeholder = { Text(text = "Note") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                        )

                        // Contatore dei caratteri
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, end = 16.dp)
                        ) {
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "${state.note.value.length} / 500",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }

                //Kilometri
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(end = 16.dp),
                            value = if (state.kmt.value == 0) "" else state.kmt.value.toString(),
                            onValueChange = { newValue ->
                                if (newValue.isEmpty()) {
                                    state.kmt.value = 0
                                } else {
                                    newValue.toIntOrNull()?.let { intValue ->
                                        if (intValue in 1..9_999_999) {
                                            state.kmt.value = intValue
                                        }
                                    }
                                }
                            },
                            placeholder = { Text(text = "Kilometri") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (state.date.value.isNotBlank() && state.note.value.isNotBlank()) {
                                        onEvent(
                                            RifEvent.UpdateRif(
                                                id = state.id.value,
                                                type = state.type.value,
                                                place = state.place.value,
                                                price = state.price.value,
                                                uvalue = state.uvalue.value,
                                                totunit = state.totunit.value,
                                                date = state.date.value,
                                                note = state.note.value,
                                                kmt = state.kmt.value,
                                            )
                                        )
                                        navController.popBackStack()
                                    } else {
                                        showError = true
                                    }
                                }

                            )
                        )
                    }
                }


                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "I campi contrassegnati da * sono obbligatori.",
                            fontSize = 14.sp,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }

            if (showError) {
                Text(
                    text = "Compila tutti i campi obbligatori.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

    }
