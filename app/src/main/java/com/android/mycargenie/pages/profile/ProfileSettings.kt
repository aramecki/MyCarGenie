package com.android.mycargenie.pages.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.mycargenie.pages.settings.SetViewModel

@Composable
fun ProfileSettingsScreen(
    carProfile: CarProfile,
    navController: NavController,
    onProfileChange: (CarProfile) -> Unit, // Assicurati che questo parametro sia presente
    setViewModel: SetViewModel
) {

    var brand by remember { mutableStateOf(carProfile.brand) }
    var model by remember { mutableStateOf(carProfile.model) }
    var engine by remember { mutableStateOf(carProfile.engine) }
    var power by remember { mutableStateOf(carProfile.power) }
    var horsepower by remember { mutableStateOf(carProfile.horsepower) }

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
            setViewModel.updateCarProfile(CarProfile(brand, model, engine, power, horsepower))
            navController.navigate("ProfileScreen")
        }) {
            Text("Salva")
        }
    }
}