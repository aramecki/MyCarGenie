package com.android.mycargenie.pages.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import coil.compose.rememberAsyncImagePainter
import androidx.navigation.NavController
import com.android.mycargenie.pages.settings.SetViewModel

@Composable
fun ProfileSettingsScreen(
    carProfile: CarProfile,
    navController: NavController,
    onProfileChange: (CarProfile) -> Unit,
    setViewModel: SetViewModel
) {
    var brand by remember { mutableStateOf(carProfile.brand) }
    var model by remember { mutableStateOf(carProfile.model) }
    var engine by remember { mutableStateOf(carProfile.engine) }
    var power by remember { mutableStateOf(carProfile.power) }
    var horsepower by remember { mutableStateOf(carProfile.horsepower) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    // Launcher per la selezione dell'immagine
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
            } else {
                Toast.makeText(context, "Nessuna immagine selezionata", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Launcher per la richiesta del permesso
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Se il permesso è stato concesso, apri la galleria
                galleryLauncher.launch("image/*")
            } else {
                // Mostra un messaggio di errore se il permesso non è stato concesso
                Toast.makeText(
                    context,
                    "Consenti l'accesso alla galleria nelle impostazioni.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Marca") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Modello") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = engine,
            onValueChange = { engine = it },
            label = { Text("Motore") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = power,
            onValueChange = { power = it },
            label = { Text("Potenza") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = horsepower,
            onValueChange = { horsepower = it },
            label = { Text("Cavalli") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Controllo per le versioni di Android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Controlla se il permesso è già stato concesso
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                    // Richiedi il permesso se non è stato concesso
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    // Se il permesso è già stato concesso, apri la galleria
                    galleryLauncher.launch("image/*")
                }
            } else {
                // Per le versioni precedenti di Android, apri direttamente la galleria
                galleryLauncher.launch("image/*")
            }
        }) {
            Text("Seleziona immagine dalla galleria")
        }

        Spacer(modifier = Modifier.height(8.dp))

        imageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            setViewModel.updateCarProfile(CarProfile(brand, model, engine, power, horsepower, imageUri))
            navController.navigate("ProfileScreen")
        }) {
            Text("Salva")
        }
    }
}
