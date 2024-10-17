package com.android.mycargenie.pages.manutenzione

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R
import com.android.mycargenie.shared.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditManScreen(
    state: ManState,
    navController: NavController,
    onEvent: (ManEvent) -> Unit
) {
    val manIndex = navController.currentBackStackEntry?.arguments?.getInt("manIndex")

    val manItem = manIndex?.takeIf { it in state.men.indices }?.let { state.men[it] }


    Log.d("manIndex", "manIndex: $manIndex")
    Log.d("manItem", "manItem: $manItem")



    if (manItem != null) {
        LaunchedEffect(manItem) {
            state.id.value = manItem.id ?: 0
            state.title.value = manItem.title
            state.type.value = manItem.type
            state.place.value = manItem.place
            state.date.value = manItem.date
            state.kmt.value = manItem.kmt
            state.description.value = manItem.description
            state.price.value = manItem.price
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
                        state.date.value = formatDate(selectedDateMillis
                        )
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

        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (state.title.value.isNotBlank() && state.date.value.isNotBlank() && state.description.value.isNotBlank()) {

                    Log.d("SaveMan", "Saving: Title: ${state.title.value}, Type: ${state.type.value} id: ${state.id.value}")

                    onEvent(
                        ManEvent.UpdateMan(
                            id = state.id.value,
                            title = state.title.value,
                            type = state.type.value,
                            place = state.place.value,
                            date = state.date.value,
                            kmt = state.kmt.value,
                            description = state.description.value,
                            price = state.price.value,
                            )
                    )
                    navController.popBackStack()
                } else {
                    showError = true
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Salva Modifiche Manutenzione"
                )
            }
        }
    ) { paddingValues ->

        val focusManager = LocalFocusManager.current

        val scrollState = rememberScrollState()

        if (manItem != null) {


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

                //Titolo
                Row {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        value = state.title.value,
                        onValueChange = { newValue ->
                            if (newValue.length <= 50) {
                                state.title.value = newValue
                            }
                        },
                        textStyle = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp
                        ),
                        placeholder = {if (state.title.value.isEmpty()) Text(text = "Titolo*") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
                }


                val types = listOf("Meccanico", "Elettrauto", "Carrozziere", "Altro")

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


                //Data
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(start = 16.dp, end = 8.dp)
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
                                text = state.date.value.ifEmpty { "Data*" },
                                fontSize = 17.sp,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                            )
                        }
                    }

                    //Kilometri
                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 16.dp),
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
                            placeholder = {if (state.kmt.value == 0) Text(text = "Kilometri") },
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

                // 3. Descrizione
                Row {
                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                            value = state.description.value,
                            onValueChange = { newValue ->
                                if (newValue.length <= 500) {
                                    state.description.value = newValue
                                }
                            },
                            placeholder = {if (state.description.value.isEmpty()) Text(text = "Descrizione*") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, end = 16.dp)
                        ) {
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "${state.description.value.length} / 500",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }


                //Prezzo
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        var userPriceInput by remember {
                            mutableStateOf(if (state.price.value == 0.0) "" else state.price.value.toString().replace('.', ','))
                        }

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(end = 16.dp),
                            value = userPriceInput,
                            onValueChange = { newValue ->
                                val regex = Regex("^\\d{0,5}(,\\d{0,2})?\$")
                                if (newValue.isEmpty()) {
                                    userPriceInput = ""
                                    state.price.value = 0.0
                                } else if (regex.matches(newValue)) {
                                    userPriceInput = newValue
                                    val formattedValue = newValue.replace(',', '.')
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
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (state.title.value.isNotBlank() && state.date.value.isNotBlank() && state.description.value.isNotBlank()) {
                                        onEvent(
                                            ManEvent.UpdateMan(
                                                id = state.id.value,
                                                title = state.title.value,
                                                type = state.type.value,
                                                place = state.place.value,
                                                date = state.date.value,
                                                kmt = state.kmt.value,
                                                description = state.description.value,
                                                price = state.price.value
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
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Errore: Elemento non trovato.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
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