package com.android.mycargenie.pages.manutenzione

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AddManScreen(
    state: ManState,
    navController: NavController,
    onEvent: (ManEvent) -> Unit
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Controllo se i campi non sono vuoti prima di salvare
                if (state.title.value.isNotBlank() && state.description.value.isNotBlank()) {
                    onEvent(
                        ManEvent.SaveMan(
                        title = state.title.value,
                        description = state.description.value
                    ))
                    // Torna alla schermata precedente
                    navController.popBackStack()
                } else {
                    // Mostra un messaggio di errore o fai qualcos'altro in caso di campi vuoti
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
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = state.title.value,
                onValueChange = {
                    state.title.value = it
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp
                ),
                placeholder = {
                    Text(text = "Title")
                }

            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = state.description.value,
                onValueChange = {
                    state.description.value = it
                },
                placeholder = {
                    Text(text = "Description")
                }
            )
        }
    }
}
