package com.android.mycargenie.pages.scadenze

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.shared.CustomNotificationManager
import com.android.mycargenie.shared.formatDateToLong
import com.android.mycargenie.shared.formatDateToString
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpSettingsScreen(
    expirations: Expirations,
    expirationsViewModel: ExpirationsViewModel,
    navController: NavController
) {

    val context = LocalContext.current



    val notificationManager = remember { CustomNotificationManager(context) }
    var isInsendChecked by remember { mutableStateOf(false) }
    var isTaxDateChecked by remember { mutableStateOf(false) }
    var isRevNextChecked by remember { mutableStateOf(false) }

    // Insurance components
    var inscheck by remember { mutableStateOf(expirations.inscheck) }
    var insstart by remember { mutableStateOf(expirations.insstart) }
    var insend by remember { mutableStateOf(expirations.insend) }
    var insdues by remember { mutableIntStateOf(expirations.insdues) }
    var insprice by remember { mutableFloatStateOf(expirations.insprice) }
    var insplace by remember { mutableStateOf(expirations.insplace) }


    var showInsStartDatePicker by remember { mutableStateOf(false) }
    val insStartDatePickerState = rememberDatePickerState()
    var showInsEndDatePicker by remember { mutableStateOf(false) }
    val insEndDatePickerState = rememberDatePickerState()

    val isInsRemButEnabled = insend != ""

    //Tax components
    var taxcheck by remember { mutableStateOf(expirations.taxcheck) }
    var taxdate by remember { mutableStateOf(expirations.taxdate) }
    var taxprice by remember { mutableFloatStateOf(expirations.taxprice) }

    var showTaxDatePicker by remember { mutableStateOf(false) }
    val taxDatePickerState = rememberDatePickerState()

    //Revision components
    var revcheck by remember { mutableStateOf(expirations.revcheck) }
    var revlast by remember { mutableStateOf(expirations.revlast) }
    var revnext by remember { mutableStateOf(expirations.revnext) }
    var revplace by remember { mutableStateOf(expirations.revplace) }

    var showRevLastDatePicker by remember { mutableStateOf(false) }
    val revLastDatePickerState = rememberDatePickerState()
    var showRevNextDatePicker by remember { mutableStateOf(false) }
    val revNextDatePickerState = rememberDatePickerState()


    val isSaveEnabled = inscheck || taxcheck || revcheck


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {

            Text(
                text = "Seleziona un campo per abilitarlo e gestirne le impostazioni.",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                //.padding(vertical = 8.dp)
        ) {

            Checkbox(
                checked = inscheck,
                onCheckedChange = { inscheck = it }
            )
            Text(
                text = "Assicurazione RCA",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

        }

        if (inscheck) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {


                Text(
                    text = "Inizio copertura:",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clickable { showInsStartDatePicker = true }
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {


                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = insstart.ifEmpty { formatDateToString(Instant.now().toEpochMilli()) },
                            fontSize = 17.sp,
                            modifier = Modifier
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {


                Text(
                    text = "Fine copertura:",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clickable { showInsEndDatePicker = true }
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = insend.ifEmpty { formatDateToString(Instant.now().toEpochMilli()) },
                            fontSize = 17.sp,
                            modifier = Modifier
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Rate:",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(2f)
                        .wrapContentWidth(Alignment.Start)
                )

                val dues = listOf(0, 2, 3)

                dues.forEach { due ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                insdues = due
                            }
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (insdues == due) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.secondaryContainer
                                }
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = due.toString(),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }


            //Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.5f)
                ) {

                    OutlinedTextField(
                        value = insplace,
                        onValueChange = { newValue ->
                            if (newValue.length <= 12) {
                                insplace = newValue
                            }
                        },
                        textStyle = TextStyle(
                            fontSize = 19.sp
                        ),
                        label = { Text("Assicuratore") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        )
                    )
                }


                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    var insPriceString by remember {
                        mutableStateOf(if (insprice == 0.0f) "" else insprice.toString())
                    }

                    OutlinedTextField(
                        value = insPriceString,
                        onValueChange = { newValue ->
                            val formattedValue = newValue.replace(',', '.')
                            val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?\$")
                            if (newValue.isEmpty()) {
                                insPriceString = ""
                                insprice = 0.0f
                            } else if (regex.matches(newValue)) {
                                insPriceString = newValue
                                formattedValue.toFloatOrNull()?.let { floatValue ->
                                    if (floatValue <= 9999.99f) {
                                        insprice = floatValue
                                    }
                                }
                            }
                        },
                        textStyle = TextStyle(
                            fontSize = 20.sp
                        ),
                        label = { Text("Costo Totale") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )
                    )

                }
            }

            if (insend != "") {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = isInsendChecked,
                        onCheckedChange = { checked ->
                            isInsendChecked = checked // Aggiorna lo stato del CheckBox
                        }
                    )
                    Text("Ricevi notifiche sulla scadenza.")



                    Button(
                        onClick = {

                            val tag = "ReminderApp"

                            Log.d(tag, "insend: $insend")
                            val insTimestamp = formatDateToLong(insend)
                            Log.d(tag, "insTimestamp: $insTimestamp")


                            val oneMonthMillis = 30L * 24 * 60 * 60 * 1000      // Circa un mese (30 giorni)
                            val oneWeekMillis = 7L * 24 * 60 * 60 * 1000        // Una settimana
                            val oneDayMillis = 1L * 24 * 60 * 60 * 1000         // Un

                            val hour14Millis = 14 * 60 * 60 * 1000  // ore 14:00
                            val hour8Millis = 8 * 60 * 60 * 1000    // ore 08:00


                            Log.d(tag, "Stato di isInsendChecked: $isInsendChecked")

                            if (isInsendChecked) {

                                notificationManager.scheduleNotification(
                                    System.currentTimeMillis() + 10000,
                                    "Notifica 5 sec dopo.",
                                    "Il Genio ti ricorda che la tua polizza assicurativa scadrà il $insend, non dimenticare di versare la quota di rinnovo per non incorrere in sanzioni.",
                                    "insurance"
                                )

                                notificationManager.scheduleNotification(
                                    (insTimestamp - oneMonthMillis) + hour14Millis,
                                    "Notifica un mese prima.",
                                    "Il Genio ti ricorda che la tua polizza assicurativa scadrà il $insend, non dimenticare di versare la quota di rinnovo per non incorrere in sanzioni.",
                                    "insurance"
                                )

                                notificationManager.scheduleNotification(
                                    (insTimestamp - oneWeekMillis) + hour14Millis,
                                    "Notifica una settimana prima.",
                                    "Il Genio ti ricorda che la tua polizza assicurativa scadrà il $insend, non dimenticare di versare la quota di rinnovo per non incorrere in sanzioni.",
                                    "insurance"
                                )

                                notificationManager.scheduleNotification(
                                    (insTimestamp - oneDayMillis) + hour8Millis,
                                    "Notifica un giorno prima.",
                                    "Il Genio ti ricorda che la tua polizza assicurativa scadrà il $insend, non dimenticare di versare la quota di rinnovo per non incorrere in sanzioni.",
                                    "insurance"
                                )

                                notificationManager.scheduleNotification(
                                    insTimestamp + hour8Millis,
                                    "Notifica il giorno stesso.",
                                    "Il Genio ti ricorda che la tua polizza assicurativa scadrà il $insend, non dimenticare di versare la quota di rinnovo per non incorrere in sanzioni.",
                                    "insurance"
                                )

                                /*
                                val insTimestamp = formatDateToLong(insend)
                                Log.d(tag, "Il timestamp per la data $insend è: $insTimestamp")

                                //val oneMonthBefore = insTimestamp - (30L * 24 * 60 * 60 * 1000)
                                notificationManager.scheduleNotification(
                                    System.currentTimeMillis() + 5000,
                                    //"La tua assicurazione auto sta per scadere.",
                                    "Notifica 1 mese prima.",
                                    "Il Genio ti ricorda che la tua polizza assicurativa scadrà il $insend, non dimenticare di versare la quota di rinnovo per non incorrere in sanzioni.",
                                    "insurance"
                                )

                                val oneWeekBefore = insTimestamp + 5000
                                val delay = oneWeekBefore - System.currentTimeMillis()
                                Log.d(tag, "Ritardo per Notifica 2: $delay ms")
                                notificationManager.scheduleNotification(
                                    oneWeekBefore,
                                    //"La tua assicurazione auto sta per scadere.",
                                    "Notifica 2.",
                                    "Il Genio ti ricorda che la tua polizza assicurativa scadrà il $insend, non dimenticare di versare la quota di rinnovo per non incorrere in sanzioni.",
                                    "insurance"
                                )



                                //val oneWeekBefore = insTimestamp - (7L * 24 * 60 * 60 * 1000)
                                notificationManager.scheduleNotification(
                                    1738281600000 + (7L * 24 * 60 * 60 * 1000),
                                    "Notifica 1 settimana prima.",
                                    "Seconda notifica.",
                                    "insurance"
                                )


                                val oneDayBefore = insTimestamp - (24 * 60 * 60 * 1000)
                                notificationManager.scheduleNotification(
                                    oneDayBefore,
                                    "Notifica 1 giorno prima.",
                                    "Seconda notifica.",
                                    "insurance"
                                )
                                Log.d(tag, "Notifica 1 giorno prima programmata per: ${formatDateToString(oneDayBefore)} alle ${formatTime(oneDayBefore)}")


                                notificationManager.scheduleNotification(
                                    insTimestamp,
                                    "Notifica il giorno stesso.",
                                    "Seconda notifica.",
                                    "insurance"
                                )
                                Log.d(tag, "Notifica giorno stesso programmata per: ${formatDateToString(insTimestamp)} alle ${formatTime(insTimestamp)}")

                                 */


                            } else {
                                notificationManager.disableNotifications("insurance")

                            }

                        },
                        //enabled = isInsendChecked,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Promemoria")
                    }



                }

            }




            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp),
                thickness = 2.dp
            )

        }

        // Tax
        //Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                //.padding(vertical = 8.dp)
        ) {

            Checkbox(
                checked = taxcheck,
                onCheckedChange = { taxcheck = it }
            )
            Text(
                text = "Tassa",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

        }

        if (taxcheck) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {


                Text(
                    text = "Prossimo saldo:",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clickable { showInsStartDatePicker = true }
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {


                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = taxdate.ifEmpty { formatDateToString(Instant.now().toEpochMilli()) },
                            fontSize = 17.sp,
                            modifier = Modifier
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {


                    var taxPriceString by remember {
                        mutableStateOf(if (taxprice == 0.0f) "" else taxprice.toString())
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.6f),
                        value = taxPriceString,
                        onValueChange = { newValue ->
                            val formattedValue = newValue.replace(',', '.')
                            val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?\$")
                            if (newValue.isEmpty()) {
                                taxPriceString = ""
                                taxprice = 0.0f
                            } else if (regex.matches(newValue)) {
                                taxPriceString = newValue
                                formattedValue.toFloatOrNull()?.let { floatValue ->
                                    if (floatValue <= 9999.99f) {
                                        taxprice = floatValue
                                    }
                                }
                            }
                        },
                        textStyle = TextStyle(
                            fontSize = 20.sp
                        ),
                        label = { Text("Costo") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp),
                thickness = 2.dp
            )

        }

        // Revision
        //Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                //.padding(vertical = 8.dp)
        ) {

            Checkbox(
                checked = revcheck,
                onCheckedChange = { revcheck = it }
            )
            Text(
                text = "Revisione",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

        }

        if (revcheck) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = "Ultima revisione:",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clickable { showRevLastDatePicker = true }
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {


                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = revlast.ifEmpty { formatDateToString(Instant.now().toEpochMilli()) },
                            fontSize = 17.sp,
                            modifier = Modifier
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {


                Text(
                    text = "Prossima revisione:",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .clickable { showRevNextDatePicker = true }
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = revnext.ifEmpty { formatDateToString(Instant.now().toEpochMilli()) },
                            fontSize = 17.sp,
                            modifier = Modifier
                        )
                    }
                }
            }


            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.6f),
                        value = revplace,
                        onValueChange = { newValue ->
                            if (newValue.length <= 12) {
                                revplace = newValue
                            }
                        },
                        textStyle = TextStyle(
                            fontSize = 19.sp
                        ),
                        label = { Text("Revisore") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        )
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp),
                thickness = 2.dp
            )

        }

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
        ) {







            Button(onClick = {



                expirationsViewModel.updateExpSettings(
                    Expirations(
                        inscheck,
                        insstart,
                        insend,
                        insdues,
                        insprice,
                        insplace,
                        taxcheck,
                        taxdate,
                        taxprice,
                        revcheck,
                        revlast,
                        revnext,
                        revplace
                    )
                )



                navController.navigate("ExpirationsScreen")
                //} else {
                //  showError = true
                //}
            },
                enabled = isSaveEnabled,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Salva")
            }
        }

    }



    if (showInsStartDatePicker) {
        CustomDatePickerDialog(
            onDismissRequest = { showInsStartDatePicker = false },
            datePickerState = insStartDatePickerState,
            onDateSelected = { selectedDate -> insstart = selectedDate }
        )
    }


    if (showInsEndDatePicker) {
        CustomDatePickerDialog(
            onDismissRequest = { showInsEndDatePicker = false },
            datePickerState = insEndDatePickerState,
            onDateSelected = { selectedDate -> insend = selectedDate }
        )
    }

    if (showTaxDatePicker) {
        CustomDatePickerDialog(
            onDismissRequest = { showTaxDatePicker = false },
            datePickerState = taxDatePickerState,
            onDateSelected = { selectedDate -> taxdate = selectedDate }
        )
    }

    if (showRevLastDatePicker) {
        CustomDatePickerDialog(
            onDismissRequest = { showRevLastDatePicker = false },
            datePickerState = revLastDatePickerState,
            onDateSelected = { selectedDate -> revlast = selectedDate }
        )
    }


    if (showRevNextDatePicker) {
        CustomDatePickerDialog(
            onDismissRequest = { showRevNextDatePicker = false },
            datePickerState = revNextDatePickerState,
            onDateSelected = { selectedDate -> revnext = selectedDate }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    onDismissRequest: () -> Unit,
    datePickerState: DatePickerState,
    onDateSelected: (String) -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                val selectedDateMillis = datePickerState.selectedDateMillis
                if (selectedDateMillis != null) {
                    onDateSelected(formatDateToString(selectedDateMillis))
                }
                onDismissRequest()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("ANNULLA")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}


