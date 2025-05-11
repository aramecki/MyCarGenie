package com.android.mycargenie.pages.manutenzione

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
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
import com.android.mycargenie.shared.CancelWithoutSaving
import com.android.mycargenie.shared.CarProfessionistsList
import com.android.mycargenie.shared.ConfiguredDropdownMenu
import com.android.mycargenie.shared.formatDateToString
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddManScreen(
    state: ManState,
    navController: NavController,
    onEvent: (ManEvent) -> Unit
) {

    LaunchedEffect(Unit) {
        state.date.value = formatDateToString(Instant.now().toEpochMilli())
    }

    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    var showError by remember { mutableStateOf(false) }
    var showCancel by remember { mutableStateOf(false) }
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

    fun saveEventAndGoBackToView() {
        if (state.title.value.isNotBlank() && state.date.value.isNotBlank() && state.description.value.isNotBlank()) {
            onEvent(
                ManEvent.SaveMan(
                    id = null,
                    title = state.title.value,
                    type = state.type.value,
                    place = state.place.value,
                    date = state.date.value,
                    kmt = state.kmt.value,
                    description = state.description.value,
                    price = state.price.value
                )
            )
            navController.navigate("ManutenzioneScreen")
        } else {
            showError = true
        }
    }

    BackHandler {
        showCancel = true
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                saveEventAndGoBackToView()
            },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "${stringResource(R.string.save)} ${stringResource(R.string.maintenance)}"
                )
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            CancelWithoutSaving(
                isShowing = showCancel,
                onDismiss = { showCancel = false },
                onConfirm = {
                    showCancel = false
                    navController.navigate("ManutenzioneScreen")
                }
            )
        }

        Column(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
            ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 14.dp, bottom = 8.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.back),
                    contentDescription = stringResource(R.string.back_to_maintenance),
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable {
                            showCancel = true
                        }
                )
                Text(
                    text = "${stringResource(R.string.add_1)} ${stringResource(R.string.maintenance)}",
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
                    .padding(bottom = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 16.dp,
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                        bottom = paddingValues.calculateBottomPadding()
                    )
                    .verticalScroll(scrollState)
            ) {

                //Titolo
                Row(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        value = state.title.value,
                        onValueChange = { newValue ->
                            if (newValue.length <= 35) {
                                state.title.value = newValue
                            }
                        },
                        shape = CircleShape,
                        textStyle = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp
                        ),
                        placeholder = { Text(text = stringResource(R.string.title) + "*") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Next) }
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .padding(horizontal = 10.dp),
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                            ) {

                            ConfiguredDropdownMenu(
                                label = stringResource(R.string.type),
                                item = state.type.value,
                                itemList = CarProfessionistsList.getCarProfessionistsList(),
                                onItemSelected = { state.type.value = it },
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .padding(end = 8.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp, top = 8.dp)
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = state.place.value,
                                    onValueChange = { newValue ->
                                        if (newValue.length <= 16) {
                                            state.place.value = newValue
                                        }
                                    },
                                    shape = CircleShape,
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


                //Data
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 16.dp)
                        .padding(horizontal = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(end = 8.dp)
                            .clickable {
                                showDatePicker = true
                            }
                            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DateRange,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = state.date.value.ifEmpty {
                                    formatDateToString(
                                        Instant.now().toEpochMilli()
                                    )
                                },
                                fontSize = 17.sp,
                                style = TextStyle(color = MaterialTheme.colorScheme.secondary),
                                modifier = Modifier
                                    .padding(start = 4.dp)
                            )
                        }
                    }

                    //Kilometri
                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp),
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
                            shape = CircleShape,
                            placeholder = { Text(text = stringResource(R.string.kilometers)) },
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

                //Descrizione
                Row(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                ) {
                    Column {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            value = state.description.value,
                            onValueChange = { newValue ->
                                if (newValue.length <= 500) {
                                    state.description.value = newValue
                                }
                            },
                            shape = RoundedCornerShape(30.dp),
                            placeholder = { Text(text = stringResource(R.string.description) + "*") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                capitalization = KeyboardCapitalization.Sentences
                            ),
                        )

                        // Contatore dei caratteri
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, end = 20.dp)
                        ) {
                            Spacer(Modifier.weight(1f))
                            Text(
                                text = "${state.description.value.length} / 500",
                                style = TextStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 12.sp,
                                ),
                                modifier = Modifier
                                    .alpha(0.7f)
                            )
                        }
                    }
                }


                //Prezzo
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        var userPriceInput by remember { mutableStateOf("") }

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(end = 10.dp),
                            value = userPriceInput,
                            onValueChange = { newValue ->
                                val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?$")
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
                            shape = CircleShape,
                            placeholder = { Text(text = stringResource(R.string.price)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.euro_symbol),
                                    contentDescription = stringResource(R.string.value),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    saveEventAndGoBackToView()
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
                        text = if (showError) stringResource(R.string.compile_req_fields) else stringResource(
                            R.string.req_fields
                        ),
                        fontSize = if (showError) 16.sp else 14.sp,
                        color = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        fontWeight = if (showError) FontWeight.SemiBold else null,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))

            }
        }
    }
}