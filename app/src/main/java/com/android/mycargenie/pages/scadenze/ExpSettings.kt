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
import androidx.compose.runtime.derivedStateOf
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
import com.android.mycargenie.shared.formatDateToLong
import com.android.mycargenie.shared.formatDateToString
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpSettingsScreen(
    expirations: Expirations,
    expirationsViewModel: ExpirationsViewModel,
    navController: NavController,
    permissionHandler: PermissionHandler
) {

    val context = LocalContext.current

    val notificationManager = remember { CustomNotificationManager(context) }

    // Insurance components
    var inscheck by remember { mutableStateOf(expirations.inscheck) }
    var insstart by remember { mutableStateOf(expirations.insstart) }
    var insend by remember { mutableStateOf(expirations.insend) }
    var insdues by remember { mutableIntStateOf(expirations.insdues) }
    var insprice by remember { mutableFloatStateOf(expirations.insprice) }
    var insplace by remember { mutableStateOf(expirations.insplace) }
    var insnot by remember { mutableStateOf(expirations.insnot) }

    var showInsStartDatePicker by remember { mutableStateOf(false) }
    val insStartDatePickerState = rememberDatePickerState()
    var showInsEndDatePicker by remember { mutableStateOf(false) }
    val insEndDatePickerState = rememberDatePickerState()

    //Tax components
    var taxcheck by remember { mutableStateOf(expirations.taxcheck) }
    var taxdate by remember { mutableStateOf(expirations.taxdate) }
    var taxprice by remember { mutableFloatStateOf(expirations.taxprice) }
    var taxnot by remember { mutableStateOf(expirations.taxnot) }

    var showTaxDatePicker by remember { mutableStateOf(false) }
    val taxDatePickerState = rememberDatePickerState()

    //Revision components
    var revcheck by remember { mutableStateOf(expirations.revcheck) }
    var revlast by remember { mutableStateOf(expirations.revlast) }
    var revnext by remember { mutableStateOf(expirations.revnext) }
    var revplace by remember { mutableStateOf(expirations.revplace) }
    var revnot by remember { mutableStateOf(expirations.revnot) }

    var showRevLastDatePicker by remember { mutableStateOf(false) }
    val revLastDatePickerState = rememberDatePickerState()
    var showRevNextDatePicker by remember { mutableStateOf(false) }
    val revNextDatePickerState = rememberDatePickerState()

    // Stato del pulsante abilitato o disabilitato
    val isButtonEnabled = remember {
        derivedStateOf {
            val isAnyCheckActive = inscheck || taxcheck || revcheck

            val isInsValid = if (inscheck) {
                insstart.isNotEmpty() || insend.isNotEmpty() ||
                        insdues != 0 || insprice != 0.0f || insplace.isNotEmpty()
            } else {
                true
            }

            val isTaxValid = if (taxcheck) {
                taxdate.isNotEmpty() || taxprice != 0.0f
            } else {
                true
            }

            val isRevValid = if (revcheck) {
                revlast.isNotEmpty() || revnext.isNotEmpty() || revplace.isNotEmpty()
            } else {
                true
            }

            isAnyCheckActive && isInsValid && isTaxValid && isRevValid
        }
    }

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
            verticalAlignment = Alignment.CenterVertically
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
                        checked = insnot,
                        onCheckedChange = { checked ->
                            insnot = checked
                            permissionHandler.initialize()
                        }
                    )
                    Text(
                        "Ricevi notifiche sulla scadenza."
                    )

                }

            }




            HorizontalDivider(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp),
                thickness = 2.dp
            )

        }

        // Tax
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = taxcheck,
                onCheckedChange = { taxcheck = it }
            )
            Text(
                text = "Tassa Automobilistica",
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
                        .clickable { showTaxDatePicker = true }
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

            if (taxdate != "") {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = taxnot,
                        onCheckedChange = { checked ->
                            taxnot = checked
                            permissionHandler.initialize()
                        }
                    )
                    Text(
                        "Ricevi notifiche sulla scadenza."
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
        Row(
            verticalAlignment = Alignment.CenterVertically
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

            if (revnext.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = revnot,
                        onCheckedChange = { checked ->
                            revnot = checked
                            permissionHandler.initialize()
                        }
                    )
                    Text(
                        "Ricevi notifiche sulla scadenza."
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
                        insnot,
                        taxcheck,
                        taxdate,
                        taxprice,
                        taxnot,
                        revcheck,
                        revlast,
                        revnext,
                        revplace,
                        revnot
                    )
                )

                val tag = "ReminderApp"

                val oneMonthMillis = 30L * 24 * 60 * 60 * 1000
                val oneWeekMillis = 7L * 24 * 60 * 60 * 1000
                val oneDayMillis = 1L * 24 * 60 * 60 * 1000

                val hour14Millis = 14 * 60 * 60 * 1000
                val hour8Millis = 8 * 60 * 60 * 1000


                Log.d(tag, "Stato di insnot: $insnot")

                if (insnot) {

                    notificationManager.disableNotifications("insurance")

                    Log.d(tag, "insend: $insend")
                    val insTimestamp = formatDateToLong(insend)

                    notificationManager.scheduleNotification(
                        (insTimestamp - oneMonthMillis) + hour14Millis,
                        "La tua assicurazione auto sta per scadere.",
                        "Il Genio ti ricorda che la polizza scadrà il $insend. Evita sanzioni pagando in tempo.",
                        "insurance"
                    )

                    notificationManager.scheduleNotification(
                        (insTimestamp - oneWeekMillis) + hour14Millis,
                        "La tua assicurazione auto è in scadenza.",
                        "Il Genio ti ricorda che la polizza scadrà fra una settimana esatta. Evita sanzioni pagando in tempo.",
                        "insurance"
                    )

                    notificationManager.scheduleNotification(
                        (insTimestamp - oneDayMillis) + hour8Millis,
                        "La tua assicurazione auto è in scadenza.",
                        "Il Genio ti ricorda che la polizza scadrà domani. Evita sanzioni pagando oggi stesso.",
                        "insurance"
                    )

                    notificationManager.scheduleNotification(
                        insTimestamp + hour8Millis,
                        "Rinnova adesso la tua assicurazione auto.",
                        "Il Genio ti ricorda che la polizza è scaduta oggi. Contatta subito l'ente assicuratore per evitare sanzioni.",
                        "insurance"
                    )
                } else {
                    notificationManager.disableNotifications("insurance")
                }

                Log.d(tag, "Stato di taxnot: $taxnot")
                if (taxnot) {

                    notificationManager.disableNotifications("tax")

                    Log.d(tag, "taxdate: $taxdate")
                    val taxTimestamp = formatDateToLong(taxdate)

                    notificationManager.scheduleNotification(
                        (taxTimestamp - oneMonthMillis) + hour14Millis,
                        "La tassa automobilistica sta per scadere.",
                        "Il Genio ti ricorda che la tassa automobilistica scadrà il $taxdate. Evita sanzioni pagando in tempo.",
                        "tax"
                    )

                    notificationManager.scheduleNotification(
                        (taxTimestamp - oneWeekMillis) + hour14Millis,
                        "La tassa automobilistica è in scadenza.",
                        "Il Genio ti ricorda che la tassa automobilistica scadrà fra una settimana esatta. Evita sanzioni pagando in tempo.",
                        "tax"
                    )

                    notificationManager.scheduleNotification(
                        (taxTimestamp - oneDayMillis) + hour8Millis,
                        "La tassa automobilistica è in scadenza.",
                        "Il Genio ti ricorda che la tassa automobilistica scadrà domani. Evita sanzioni pagando oggi stesso.",
                        "tax"
                    )

                    notificationManager.scheduleNotification(
                        taxTimestamp + hour8Millis,
                        "Paga adesso la tassa automobilistica.",
                        "Il Genio ti ricorda che la tassa automobilistica è scaduta oggi.",
                        "tax"
                    )
                } else {
                    notificationManager.disableNotifications("tax")
                }

                Log.d(tag, "Stato di revnot: $revnot")
                if (revnot) {

                    notificationManager.disableNotifications("rev")

                    Log.d(tag, "revnext: $revnext")
                    val revTimestamp = formatDateToLong(revnext)

                    notificationManager.scheduleNotification(
                        (revTimestamp - oneMonthMillis) + hour14Millis,
                        "La tua revisione auto sta per scadere.",
                        "Il Genio ti ricorda che la revisione scadrà il $revnext. Evita sanzioni effettuandola in tempo.",
                        "rev"
                    )

                    notificationManager.scheduleNotification(
                        (revTimestamp - oneWeekMillis) + hour14Millis,
                        "La tua revisione auto è in scadenza.",
                        "Il Genio ti ricorda che la revisione scadrà fra una settimana esatta. Evita sanzioni effettuandola in tempo.",
                        "rev"
                    )

                    notificationManager.scheduleNotification(
                        (revTimestamp - oneDayMillis) + hour8Millis,
                        "La tua revisione auto è in scadenza.",
                        "Il Genio ti ricorda che la revisione scadrà domani. Evita sanzioni effettuandola oggi stesso.",
                        "rev"
                    )

                    notificationManager.scheduleNotification(
                        revTimestamp + hour8Millis,
                        "Effettua adesso la revisione della tua auto.",
                        "Il Genio ti ricorda che la revisione è scaduta oggi. Contatta subito un'officina autorizzata per evitare sanzioni.",
                        "rev"
                    )
                } else {
                    notificationManager.disableNotifications("rev")
                }

                navController.navigate("ExpirationsScreen")
            },
                enabled = isButtonEnabled.value,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Salva")
            }
        }

            Text(
                "Resetta tutti i campi",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expirationsViewModel.updateExpSettings(
                            Expirations(
                                inscheck = false,
                                insstart = "",
                                insend = "",
                                insdues = 0,
                                insprice = 0.0f,
                                insplace = "",
                                insnot = false,
                                taxcheck = false,
                                taxdate = "",
                                taxprice = 0.0f,
                                taxnot = false,
                                revcheck = false,
                                revlast = "",
                                revnext = "",
                                revplace = "",
                                revnot = false
                            )
                        )

                        notificationManager.disableNotifications("insurance")
                        notificationManager.disableNotifications("tax")
                        notificationManager.disableNotifications("rev")

                        navController.navigate("ExpirationsScreen")
                    }
            )

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


