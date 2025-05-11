package com.android.mycargenie.pages.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.android.mycargenie.R
import com.android.mycargenie.shared.Brands
import com.android.mycargenie.shared.CarEcoList
import com.android.mycargenie.shared.CarFuels
import com.android.mycargenie.shared.CarTypes
import com.android.mycargenie.shared.ConfiguredDropdownMenu
import com.android.mycargenie.shared.saveImageToMmry

@Composable
fun LibrettoSettingsScreen(
    carProfile: CarProfile,
    librettoViewModel: LibrettoViewModel,
    navController: NavController,
    context: Context
) {
    val focusManager = LocalFocusManager.current

    var brand by remember { mutableStateOf(carProfile.brand) }
    var model by remember { mutableStateOf(carProfile.model) }
    var displacement by remember { mutableIntStateOf(carProfile.displacement) }
    var power by remember { mutableFloatStateOf(carProfile.power) }
    var horsepower by remember { mutableFloatStateOf(carProfile.horsepower) }
    val savedImagePath by remember { mutableStateOf(carProfile.savedImagePath) }
    var type by remember { mutableStateOf(carProfile.type) }
    var fuel by remember { mutableStateOf(carProfile.fuel) }
    var year by remember { mutableIntStateOf(carProfile.year) }
    var eco by remember { mutableStateOf(carProfile.eco) }
    var conf by remember { mutableStateOf(carProfile.conf) }

    val tag = "ProfilePic"
    Log.d(tag, "Immagine già presente: $savedImagePath")

    var newImagePath by remember { mutableStateOf("") }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { it: Uri? ->
            Log.d(tag, "Hai selezionato l'uri: ${it.toString()}")
            newImagePath = it?.toString() ?: savedImagePath
            Log.d(tag, "L'uri è stato traformato in: $newImagePath")
        }
    )

    //Function to save the profile picture and go back to Profile view page
    fun saveProfileSettings() {
        if (brand.isNotBlank() && model.isNotBlank()) {

            //Log.d(tag, "Prima della funzione: $savedImagePath")

            val imageToSave =
                if (newImagePath != savedImagePath && newImagePath != "") {
                    saveImageToMmry(context = context, newImagePath.toUri())
                } else {
                    savedImagePath
                }

            //Log.d(tag, "dopo la funzione: $newImagePath")

            librettoViewModel.updateCarProfile(
                CarProfile(
                    brand,
                    model,
                    displacement,
                    power,
                    horsepower,
                    savedImagePath = imageToSave,
                    type,
                    fuel,
                    year,
                    eco,
                    conf
                )
            )
            navController.navigate("ProfileScreen")
        } else {
            navController.navigate("ProfileScreen")
        }
    }

    //When back button is pressed save settings and go back to the deadlines view screen
    BackHandler {
        saveProfileSettings()
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .imePadding()
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
                    contentDescription = "Back to profile view screen", //To put in strings xml
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .clickable {
                            saveProfileSettings()
                        }
                )
                Text(
                    text = "Impostazioni Libretto", //To put in strings xml
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

            if (savedImagePath.isNotEmpty() || newImagePath.isNotEmpty()) {

                AsyncImage(
                    model = when {
                        newImagePath.isNotEmpty() -> newImagePath
                        savedImagePath.isNotEmpty() && newImagePath == "" -> savedImagePath
                        else -> null
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Log.d(tag, "mostro l'immagine: $savedImagePath")

            } else {
                Text(
                    text = stringResource(R.string.set_pp_message),
                    modifier = Modifier.padding(16.dp),
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                Log.d(tag, "Immagine già presente dopo il photoPicker: $savedImagePath")
                Log.d(tag, "Immagine scelta come nuova: $newImagePath")
            }) {
                Text(stringResource(R.string.select))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {

                ConfiguredDropdownMenu(
                    label = stringResource(R.string.brand) + "*",
                    item = brand,
                    itemList = Brands.getBrandsList(),
                    onItemSelected = { brand = it },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 8.dp)
                )

                OutlinedTextField(
                    value = model,
                    onValueChange = { newValue ->
                        if (newValue.length <= 21) {
                            model = newValue
                        }
                    },
                    shape = CircleShape,
                    textStyle = TextStyle(
                        fontSize = 18.sp
                    ),
                    label = { Text(stringResource(R.string.model) + "*") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                OutlinedTextField(
                    value = conf,
                    onValueChange = { newValue ->
                        if (newValue.length <= 30) {
                            conf = newValue
                        }
                    },
                    shape = CircleShape,
                    textStyle = TextStyle(
                        fontSize = 18.sp
                    ),
                    label = { Text(stringResource(R.string.configuration)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 8.dp),
                    value = if (year == 0) "" else year.toString(),
                    onValueChange = { newValue ->
                        if (newValue.isEmpty()) {
                            year = 0
                        } else {
                            newValue.toIntOrNull()?.let { intValue ->
                                if (intValue in 1..9_999) {
                                    year = intValue
                                }
                            }
                        }
                    },
                    shape = CircleShape,
                    textStyle = TextStyle(
                        fontSize = 20.sp
                    ),
                    label = { Text(text = stringResource(R.string.year)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    value = if (displacement == 0) "" else displacement.toString(),
                    onValueChange = { newValue ->
                        if (newValue.isEmpty()) {
                            displacement = 0
                        } else {
                            newValue.toIntOrNull()?.let { intValue ->
                                if (intValue in 1..9_999) {
                                    displacement = intValue
                                }
                            }
                        }
                    },
                    shape = CircleShape,
                    textStyle = TextStyle(
                        fontSize = 20.sp
                    ),
                    label = { Text(text = stringResource(R.string.displacement) + "(" + stringResource(R.string.cc) + ")") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                var carPower by remember {
                    mutableStateOf(if (power == 0.0f) "" else power.toString())
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 8.dp),
                    value = carPower,
                    onValueChange = { newValue ->
                        val formattedValue = newValue.replace(',', '.')
                        val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?$")
                        if (newValue.isEmpty()) {
                            carPower = ""
                            power = 0.0f
                        } else if (regex.matches(newValue)) {
                            carPower = newValue
                            formattedValue.toFloatOrNull()?.let { floatValue ->
                                if (floatValue <= 9999.99f) {
                                    power = floatValue
                                }
                            }
                        }
                    },
                    shape = CircleShape,
                    textStyle = TextStyle(
                        fontSize = 20.sp
                    ),
                    label = { Text(stringResource(R.string.power) + "(" + stringResource(R.string.kW) + ")") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    )
                )

                var carHorsePower by remember {
                    mutableStateOf(if (horsepower == 0.0f) "" else horsepower.toString())
                }

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    value = carHorsePower,
                    onValueChange = { newValue ->
                        val formattedValue = newValue.replace(',', '.')
                        val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?$")
                        if (newValue.isEmpty()) {
                            carHorsePower = ""
                            horsepower = 0.0f
                        } else if (regex.matches(newValue)) {
                            carHorsePower = newValue
                            formattedValue.toFloatOrNull()?.let { floatValue ->
                                if (floatValue <= 9999.99f) {
                                    horsepower = floatValue
                                }
                            }
                        }
                    },
                    shape = CircleShape,
                    textStyle = TextStyle(
                        fontSize = 20.sp
                    ),
                    label = { Text(stringResource(R.string.horses) + "(" + stringResource(R.string.CV) + ")") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    )
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {

                ConfiguredDropdownMenu(
                    label = stringResource(R.string.type),
                    item = type,
                    itemList = CarTypes.getCarTypesList(),
                    onItemSelected = { type = it },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 8.dp)
                )

                ConfiguredDropdownMenu(
                    label = stringResource(R.string.fuel),
                    item = fuel,
                    itemList = CarFuels.getCarFuelsList(),
                    onItemSelected = { fuel = it },
                    modifier = Modifier
                        .padding(start = 8.dp)
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {

                ConfiguredDropdownMenu(
                    label = stringResource(R.string.eco),
                    item = eco,
                    itemList = CarEcoList.getCarEcoList(),
                    onItemSelected = { eco = it },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 8.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.req_fields),
                    modifier = Modifier
                        .padding(top = 14.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}




