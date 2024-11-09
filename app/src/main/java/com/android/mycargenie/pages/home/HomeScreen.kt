package com.android.mycargenie.pages.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.android.mycargenie.R
import com.android.mycargenie.pages.libretto.CarProfile
import com.android.mycargenie.pages.manutenzione.ManState
import com.android.mycargenie.pages.rifornimento.RifState
import com.android.mycargenie.shared.formatDisplacement
import com.android.mycargenie.shared.formatKmt
import com.android.mycargenie.shared.formatPrice

@Composable
fun HomeScreen(
    manState: ManState,
    rifState: RifState,
    homeViewModel: HomeViewModel,
    carProfile: CarProfile,
    navController: NavController
) {

    val lastManutenzione by homeViewModel.lastManutenzione.collectAsState()
    val lastRifornimento by homeViewModel.lastRifornimento.collectAsState()

    var localCarProfile by remember { mutableStateOf(carProfile) }

    LaunchedEffect(carProfile) {
        localCarProfile = carProfile
    }

    /*
    Box {
        Image(
            painter = painterResource(id = R.drawable.filigrana),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )

     */

    Column {

        Spacer(modifier = Modifier.height(40.dp))

        if (carProfile.brand.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (carProfile.savedImagePath != "") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(start = 32.dp)
                    ) {
                        val imagePainter =
                            rememberAsyncImagePainter(model = carProfile.savedImagePath)

                        Image(
                            painter = imagePainter,
                            contentDescription = "Automobile",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(160.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp)
                ) {
                    Row {
                        Text(
                            text = carProfile.brand,
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
                            text = carProfile.model,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row {

                        if (carProfile.displacement != 0) {
                            val displacement = formatDisplacement(carProfile.displacement)

                            Text(
                                text = displacement,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(end = 4.dp)
                            )
                        }

                        if (carProfile.power != 0.0f) {
                            Text(
                                text = carProfile.power.toInt().toString(),
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(end = 4.dp)
                            )
                        }

                        if (carProfile.horsepower != 0.0f) {
                            Text(
                                text = carProfile.horsepower.toInt().toString(),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(38.dp))

        Column {
            if (lastManutenzione?.title.isNullOrEmpty() && lastRifornimento?.price?.toString()
                    .isNullOrEmpty()
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 180.dp)
                ) {
                    Text(
                        text = "Questa pagina si aggiorna automaticamente man mano che usi l’app. Aggiungi eventi e scadenze nelle sezioni dedicate: così troverai qui le informazioni più utili.",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                            .alpha(0.7f)
                    )
                }

            }
        }

        if (carProfile.brand == "") {
            Text(
                text = "Per iniziare, aggiungi i dati del tuo veicolo.",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .alpha(0.7f)
            )

            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Button(onClick = {
                    navController.navigate("ProfileSettings")
                }) {
                    Text(
                        text = "Configura",
                        fontSize = 16.sp
                    )
                }
            }
        }


        lastManutenzione?.let { manutenzione ->

            val index = manState.men.indexOf(manutenzione)

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

                val icon = when (manutenzione.type) {
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
                                contentDescription = manutenzione.type,
                                modifier = Modifier
                                    .size(34.dp)
                                    .padding(end = 4.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }


                        Column {

                            //Titolo
                            Text(
                                text = manutenzione.title,
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
                                text = manutenzione.date,
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
                        val place = manutenzione.place

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
                                text = manutenzione.place,
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

                    val kmt = formatKmt(manutenzione.kmt)

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
                            val price = formatPrice(manState.men[index].price)

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
        /*?: Row(
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

                 */


        Spacer(modifier = Modifier.height(16.dp))


        //RIFORNIMENTO
        lastRifornimento?.let { rifornimento ->

            val rifIndex = rifState.rifs.indexOf(rifornimento)

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
                val icon = when (rifornimento.type) {
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
                                text = rifornimento.date,
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
                            val price = formatPrice(rifornimento.price)

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
                                formatPrice(rifornimento.uvalue).replace('.', ',')

                            val unitprice = if (rifornimento.type == "Elettrico") {
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
                        val place = rifornimento.place

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
                                text = rifornimento.place,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .height(34.dp)
                            )
                        }

                        //Unità totali
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val totunit = formatPrice(rifornimento.totunit)

                            val showunit = if (rifornimento.type == "Elettrico") {
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



