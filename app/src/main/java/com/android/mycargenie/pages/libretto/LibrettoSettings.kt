package com.android.mycargenie.pages.libretto

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.android.mycargenie.R
import com.android.mycargenie.pages.manutenzione.TypeDropdownMenu
import com.android.mycargenie.shared.saveImageToMmry

@Composable
fun LibrettoSettingsScreen(
    carProfile: CarProfile,
    librettoViewModel: LibrettoViewModel,
    navController: NavController,
    context: Context
) {
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

    var showError by remember { mutableStateOf(false) }

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



    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {

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

        if (showError) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.compile_req_fields),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Row {

            OutlinedTextField(
                value = brand,
                onValueChange = { newValue ->
                    if (newValue.length <= 12) {
                        brand = newValue
                    }
                },
                textStyle = TextStyle(
                    fontSize = 19.sp
                ),
                label = { Text(text = "${stringResource(id = R.string.brand)}*") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(end = 8.dp)
            )

            OutlinedTextField(
                value = model,
                onValueChange = { newValue ->
                    if (newValue.length <= 21) {
                        model = newValue
                    }
                },
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                label = { Text("${stringResource(R.string.model)}*") },
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

        Row {
            OutlinedTextField(
                value = conf,
                onValueChange = { newValue ->
                    if (newValue.length <= 30) {
                        conf = newValue
                    }
                },
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



        Row {

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
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                label = { Text(text = "${stringResource(R.string.displacement)}(${stringResource(R.string.cc)})") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

        }


        Spacer(modifier = Modifier.height(16.dp))

        Row {

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
                    val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?\$")
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
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                label = { Text("${stringResource(R.string.power)}(${stringResource(R.string.kW)})") },
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
                    val regex = Regex("^\\d{0,5}(\\.\\d{0,2})?\$")
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
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                label = { Text("${stringResource(R.string.horses)}(${stringResource(R.string.CV)})") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                )
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            val selectedType = remember { mutableStateOf(type) }

            LaunchedEffect(selectedType.value) {
                type = selectedType.value
            }

            val types = listOf(stringResource(R.string.sedan), stringResource(R.string.coupe),
                stringResource(R.string.sportscar), stringResource(R.string.suv), stringResource(R.string.stationwagon),
                stringResource(R.string.minivan), stringResource(R.string.supercar), stringResource(R.string.different))

            Box(modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(top = 8.dp, end = 8.dp)
            ) {
                TypeDropdownMenu(
                    types = types,
                    selectedType = selectedType
                )

            }


            val selectedFuelType = remember { mutableStateOf(fuel) }

            LaunchedEffect(selectedFuelType.value) {
                fuel = selectedFuelType.value
            }

            val fuelTypes = listOf(stringResource(R.string.gasoline), stringResource(R.string.diesel), stringResource(R.string.lpg),
                stringResource(R.string.cng), stringResource(R.string.electric), stringResource(R.string.different))

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp)
            ) {
                TypeDropdownMenu(
                    types = fuelTypes,
                    selectedType = selectedFuelType,
                    placeholder = stringResource(R.string.fuel)
                )

            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            val selectedEcoType = remember { mutableStateOf(eco) }

            LaunchedEffect(selectedEcoType.value) {
                eco = selectedEcoType.value
            }

            val ecoTypes = listOf(stringResource(R.string.e1), stringResource(R.string.e2), stringResource(R.string.e3),
                stringResource(R.string.e4), stringResource(R.string.e5), stringResource(R.string.e6), stringResource(R.string.different))

            Box(modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(top = 8.dp, end = 8.dp)
            ) {
                TypeDropdownMenu(
                    types = ecoTypes,
                    selectedType = selectedEcoType,
                    placeholder = stringResource(R.string.eco)
                )

            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {


                Button(onClick = {

                    if (brand.isNotBlank() && model.isNotBlank()) {

                        //Log.d(tag, "Prima della funzione: $savedImagePath")

                        val imageToSave =
                            if (newImagePath != savedImagePath && newImagePath != "") {
                                saveImageToMmry(context = context, Uri.parse(newImagePath))
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
                        showError = true
                    }
                },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                    ) {
                    Text(stringResource(R.string.save))
                }


            }
        }
    }
}




