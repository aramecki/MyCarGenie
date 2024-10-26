package com.android.mycargenie.pages.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.android.mycargenie.pages.manutenzione.TypeDropdownMenu
import com.android.mycargenie.pages.settings.SetViewModel
import com.android.mycargenie.shared.saveImgToMmry
import java.io.File

@Composable
fun ProfileSettingsScreen(
    carProfile: CarProfile,
    setViewModel: SetViewModel,
    navController: NavController
) {
    var brand by remember { mutableStateOf(carProfile.brand) }
    var model by remember { mutableStateOf(carProfile.model) }
    var displacement by remember { mutableIntStateOf(carProfile.displacement) }
    var power by remember { mutableFloatStateOf(carProfile.power) }
    var horsepower by remember { mutableFloatStateOf(carProfile.horsepower) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var type by remember { mutableStateOf(carProfile.type) }
    var fuel by remember { mutableStateOf(carProfile.fuel) }
    var year by remember { mutableIntStateOf(carProfile.year) }
    var eco by remember { mutableStateOf(carProfile.eco) }
    var conf by remember { mutableStateOf(carProfile.conf) }


    var permissionRequested by remember { mutableStateOf(false) }

    var showError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Selezione immagine
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val savedImagePath = saveImgToMmry(context, uri)
                if (savedImagePath != null) {
                    imageUri = Uri.parse(savedImagePath)
                } else {
                    Toast.makeText(context, "Errore durante il salvataggio dell'immagine", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Nessuna immagine selezionata", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Richiesta del permesso
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Apri la galleria solo se non è stata richiesta di recente
                if (!permissionRequested) {
                    permissionRequested = true
                    galleryLauncher.launch("image/*")
                }
            } else {
                // Errore se permesso non concesso
                Toast.makeText(
                    context,
                    "Consenti l'accesso alla galleria nelle impostazioni.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(16.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()) // Rendi la colonna scrollabile
    ) {

        if (imageUri != null) {
            imageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(File(uri.path.toString())),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        } else {
            Text(
                text = "Imposta un'immagine del profilo",
                modifier = Modifier.padding(16.dp),
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))


        Button(onClick = {
            // Controllo versione Android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Permesso già stato concesso
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    // Permesso se non concesso
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    // Permesso già concesso apri la galleria
                    galleryLauncher.launch("image/*")
                }
            } else {
                // Android 13 in giù apri direttamente la galleria
                galleryLauncher.launch("image/*")
            }
        }) {
            Text("Seleziona")
        }


        Spacer(modifier = Modifier.height(16.dp))

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
                label = { Text("Marca*") },
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
                label = { Text("Modello*") },
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
                label = { Text("Configurazione") },
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
                label = { Text(text = "Anno") },
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
                label = { Text(text = "Cilindrata(cc)") },
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
                label = { Text("Potenza(kW)") },
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
                label = { Text("Cavalli(CV)") },
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

            val types = listOf("Berlina", "Coupé", "Sportiva", "SUV", "Station Wagon", "Monovolume", "Supercar", "Altro")

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

            val fuelTypes = listOf("Benzina", "Gasolio", "GPL", "Metano", "Elettrico", "Altro")

            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp)
            ) {
                TypeDropdownMenu(
                    types = fuelTypes,
                    selectedType = selectedFuelType,
                    placeholder = "Alimentazione"
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

            val ecoTypes = listOf("Euro 1", "Euro 2", "Euro 3", "Euro 4", "Euro 5", "Euro 6", "Euro 6d/dTemp", "Altro")

            Box(modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(top = 8.dp, end = 8.dp)
            ) {
                TypeDropdownMenu(
                    types = ecoTypes,
                    selectedType = selectedEcoType,
                    placeholder = "Categoria Eco"
                )

            }

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
                label = { Text("Marca*") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences
                ),
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(end = 8.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {


                Button(onClick = {

                    if (brand.isNotBlank() && model.isNotBlank()) {

                        setViewModel.updateCarProfile(
                            CarProfile(
                                brand,
                                model,
                                displacement,
                                power,
                                horsepower,
                                imageUri?.path,
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
                    Text("Salva")
                }


            }



        }

        if (showError) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
                Text(
                    text = "Compila tutti i campi obbligatori.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
            }
        }
    }

}




