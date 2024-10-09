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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
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
import java.text.DecimalFormat

@Composable
fun LibrettoScreen(
    state: ManState,
    navController: NavController,
    lastId: Int?
) {
    //println("Ultimo ID passato a LibrettoScreen: $lastId")  // Aggiungi questo log
    //println("Numero di elementi in state.men: ${state.men.size}")  // Aggiungi questo log

    val sortedMen = state.men.sortedByDescending { it.id }

    Column {

        Spacer(modifier = Modifier.height(70.dp))

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


        // Visualizzazione del caricamento se la lista è vuota
        if (sortedMen.isEmpty()) {
            Text(
                text = "Aggiungi la tua prima manutenzione per visualizzare un resoconto.",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            return@Column // Interrompi l'esecuzione della funzione se la lista è vuota
        } else {
            // Log dei contenuti della lista
            //println("Contenuto della lista sortedMen:")
            sortedMen.forEachIndexed { index, man ->
                //println("Elemento $index: $man")
            }
        }

        // Mostra il contenuto della card solo se lastId è valido
        if (sortedMen.isNotEmpty()) {
            // Prendi sempre il primo elemento della lista ordinata
            val currentItem = sortedMen.first()
            val index = state.men.indexOf(currentItem)

            // Log per mostrare l'elemento attuale
            //println("Mostro la card con id: ${currentItem.id}, Dettagli dell'oggetto: $currentItem")

            Spacer(modifier = Modifier.height(30.dp))

                Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(12.dp)
                    .clickable {
                        navController.navigate("ViewManScreen/$index")
                    }
            ) {

                val icon = when (currentItem.type) {
                    "Meccanico" -> ImageVector.vectorResource(id = R.drawable.manufacturing)
                    "Elettrauto" -> ImageVector.vectorResource(id = R.drawable.lightbulb)
                    "Carrozziere" -> ImageVector.vectorResource(id = R.drawable.brush)
                    else -> ImageVector.vectorResource(id = R.drawable.repair)
                }

                    Column {


                    Row {
                        Column {
                            Icon(
                                imageVector = icon,
                                contentDescription = currentItem.type,
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(end = 8.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }


                        Column {

                            //Titolo
                            Text(
                                text = currentItem.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }




                        Row(
                            modifier = Modifier
                                .padding(top = 8.dp)
                        ) {

                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.DateRange,
                                        contentDescription = "Data",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .size(24.dp)
                                    )
                                    Text(
                                        text = currentItem.date,
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }


                            //Luogo
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource



                                            (id = R.drawable.location),
                                        contentDescription = "Luogo",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .size(24.dp)
                                    )

                                    Text(
                                        text = currentItem.place,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }

                        }


                        Row(
                            modifier = Modifier
                                .padding(top = 16.dp)
                        ) {

                            Column(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                            ) {
                                val formatter = DecimalFormat("#,###")
                                val formattedKmt = formatter.format(currentItem.kmt)

                                Text(
                                    text = "$formattedKmt km",
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                val price = currentItem.price.toString().replace('.', ',')

                                Row {
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
        } else {
            //println("Id non mostrato: $lastId")
            Text(
                text = "Aggiungi la tua prima manutenzione per mostrare un riepilogo.",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 60.dp)
            )
            }
        }
    }