package com.android.mycargenie.pages.scadenze

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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

    val field = stringResource(R.string.field)
    val insurer = stringResource(R.string.insurer)
    val amount = stringResource(R.string.amount)
    val total = stringResource(R.string.total_e)
    val insurance = stringResource(R.string.insurance)
    val tax = stringResource(R.string.tax)
    val revPlace = stringResource(R.string.revplace)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleCheckbox(
                label = stringResource(R.string.insurance),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                isChecked = inscheck,
                onValueChange = { inscheck = it },
                modifier = Modifier
                    .padding(10.dp)
            )
        }

        AnimatedVisibility(
            visible = inscheck,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {


                    Text(
                        text = "${stringResource(R.string.start)} ${stringResource(R.string.coverage)}",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .clickable { showInsStartDatePicker = true }
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

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {


                    Text(
                        text = "${stringResource(R.string.end)} ${stringResource(R.string.coverage)}",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .clickable { showInsEndDatePicker = true }
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

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.dues),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(3f)
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
                                .clip(CircleShape)
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
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
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
                        text = stringResource(R.string.insurer),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(1f)
                    )



                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
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

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {


                    Text(
                        text = "${stringResource(R.string.amount)} ${stringResource(R.string.total_e)}",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(1f)
                    )

                    var insPriceString by remember {
                        mutableStateOf(if (insprice == 0.0f) "" else insprice.toString())
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .semantics {
                                if (insplace.isEmpty()) {
                                    contentDescription = "$field $amount $total $insurance"
                                }
                            },
                        shape = CircleShape,
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
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (insend.isNotEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.reminders),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Switch(
                                checked = insnot,
                                onCheckedChange = { checked ->
                                    insnot = checked
                                    permissionHandler.initialize()
                                }
                            )
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp),
                    thickness = 2.dp
                )

            }
        }

        // Tax
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleCheckbox(
                label = "${stringResource(R.string.tax)} ${stringResource(R.string.automotive)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                isChecked = taxcheck,
                onValueChange = { taxcheck = it },
                modifier = Modifier
                    .padding(10.dp)
            )
        }

        AnimatedVisibility(
            visible = taxcheck,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Text(
                        text = "${stringResource(R.string.next_m)} ${stringResource(R.string.payment)}",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .clickable { showTaxDatePicker = true }
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

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Text(
                        text = stringResource(R.string.amount),
                        fontSize = 18.sp,
                        modifier = Modifier
                            .weight(1f)
                    )

                    var taxPriceString by remember {
                        mutableStateOf(if (taxprice == 0.0f) "" else taxprice.toString())
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .semantics {
                                if (taxPriceString.isEmpty()) {
                                    contentDescription = "$field $amount $total $tax"
                                }
                            },
                        shape = CircleShape,
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
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (taxdate.isNotEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.reminders),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Switch(
                                checked = taxnot,
                                onCheckedChange = { checked ->
                                    taxnot = checked
                                    permissionHandler.initialize()
                                }
                            )
                        }
                    }
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp),
                    thickness = 2.dp
                )

            }
        }

        // Revision
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleCheckbox(
                label = stringResource(R.string.revision),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                isChecked = revcheck,
                onValueChange = { revcheck = it },
                modifier = Modifier
                    .padding(10.dp)
            )
        }

            AnimatedVisibility(
                visible = revcheck,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {

                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "${stringResource(R.string.last)} ${stringResource(R.string.revision_low)}",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .weight(1f)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .clickable { showRevLastDatePicker = true }
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
                            text = "${stringResource(R.string.next_f)} ${stringResource(R.string.revision_low)}",
                            fontSize = 18.sp,
                            modifier = Modifier
                                .weight(1f)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
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
                                    text = revnext.ifEmpty { formatDateToString(Instant.now().toEpochMilli()) },
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
                            text = stringResource(R.string.revplace),
                            fontSize = 18.sp,
                            modifier = Modifier
                                .weight(1f)
                        )

                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
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

                    Spacer(modifier = Modifier.height(8.dp))

                    if (revnext.isNotEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    stringResource(R.string.reminders),
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                )

                                Switch(
                                    checked = revnot,
                                    onCheckedChange = { checked ->
                                        revnot = checked
                                        permissionHandler.initialize()
                                    }
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp),
                        thickness = 2.dp
                    )
            }



        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 8.dp)
        ) {

            Button(onClick = {

                if (inscheck || taxcheck || revcheck) {

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

                    handleInsuranceNotifications(insnot, insend, notificationManager)
                    handleTaxNotifications(taxnot,taxdate, notificationManager)
                    handleRevisionNotifications(revnot, revnext, notificationManager)

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
                             },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
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




