package com.android.mycargenie.pages.scadenze

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R
import com.android.mycargenie.shared.CircleCheckbox
import com.android.mycargenie.shared.formatDateToLong
import com.android.mycargenie.shared.formatDateToString
import java.time.Instant

//Sovrascrivere back button con salvataggio settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpSettingsScreen(
    expirations: Expirations,
    expirationsViewModel: ExpirationsViewModel,
    navController: NavController,
    permissionHandler: PermissionHandler
) {

    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

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

    val field = stringResource(R.string.field)
    val insurer = stringResource(R.string.insurer)
    val amount = stringResource(R.string.amount)
    val total = stringResource(R.string.total_e)
    val insurance = stringResource(R.string.insurance)
    val tax = stringResource(R.string.tax)
    val revPlace = stringResource(R.string.revplace)

    fun saveDeadlinesSettings() {
        val finalInsCheck = if (insstart.isBlank() && insend.isBlank()) false else inscheck
        val finalTaxCheck = if (taxdate.isBlank()) false else taxcheck
        val finalRevCheck = if (revlast.isBlank() && revnext.isBlank()) false else revcheck

        if (inscheck || taxcheck || revcheck) {

            expirationsViewModel.updateExpSettings(
                Expirations(
                    finalInsCheck,
                    insstart,
                    insend,
                    insdues,
                    insprice,
                    insplace,
                    insnot,
                    finalTaxCheck,
                    taxdate,
                    taxprice,
                    taxnot,
                    finalRevCheck,
                    revlast,
                    revnext,
                    revplace,
                    revnot
                )
            )

            if (finalInsCheck) handleInsuranceNotifications(insnot, insend, notificationManager)
            if (finalTaxCheck) handleTaxNotifications(taxnot,taxdate, notificationManager)
            if (finalRevCheck) handleRevisionNotifications(revnot, revnext, notificationManager)

            navController.navigate("ExpirationsScreen")

        } else {
            notificationManager.disableNotifications("insurance")
            notificationManager.disableNotifications("tax")
            notificationManager.disableNotifications("rev")

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
            navController.navigate("ExpirationsScreen")
        }
    }

    //When back button is pressed save settings and go back to the deadlines view screen
    BackHandler {
        saveDeadlinesSettings()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 14.dp, bottom = 8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.back),
                    contentDescription = "Back to deadlines view screen", //To put in strings xml
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable {
                            saveDeadlinesSettings()
                        }
                )
                Text(
                    text = "Impostazioni Scadenze", //To put in strings xml
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(start = 24.dp)
                )
            }

            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .alpha(0.2f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                CircleCheckbox(
                    label = stringResource(R.string.insurance),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    isChecked = inscheck,
                    onValueChange = { inscheck = it },
                    modifier = Modifier
                        .padding(start = 4.dp, top= 0.dp, bottom = 0.dp)
                )
            }

            AnimatedVisibility(
                visible = inscheck,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp, end = 10.dp)
                ) {
                    // Riga assicuratore e importo totale
                    Row {
                        //Colonna assicuratore
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.insurer),
                                    fontSize = 14.sp
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 4.dp)
                            ) {

                                OutlinedTextField(
                                    modifier = Modifier
                                        .semantics {
                                            if (insplace.isEmpty()) {
                                                contentDescription = "$field $insurer"
                                            }
                                        },
                                    shape = CircleShape,
                                    value = insplace,
                                    onValueChange = { newValue ->
                                        if (newValue.length <= 23) {
                                            insplace = newValue
                                        }
                                    },
                                    textStyle = TextStyle(
                                        fontSize = 16.sp
                                    ),
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Next,
                                        capitalization = KeyboardCapitalization.Sentences
                                    ),
                                )
                            }
                        }
                        //Colonna importo totale
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp)
                            ) {
                                Text(
                                    text = "${stringResource(R.string.amount)} ${stringResource(R.string.total_e)}",
                                    fontSize = 14.sp
                                )
                            }

                            var insPriceString by remember {
                                mutableStateOf(if (insprice == 0.0f) "" else insprice.toString())
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 4.dp)
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .semantics {
                                            if (insplace.isEmpty()) {
                                                contentDescription =
                                                    "$field $amount $total $insurance"
                                            }
                                        },
                                    shape = CircleShape,
                                    value = insPriceString,
                                    onValueChange = { newValue ->
                                        val formattedValue = newValue.replace(',', '.')
                                        val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?$")
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
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal,
                                        imeAction = ImeAction.Next
                                    )
                                )
                            }
                        }
                    }

                    //Riga date assicurazione
                    Row {
                        // Colonna inizio assicurazione
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp)
                            ) {
                                Text(
                                    text = "${stringResource(R.string.start)} ${stringResource(R.string.coverage)}",
                                    fontSize = 14.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showInsStartDatePicker = true }
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .padding(16.dp)
                                ) {

                                    Icon(
                                        imageVector = Icons.Rounded.DateRange,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = insstart.ifEmpty {
                                            formatDateToString(
                                                Instant.now().toEpochMilli()
                                            )
                                        },
                                        fontSize = 17.sp,
                                        modifier = Modifier
                                    )
                                }
                            }
                        }

                        // Colonna fine assicurazione
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp)
                            ) {
                                Text(
                                    text = "${stringResource(R.string.end)} ${stringResource(R.string.coverage)}",
                                    fontSize = 14.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .padding(start = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showInsEndDatePicker = true }
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .padding(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.DateRange,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = insend.ifEmpty {
                                            formatDateToString(
                                                Instant.now().toEpochMilli()
                                            )
                                        },
                                        fontSize = 17.sp
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Colonna rate
                        Column(
                            modifier = Modifier
                                .padding(end = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                            ) {
                                Text(
                                    text = stringResource(R.string.dues),
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .wrapContentWidth(Alignment.Start)
                                        .padding(start = 20.dp)
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .padding(8.dp)
                            ) {

                                val dues = listOf(0, 2, 3)

                                dues.forEach { due ->
                                    Column(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clickable {
                                                insdues = due
                                            }
                                            .clip(CircleShape)
                                            .background(
                                                if (insdues == due) {
                                                    MaterialTheme.colorScheme.secondary
                                                } else {
                                                    MaterialTheme.colorScheme.onPrimary
                                                }
                                            )
                                            .padding(6.dp)
                                    ) {
                                        Text(
                                            text = due.toString(),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight(450),
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }

                        if (insend.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 4.dp)
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = stringResource(R.string.reminders),
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .wrapContentWidth(Alignment.Start)
                                            .padding(start = 24.dp)
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .padding(4.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "OFF",
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier
                                                    .padding(end = 6.dp)
                                            )
                                            Switch(
                                                checked = insnot,
                                                onCheckedChange = { checked ->
                                                    insnot = checked
                                                    permissionHandler.initialize()
                                                },
                                                enabled = insend.isNotEmpty()
                                            )
                                            Text(
                                                text = "ON",
                                                fontSize = 16.sp,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier
                                                    .padding(start = 6.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 4.dp),
                        thickness = 2.dp
                    )

                }
            }

            // Tax
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                CircleCheckbox(
                    label = "${stringResource(R.string.tax)} ${stringResource(R.string.automotive)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    isChecked = taxcheck,
                    onValueChange = { taxcheck = it },
                    modifier = Modifier
                        .padding(start = 4.dp, top = 8.dp, bottom = 0.dp)
                )
            }

            AnimatedVisibility(
                visible = taxcheck,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp)
                ) {
                    //Riga tassa prossimo saldo ed importo
                    Row {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp)
                            ) {
                                Text(
                                    text = "${stringResource(R.string.next_m)} ${stringResource(R.string.payment)}",
                                    fontSize = 14.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showTaxDatePicker = true }
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondaryContainer)
                                        .padding(16.dp)
                                ) {

                                    Icon(
                                        imageVector = Icons.Rounded.DateRange,
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = taxdate.ifEmpty {
                                            formatDateToString(
                                                Instant.now().toEpochMilli()
                                            )
                                        },
                                        fontSize = 17.sp,
                                        modifier = Modifier
                                    )
                                }
                            }
                        }

                        //Colonna importo tassa
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.amount),
                                    fontSize = 14.sp
                                )
                            }

                            var taxPriceString by remember {
                                mutableStateOf(if (taxprice == 0.0f) "" else taxprice.toString())
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 4.dp)
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .semantics {
                                            if (taxPriceString.isEmpty()) {
                                                contentDescription = "$field $amount $total $tax"
                                            }
                                        },
                                    shape = CircleShape,
                                    value = taxPriceString,
                                    onValueChange = { newValue ->
                                        val formattedValue = newValue.replace(',', '.')
                                        val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?$")
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
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal,
                                        imeAction = ImeAction.Next
                                    )
                                )
                            }
                        }
                    }

                    //Tax switch column
                    if (taxdate.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .padding(end = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.reminders),
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .wrapContentWidth(Alignment.Start)
                                        .padding(start = 24.dp)
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.secondaryContainer)
                                    .padding(4.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "OFF",
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .padding(end = 6.dp)
                                        )

                                        Switch(
                                            checked = taxnot,
                                            onCheckedChange = { checked ->
                                                taxnot = checked
                                                permissionHandler.initialize()
                                            }
                                        )
                                        Text(
                                            text = "ON",
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .padding(start = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 4.dp),
                        thickness = 2.dp
                    )
                }
            }

            // Revision
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                CircleCheckbox(
                    label = stringResource(R.string.revision),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    isChecked = revcheck,
                    onValueChange = { revcheck = it },
                    modifier = Modifier
                        .padding(start = 4.dp, top = 8.dp, bottom = 0.dp)
                )
            }

                AnimatedVisibility(
                    visible = revcheck,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                    ) {
                        Row {
                            //Colonna ultima revisione
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 20.dp)
                                ) {
                                    Text(
                                        text = "${stringResource(R.string.last)} ${stringResource(R.string.revision_low)}",
                                        fontSize = 14.sp
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .padding(end = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { showRevLastDatePicker = true }
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.secondaryContainer)
                                            .padding(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.DateRange,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = revlast.ifEmpty {
                                                formatDateToString(
                                                    Instant.now().toEpochMilli()
                                                )
                                            },
                                            fontSize = 17.sp,
                                            modifier = Modifier
                                        )
                                    }
                                }
                            }


                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 24.dp)
                                ) {
                                    Text(
                                        text = "${stringResource(R.string.next_f)} ${stringResource(R.string.revision_low)}",
                                        fontSize = 14.sp
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { showRevNextDatePicker = true }
                                            .clip(CircleShape)
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
                                                text = revnext.ifEmpty {
                                                    formatDateToString(
                                                        Instant.now().toEpochMilli()
                                                    )
                                                },
                                                fontSize = 17.sp,
                                                modifier = Modifier
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            //Colonna revisore
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                ) {
                                    Text(
                                        text = stringResource(R.string.revplace),
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .wrapContentWidth(Alignment.Start)
                                            .padding(start = 20.dp)
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .padding(end = 4.dp)
                                ) {

                                    OutlinedTextField(
                                        modifier = Modifier
                                            .semantics {
                                                if (revplace.isEmpty()) {
                                                    contentDescription = "$field $revPlace"
                                                }
                                            },
                                        shape = CircleShape,
                                        value = revplace,
                                        onValueChange = { newValue ->
                                            if (newValue.length <= 20) {
                                                revplace = newValue
                                            }
                                        },
                                        textStyle = TextStyle(
                                            fontSize = 15.sp
                                        ),
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            imeAction = ImeAction.Next,
                                            capitalization = KeyboardCapitalization.Sentences
                                        )
                                    )
                                }
                            }

                            if (revnext.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                ) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Text(
                                            stringResource(R.string.reminders),
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .wrapContentWidth(Alignment.Start)
                                                .padding(start = 24.dp)
                                        )
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.secondaryContainer)
                                            .padding(4.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "OFF",
                                                    fontSize = 16.sp,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier
                                                        .padding(end = 6.dp)
                                                )
                                                Switch(
                                                    checked = revnot,
                                                    onCheckedChange = { checked ->
                                                        revnot = checked
                                                        permissionHandler.initialize()
                                                    }
                                                )
                                                Text(
                                                    text = "ON",
                                                    fontSize = 16.sp,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier
                                                        .padding(start = 6.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(top = 12.dp, bottom = 4.dp),
                            thickness = 2.dp
                        )
                    }

                }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.select_field_message),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f)
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
                } else {
                    onDateSelected(formatDateToString(Instant.now().toEpochMilli()))
                }
                Log.d("date", "selectedDateMillis: $selectedDateMillis")
                onDismissRequest()
            }) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }

    ) {
        DatePicker(state = datePickerState)
    }
}

