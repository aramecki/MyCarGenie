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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
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
    var engine by remember { mutableStateOf(carProfile.engine) }
    var power by remember { mutableStateOf(carProfile.power) }
    var horsepower by remember { mutableStateOf(carProfile.horsepower) }
    var imageUri by remember { mutableStateOf(carProfile.imageUri?.let { Uri.parse(it) }) }
    var permissionRequested by remember { mutableStateOf(false) }

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
        modifier = Modifier
            .padding(16.dp)
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

        OutlinedTextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Marca") },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            OutlinedTextField(
                value = model,
                onValueChange = { model = it },
                label = { Text("Modello") },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(end = 8.dp)
            )


            OutlinedTextField(
                value = engine,
                onValueChange = { engine = it },
                label = { Text("Cilindrata") },
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        Row {

            OutlinedTextField(
                value = power,
                onValueChange = { power = it },
                label = { Text("Potenza") },
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(end = 8.dp)
            )


            OutlinedTextField(
                value = horsepower,
                onValueChange = { horsepower = it },
                label = { Text("Cavalli") },
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }


        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Button(onClick = {
                setViewModel.updateCarProfile(CarProfile(brand, model, engine, power, horsepower, imageUri?.path))
                navController.navigate("ProfileScreen")
            }) {
                Text("Salva")
            }

        }


    }
}



