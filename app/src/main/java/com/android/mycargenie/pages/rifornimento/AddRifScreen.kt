package com.android.mycargenie.pages.rifornimento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.res.stringResource
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
import com.android.mycargenie.pages.manutenzione.TypeDropdownMenu
import com.android.mycargenie.shared.formatDateToString
import com.android.mycargenie.shared.formatPrice
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRifScreen(
    state: RifState,
    navController: NavController,
    onEvent: (RifEvent) -> Unit
) {

    LaunchedEffect(Unit) {
        state.date.value = formatDateToString(Instant.now().toEpochMilli())
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
                        state.date.value = formatDateToString(selectedDateMillis)
                    }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (state.price.value > 0) {
                    onEvent(
                        RifEvent.SaveRif(
                            id = null,
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
            },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "${stringResource(R.string.save)} ${stringResource(R.string.refueling)}"
                )
            }
        }
    ) { paddingValues ->

        val focusManager = LocalFocusManager.current


        val scrollState = rememberScrollState()

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


            val types = listOf(stringResource(R.string.gasoline), stringResource(R.string.diesel), stringResource(R.string.lpg),
                stringResource(R.string.cng), stringResource(R.string.electric), stringResource(R.string.different))

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
                                .padding(start = 8.dp, end = 8.dp)
                        ) {
                            TypeDropdownMenu(
                                types = types,
                                selectedType = state.type
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp, end = 8.dp)
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
                                placeholder = { Text(text = stringResource(R.string.place)) },
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
                    .padding(top = 16.dp)
            ) {
                //Prezzo
                var userPriceInput by remember { mutableStateOf("") }

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
                        value = userPriceInput,
                        onValueChange = { newValue ->
                            val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?\$")
                            if (newValue.isEmpty()) {
                                userPriceInput = ""
                                state.price.value = 0.0
                            } else if (regex.matches(newValue)) {
                                userPriceInput = newValue
                                newValue.toDoubleOrNull()?.let { doubleValue ->
                                    if (doubleValue <= 99999.99) {
                                        state.price.value = doubleValue
                                    }
                                }
                            }
                        },
                        placeholder = { Text(text = "${stringResource(R.string.amount)}*") },
                        leadingIcon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.euro_symbol),
                                contentDescription = stringResource(R.string.value),
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
                }


                //Prezzo per unità
                var uValueInput by remember { mutableStateOf("") }

                val uValueLeadingIcon: @Composable (() -> Unit)? = if (state.uvalue.value != 0.0) {
                    {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.euro_symbol),
                            contentDescription = stringResource(R.string.unit_cost),
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
                        value = uValueInput,
                        onValueChange = { newValue ->
                            val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?\$")
                            if (newValue.isEmpty()) {
                                uValueInput = ""
                                state.uvalue.value = 0.0
                            } else if (regex.matches(newValue)) {
                                uValueInput = newValue
                                newValue.toDoubleOrNull()?.let { doubleValue ->
                                    if (doubleValue <= 99999.99) {
                                        state.uvalue.value = doubleValue
                                    }
                                }
                            }
                        },
                        placeholder = {
                            if (state.type.value == stringResource(R.string.electric)) Text(text = stringResource(R.string.eur_kwh))
                            else if (state.type.value.isEmpty() || state.type.value == stringResource(R.string.different)) Text(text = "${stringResource(R.string.eur_l)} ${stringResource(R.string.or)} ${stringResource(R.string.eur_kwh)}")
                            else Text(text = stringResource(R.string.eur_l))
                        },
                        leadingIcon = uValueLeadingIcon,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
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
                state.totunit.value != 0.0 && state.type.value == stringResource(R.string.electric) -> {
                    { Text(text = stringResource(R.string.kWh)) }
                }
                state.totunit.value == 0.0 || state.type.value.isEmpty() || state.type.value == stringResource(R.string.different) -> null
                else -> {
                    { Text(text = stringResource(R.string.l)) }
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
                            isManualInput.value = false
                        } else {
                            totUnit.value = ""
                            state.totunit.value = 0.0
                            isManualInput.value = true
                        }
                    }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = totUnit.value,
                        onValueChange = { newValue ->
                            val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?\$")
                            if (newValue.isEmpty()) {
                                state.totunit.value = 0.0
                                totUnit.value = ""
                                isManualInput.value = true
                            } else if (regex.matches(newValue)) {
                                newValue.toDoubleOrNull()?.let { doubleValue ->
                                    if (doubleValue <= 9999.99) {
                                        state.totunit.value = doubleValue
                                        totUnit.value = newValue
                                        isManualInput.value = true
                                    }
                                }
                            }
                        },
                        textStyle = TextStyle(
                            fontSize = 17.sp
                        ),
                        placeholder = {
                            if (totUnit.value.isEmpty()) {
                                if (state.type.value == stringResource(R.string.electric)) Text(text = "${stringResource(R.string.kWh)} ${stringResource(R.string.total)}")
                                else if (state.type.value.isEmpty() || state.type.value == stringResource(R.string.different)) Text(text = "${stringResource(R.string.liters)} ${stringResource(R.string.or)} ${stringResource(R.string.kWh)} ${stringResource(R.string.total)}")
                                else Text(text = "${stringResource(R.string.liters)} ${stringResource(R.string.total)}")
                            }
                        },
                        leadingIcon = totUnitLeadingIcon,
                        enabled = !(state.price.value > 0.0 && state.uvalue.value > 0.0),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
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
                            contentDescription = null
                        )
                        Text(
                            text = state.date.value.ifEmpty { formatDateToString(Instant.now().toEpochMilli()) },
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
                        placeholder = { Text(text = stringResource(R.string.notes)) },
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
                        placeholder = { Text(text = stringResource(R.string.kilometers)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (state.price.value > 0) {
                                    onEvent(
                                        RifEvent.SaveRif(
                                            id = null,
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
                    text = if (showError) stringResource(R.string.compile_req_fields) else stringResource(R.string.req_fields),
                    fontSize = if (showError) 16.sp else 14.sp,
                    color = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    fontWeight = if (showError) FontWeight.SemiBold else null,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}


@Composable
fun TypeDropdownMenu(types: List<String>, selectedType: MutableState<String>) {
    var isDropDownExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isDropDownExpanded = true }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = selectedType.value.ifEmpty { stringResource(R.string.type) })
            Icon(
                imageVector = Icons.Rounded.ArrowDropDown,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = isDropDownExpanded,
            onDismissRequest = { isDropDownExpanded = false }
        ) {
            types.forEachIndexed { _, type ->
                DropdownMenuItem(
                    text = { Text(text = type) },
                    onClick = {
                        selectedType.value = type
                        isDropDownExpanded = false
                    }
                )
            }
        }
    }
}