const val oneMonthMillis = 30L * 24 * 60 * 60 * 1000
const val oneWeekMillis = 7L * 24 * 60 * 60 * 1000
const val oneDayMillis = 1L * 24 * 60 * 60 * 1000
const val hour14Millis = 14L * 60 * 60 * 1000
const val hour8Millis = 8L * 60 * 60 * 1000


fun scheduleNotifications(
    isEnabled: Boolean,
    timestamp: Long,
    type: String,
    titles: List<String>,
    messages: List<String>,
    offsets: List<Long>,
    notificationManager: CustomNotificationManager
) {
    if (isEnabled) {
        notificationManager.disableNotifications(type)
        offsets.forEachIndexed { index, offset ->
            notificationManager.scheduleNotification(
                timestamp + offset,
                titles[index],
                messages[index],
                type
            )
        }
    } else {
        notificationManager.disableNotifications(type)
    }
}

fun handleInsuranceNotifications(insnot: Boolean, insend: String, notificationManager: CustomNotificationManager) {
    val insTimestamp = formatDateToLong(insend)
    val titles = listOf(
        "La tua assicurazione auto sta per scadere.",
        "La tua assicurazione auto è in scadenza.",
        "La tua assicurazione auto è in scadenza.",
        "Rinnova adesso la tua assicurazione auto.",
    )
    val messages = listOf(
        "Il Genio ti ricorda che la polizza scadrà il $insend. Evita sanzioni pagando in tempo.",
        "Il Genio ti ricorda che la polizza scadrà fra una settimana esatta. Evita sanzioni pagando in tempo.",
        "Il Genio ti ricorda che la polizza scadrà domani. Evita sanzioni pagando oggi stesso.",
        "Il Genio ti ricorda che la polizza è scaduta oggi. Contatta subito l'ente assicuratore per evitare sanzioni."
    )
    val offsets = listOf(
        -oneMonthMillis + hour14Millis,
        -oneWeekMillis + hour14Millis,
        -oneDayMillis + hour8Millis,
        hour8Millis
    )
    val tag = "ReminderApp"
    Log.d(tag, "Stato di insnot: $insnot")
    Log.d(tag, "insend: $insend")
    scheduleNotifications(insnot, insTimestamp, "insurance", titles, messages, offsets, notificationManager)
}

