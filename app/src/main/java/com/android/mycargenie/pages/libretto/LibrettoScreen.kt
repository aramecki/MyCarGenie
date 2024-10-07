package com.android.mycargenie.pages.libretto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R
import com.android.mycargenie.pages.manutenzione.ManState

@Composable
fun LibrettoScreen(
    state: ManState,
    navController: NavController,
    lastId: Int?
) {
    println("Ultimo ID passato a LibrettoScreen: $lastId")  // Aggiungi questo log
    println("Numero di elementi in state.men: ${state.men.size}")  // Aggiungi questo log

    val sortedMen = state.men.sortedByDescending { it.id }

    Column {

        Spacer(modifier = Modifier.height(80.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(start = 32.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.subaru_baracca),
                    contentDescription = "Automobile",
                    modifier = Modifier
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp)
            ) {
                Row {
                    Text(
                        text = "Subaru",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(top = 16.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Baracca",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {

                    Text(
                        text = "2.0",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(end = 4.dp)
                    )

                    Text(
                        text = "170kW",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(end = 4.dp)
                    )

                    Text(
                        text = "230CV",
                        fontSize = 16.sp
                    )
                }
            }
        }

        data class Man(
            val id: Int,
            val title: String,
            val type: String,
            // Aggiungi altre proprietà se necessario
        ) {
            override fun toString(): String {
                return "Man(id=$id, title='$title', type='$type')"
            }
        }


        // Visualizzazione del caricamento se la lista è vuota
        if (sortedMen.isEmpty()) {
            Text(
                text = "Caricamento in corso...",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp
            )
            return@Column // Interrompi l'esecuzione della funzione se la lista è vuota
        } else {
            // Log dei contenuti della lista
            println("Contenuto della lista sortedMen:")
            sortedMen.forEachIndexed { index, man ->
                println("Elemento $index: $man")
            }
        }

        // Mostra il contenuto della card solo se lastId è valido
        if (sortedMen.isNotEmpty()) {
            // Prendi sempre il primo elemento della lista ordinata
            val currentItem = sortedMen.first() // Oppure, se vuoi il primo elemento quando lastId è 6: sortedMen[5]

            // Log per mostrare l'elemento attuale
            println("Mostro la card con id: ${currentItem.id}, Dettagli dell'oggetto: $currentItem")

                Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(12.dp)
                    .clickable {
                        navController.navigate("ViewManScreen/$lastId")
                    }
            ) {

                val icon = when (currentItem.type) {
                    "Meccanico" -> ImageVector.vectorResource(id = R.drawable.manufacturing)
                    "Elettrauto" -> ImageVector.vectorResource(id = R.drawable.lightbulb)
                    "Carrozziere" -> ImageVector.vectorResource(id = R.drawable.brush)
                    else -> ImageVector.vectorResource(id = R.drawable.repair)
                }


                Icon(
                    imageVector = icon,
                    contentDescription = currentItem.type,
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
                        text = currentItem.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

/*
                    Spacer(modifier = Modifier.height(8.dp))

                    //Luogo
                    Text(
                        text = state.men[lastId].place,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    //Data
                    Text(
                        text = state.men[lastId].date,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = state.men[lastId].kmt.toString(),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )


                    Spacer(modifier = Modifier.height(8.dp))

                    val price = state.men[lastId].price.toString().replace('.', ',')

                    Text(
                        text = "$price €",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .align(Alignment.End)
                    )
*/
                }
            }
        } else {
            println("Id non mostrato: $lastId")
            Text(
                text = "Aggiungi la tua prima manutenzione per mostrare un riepilogo.",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 60.dp)
            )
            }
        }
    }