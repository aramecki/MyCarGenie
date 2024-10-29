package com.android.mycargenie.pages.scadenze

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.shared.formatDate
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpSettingsScreen(
    expirations: Expirations,
    navController: NavController
) {

    var assstart by remember { mutableStateOf(expirations.assstart) }
    var assend by remember { mutableStateOf(expirations.assend) }
    var assdues by remember { mutableIntStateOf(expirations.assdues) }
    var assprice by remember { mutableFloatStateOf(expirations.assprice) }
    var assplace by remember { mutableStateOf(expirations.assplace) }

    var isAssChecked by remember { mutableStateOf(false) }

    var showAssStartDatePicker by remember { mutableStateOf(false) }
    val assStartDatePickerState = rememberDatePickerState()


    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = isAssChecked,
                onCheckedChange = { isAssChecked = it }
            )
            Text(
                text = "Assicurazione RCA",
                fontSize = 18.sp
            )

        }

        if (isAssChecked) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {

                Column(
                    modifier = Modifier
                        .padding(start = 48.dp)
                ) {
                    Text(
                        text = "Inizio copertura:"
                    )
                }
                Column(
                    modifier = Modifier
                        //.fillMaxWidth(0.5f)
                        .padding(start = 16.dp, end = 32.dp)
                        .clickable {
                            showAssStartDatePicker = true
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
                            text = assstart.ifEmpty { formatDate(Instant.now().toEpochMilli()) },
                            fontSize = 17.sp,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }

            }


        }
    }

    if (showAssStartDatePicker) {

        DatePickerDialog(
            onDismissRequest = { showAssStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDateMillis = assStartDatePickerState.selectedDateMillis
                    if (selectedDateMillis != null) {
                        assstart = formatDate(selectedDateMillis)
                    }
                    showAssStartDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAssStartDatePicker = false }) {
                    Text("ANNULLA")
                }
            }
        ) {
            DatePicker(state = assStartDatePickerState)
        }
    }
}