fun handleTaxNotifications(taxnot: Boolean, taxdate: String, notificationManager: CustomNotificationManager) {
    val taxTimestamp = formatDateToLong(taxdate)
    val titles = listOf(
        "La tassa automobilistica sta per scadere.",
        "La tassa automobilistica è in scadenza.",
        "La tassa automobilistica è in scadenza.",
        "Paga adesso la tassa automobilistica."
    )
    val messages = listOf(
        "Il Genio ti ricorda che la tassa automobilistica scadrà il $taxdate. Evita sanzioni pagando in tempo.",
        "Il Genio ti ricorda che la tassa automobilistica scadrà fra una settimana esatta. Evita sanzioni pagando in tempo.",
        "Il Genio ti ricorda che la tassa automobilistica scadrà domani. Evita sanzioni pagando oggi stesso.",
        "Il Genio ti ricorda che la tassa automobilistica è scaduta oggi."
    )
    val offsets = listOf(
        -oneMonthMillis + hour14Millis,
        -oneWeekMillis + hour14Millis,
        -oneDayMillis + hour8Millis,
        hour8Millis
    )
    scheduleNotifications(taxnot, taxTimestamp, "tax", titles, messages, offsets, notificationManager)
}

fun handleRevisionNotifications(revnot: Boolean, revnext: String, notificationManager: CustomNotificationManager) {
    val revTimestamp = formatDateToLong(revnext)
    val titles = listOf(
        "La tua revisione auto sta per scadere.",
        "La tua revisione auto è in scadenza.",
        "La tua revisione auto è in scadenza.",
        "Effettua adesso la revisione della tua auto."
    )
    val messages = listOf(
        "Il Genio ti ricorda che la revisione scadrà il $revnext. Evita sanzioni effettuandola in tempo.",
        "Il Genio ti ricorda che la revisione scadrà fra una settimana esatta. Evita sanzioni effettuandola in tempo.",
        "Il Genio ti ricorda che la revisione scadrà domani. Evita sanzioni effettuandola oggi stesso.",
        "Il Genio ti ricorda che la revisione è scaduta oggi. Contatta subito un'officina autorizzata per evitare sanzioni."
    )
    val offsets = listOf(
        -oneMonthMillis + hour14Millis,
        -oneWeekMillis + hour14Millis,
        -oneDayMillis + hour8Millis,
        hour8Millis
    )
    scheduleNotifications(revnot, revTimestamp, "rev", titles, messages, offsets, notificationManager)
}




