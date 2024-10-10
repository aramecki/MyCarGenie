package com.android.mycargenie.pages.manutenzione

import android.annotation.SuppressLint
import android.content.res.Configuration
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.android.mycargenie.R
import com.android.mycargenie.data.Man
import com.android.mycargenie.ui.theme.MyCarGenieTheme
import java.text.DecimalFormat

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


        Icon(
            imageVector = icon,
            contentDescription = state.men[index].type,
            modifier = Modifier
                .size(30.dp)
                .padding(end = 8.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            //Titolo
            Text(
                text = state.men[index].title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )


            Spacer(modifier = Modifier.height(8.dp))

            //Luogo
            Text(
                text = state.men[index].place,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            //Data
            Text(
                text = state.men[index].date,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = state.men[index].kmt.toString(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            val decimalFormat = DecimalFormat("#,##0.00")
            val price = decimalFormat.format(state.men[index].price).replace('.', ',')

            Text(
                text = "$price €",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .align(Alignment.End)
            )

        }

        /*
        IconButton(
            onClick = {
                onEvent(ManEvent.DeleteMan(state.men[index]))
            }
        ) {

            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Elimina Manutenzione",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )

        }

         */

    }
}

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