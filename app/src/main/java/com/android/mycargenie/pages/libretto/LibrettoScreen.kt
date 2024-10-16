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
import com.android.mycargenie.pages.rifornimento.RifState
import com.android.mycargenie.shared.formatKmt
import com.android.mycargenie.shared.formatPrice

@Composable
fun LibrettoScreen(
    state: ManState,
    rifState: RifState,
    navController: NavController
) {

    val sortedMen = state.men.sortedByDescending { it.id }
    val sortedRif = rifState.rifs.sortedByDescending { it.id }


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

        Spacer(modifier = Modifier.height(38.dp))


        if (sortedMen.isEmpty()) {
            Row(
                modifier = Modifier
                    .padding(top = 32.dp)
            ) {
                Text(
                    text = "Aggiungi la tua prima manutenzione per visualizzare un resoconto.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }


        if (sortedMen.isNotEmpty()) {

            val currentItem = sortedMen.first()
            val index = state.men.indexOf(currentItem)

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

                Column(
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {


                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    ) {
                        Column {
                            Icon(
                                imageVector = icon,
                                contentDescription = currentItem.type,
                                modifier = Modifier
                                    .size(34.dp)
                                    .padding(end = 4.dp),
                                tint = MaterialTheme.colorScheme.primary
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
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = currentItem.date,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }


                    //Luogo

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    ) {
                        val place = currentItem.place

                        if (place.isNotEmpty()) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.location),
                                contentDescription = "Luogo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(34.dp)
                                    .padding(end = 4.dp),
                            )

                            Text(
                                text = currentItem.place,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .height(34.dp)
                            )
                        }
                    }

                    //Kilometri

                    val kmt = formatKmt(currentItem.kmt)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.time_to_leave),
                            contentDescription = "Icona automobile",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(34.dp)
                                .padding(end = 4.dp),
                        )

                        Text(
                            text = "$kmt km",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

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


        Spacer(modifier = Modifier.height(16.dp))

        if (sortedRif.isEmpty()) {
            Row(
                modifier = Modifier
                    .padding(top = 32.dp)
            ) {
                Text(
                    text = "Aggiungi il tuo primo rifornimento per visualizzare un resoconto.",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
            //return@Column
        }

        if (sortedRif.isNotEmpty()) {
            val currentRifItem = sortedRif.first()
            val rifIndex = rifState.rifs.indexOf(currentRifItem)


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(12.dp)
                    .clickable {
                        navController.navigate("ViewRifScreen/$rifIndex")
                    }
            ) {

                //Icona tipo
                val icon = when (currentRifItem.type) {
                    "Elettrico" -> ImageVector.vectorResource(id = R.drawable.electric)
                    else -> ImageVector.vectorResource(id = R.drawable.oil)
                }

                Column(
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    ) {
                        Column {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(34.dp)
                                    .padding(end = 4.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }



                        Column {

                            //Data
                            Text(
                                text = currentRifItem.date,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            //Prezzo
                            val price = formatPrice(currentRifItem.price)

                            Text(
                                text = "$price €",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }

                    //Prezzo per unità
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val uvalue =
                                formatPrice(currentRifItem.uvalue).replace('.', ',')

                            val unitprice = if (currentRifItem.type == "Elettrico") {
                                "$uvalue €/kWh"
                            } else {
                                "$uvalue €/l"
                            }

                            Text(
                                text = unitprice,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }


                    //Luogo
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val place = currentRifItem.place

                        if (place.isNotEmpty()) {

                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.location),
                                contentDescription = "Luogo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(34.dp)
                                    .padding(end = 4.dp),
                            )

                            Text(
                                text = currentRifItem.place,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Spacer(modifier = Modifier
                                .height(34.dp)
                            )
                        }

                        //Unità totali
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val totunit = formatPrice(currentRifItem.totunit)

                            val showunit = if (currentRifItem.type == "Elettrico") {
                                "$totunit kWh"
                            } else {
                                "$totunit l"
                            }

                            Text(
                                text = showunit,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                }
            }
        }
    }
    }
