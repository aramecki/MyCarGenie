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
import java.text.DecimalFormat

@Composable
fun LibrettoScreen(
    state: ManState,
    navController: NavController
) {

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


        if (sortedMen.isEmpty()) {
            Text(
                text = "Aggiungi la tua prima manutenzione per visualizzare un resoconto.",
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            return@Column
        } else {
            sortedMen.forEachIndexed { _, _ ->
            }
        }

        if (sortedMen.isNotEmpty()) {
            val currentItem = sortedMen.first()
            val index = state.men.indexOf(currentItem)


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
                                }

                        //Kilometri

                        val formatter = DecimalFormat("#,###")
                        val formattedKmt = formatter.format(currentItem.kmt)


                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.time_to_leave),
                                contentDescription = "Data",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(34.dp)
                                    .padding(end = 4.dp),
                                )

                            Text(
                                text = "$formattedKmt km",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                val decimalFormat = DecimalFormat("#,##0.00")
                                val price = decimalFormat.format(state.men[index].price).replace('.', ',')

                                Text(
                                    text = "$price €",
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                    }
                }

        } else {
            Text(
                text = "Aggiungi la tua prima manutenzione per mostrare un riepilogo.",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 60.dp)
            )
        }
    }
}