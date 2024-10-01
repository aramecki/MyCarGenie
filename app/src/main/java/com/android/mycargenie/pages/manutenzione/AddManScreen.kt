package com.android.mycargenie.pages.manutenzione

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects.toString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddManScreen(
    state: ManState,
    navController: NavController,
    onEvent: (ManEvent) -> Unit
) {

    var showError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) } // Stato per controllare quando mostrare il DatePickerDialog

    // DatePickerDialog da mostrare quando l'utente clicca sul campo data
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
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (state.title.value.isNotBlank() && state.date.value.isNotBlank() && state.place.value.isNotBlank() && state.description.value.isNotBlank()) {
                    onEvent(
                        ManEvent.SaveMan(
                            title = state.title.value,
                            date = state.date.value,
                            place = state.place.value,
                            description = state.description.value
                        )
                    )
                    navController.popBackStack() // Torna alla schermata precedente
                } else {
                    showError = true // Mostra il messaggio di errore
                }
            }) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Salva Manutenzione"
                )
            }
        }
    ) { paddingValues ->

        Column(
            verticalArrangement = Arrangement.Top, // Assicura che gli elementi vengano disposti dall'alto
            horizontalAlignment = Alignment.Start, // Allinea gli elementi a sinistra
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            Row {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = state.title.value,
                    onValueChange = { state.title.value = it },
                    textStyle = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    ),
                    placeholder = { Text(text = "Titolo") }
                )
            }

            Row {
// 2. Selettore di data con pulsante
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(16.dp)
                        .clickable {
                            showDatePicker = true
                        } // Quando clicchi, mostra il DatePickerDialog
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp)
                ) {
                    Text(
                        text = state.date.value.ifEmpty { "Data" },
                        //fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    )
                }

                Row {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        value = state.place.value,
                        onValueChange = { state.place.value = it },
                        placeholder = { Text(text = "Luogo") }
                    )
                }
            }


            // 3. Descrizione
            Row {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = state.description.value,
                    onValueChange = { state.description.value = it },
                    placeholder = { Text(text = "Descrizione") }
                )
            }

            if (showError) {
                Text(
                    text = "Compila tutti i campi.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}


// Funzione di supporto per formattare la data in modo leggibile
fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun PreviewAddManScreen() {
    val exampleState = ManState(
        title = mutableStateOf("Titolo di esempio"),
        date = mutableStateOf("01/01/2024"),
        place = mutableStateOf("Luogo di esempio"),
        description = mutableStateOf("Descrizione di esempio")
    )

    val navController = rememberNavController()

    val onEvent: (ManEvent) -> Unit = { event ->
        when (event) {
            is ManEvent.SaveMan -> {
                println("Salvato: ${event.title}, ${event.date}, ${event.place}, ${event.description}")
            }

            is ManEvent.DeleteMan -> TODO()
            ManEvent.SortMan -> TODO()
        }
    }

    AddManScreen(
        state = exampleState,
        navController = navController,
        onEvent = onEvent
    )
}