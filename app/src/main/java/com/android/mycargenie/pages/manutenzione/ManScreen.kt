package com.android.mycargenie.pages.manutenzione

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R
import com.android.mycargenie.shared.formatKmt
import com.android.mycargenie.shared.formatPrice

@Composable
fun ManutenzioneScreen(
    state: ManState,
    navController: NavController,
    onEvent: (ManEvent) -> Unit
) {


    Scaffold(

        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Manutenzione", //Da inserire in strings
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )


                IconButton(onClick = { onEvent(ManEvent.SortMan) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.sort),
                        contentDescription = "Ordina Manutenzione",
                        modifier = Modifier.size(35.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                state.title.value = ""
                state.type.value = ""
                state.place.value = ""
                state.date.value = ""
                state.kmt.value = 0
                state.description.value = ""
                state.price.value = 0.0
                navController.navigate("AddManScreen")
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Aggiungi Manutenzione")
            }
        }
    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(state.men.size) { index ->
                ManItem(
                    state = state,
                    index = index,
                    navController = navController
                )
            }

        }

    }

}

@Composable
fun ManItem(
    state: ManState,
    index: Int,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
            .clickable {
                navController.navigate("ViewManScreen/$index")
            }
    ) {
        val icon = when (state.men[index].type) {
            "Meccanico" -> ImageVector.vectorResource(id = R.drawable.manufacturing)
            "Elettrauto" -> ImageVector.vectorResource(id = R.drawable.lightbulb)
            "Carrozziere" -> ImageVector.vectorResource(id = R.drawable.brush)
            else -> ImageVector.vectorResource(id = R.drawable.repair)
        }

        Column(
            verticalArrangement = Arrangement.SpaceEvenly
        ) {


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = state.men[index].type,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(end = 4.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                Column {

                    //Titolo
                    Text(
                        text = state.men[index].title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                //Data
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = state.men[index].date,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }


            //Luogo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            ) {
                val place = state.men[index].place

                if (place.isNotEmpty()) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.location),
                        contentDescription = "Luogo",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(34.dp)
                            .padding(end = 4.dp),
                    )
                    Text(
                        text = state.men[index].place,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    Spacer(modifier = Modifier
                        .height(34.dp)
                    )
                }
            }


            //Kilometri

            val kmt = formatKmt(state.men[index].kmt)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.time_to_leave),
                    contentDescription = "Data",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(end = 4.dp),
                )

                Text(
                    text = "$kmt km",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )


                //Prezzo
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val price = formatPrice(state.men[index].price)

                    Text(
                        text = "$price €",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
    }
}

    /*
@SuppressLint("UnrememberedMutableState")
@Preview(
    name = "Light Mode",
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun PreviewManutenzioneScreen() {
    // Stato di esempio per la preview
    val exampleState = ManState(
        title = mutableStateOf("Manutenzione Auto"),
        type = mutableStateOf("Meccanico"),
        place = mutableStateOf("Garage ABC"),
        date = mutableStateOf("10/10/2024"),
        kmt = mutableIntStateOf(45000),
        description = mutableStateOf("Cambio olio e controllo freni"),
        price = mutableDoubleStateOf(150.0),
        men = listOf(
            Man("Cambio olio", "Meccanico", "Garage 1", "10/10/2024", 45000, "Cambio completo olio", 100.0),
            Man("Revisione freni", "Elettrauto", "Garage 2", "11/11/2024", 45500, "Revisione pastiglie freni", 150.0)
        )
    )

    val navController = rememberNavController()

    val onEvent: (ManEvent) -> Unit = { event ->
        when (event) {
            is ManEvent.SaveMan -> {
                println("Salvato: ${event.title}, ${event.date}, ${event.place}, ${event.description}")
            }
            is ManEvent.UpdateMan -> {
                println("Aggiornato: ${event.id}, ${event.title}, ${event.date}, ${event.place}, ${event.description}")
            }
            is ManEvent.DeleteMan -> {
                println("Elimina: ${event.man.title}")
            }
            ManEvent.SortMan -> {
                println("Ordina Manutenzione")
            }
        }
    }

    MyCarGenieTheme {
        ManutenzioneScreen(
            state = exampleState,
            navController = navController,
            onEvent = onEvent
        )
    }
}

     */