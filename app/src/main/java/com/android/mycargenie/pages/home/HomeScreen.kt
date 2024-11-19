package com.android.mycargenie.pages.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
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

    var isExpanded by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    FloatingActionButton(onClick = {
                        navController.navigate("AddManScreen")
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.time_to_leave),
                            contentDescription = stringResource(R.string.maintenance)
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    FloatingActionButton(onClick = {
                        navController.navigate("AddRifScreen")
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.gas_station),
                            contentDescription = stringResource(R.string.refueling)
                        )
                    }
                }

                FloatingActionButton(onClick = {
                    isExpanded = !isExpanded
                }) {
                    Icon(
                        imageVector =if (isExpanded) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.settings)
                    )
                }
            }
        }
    ) { padding ->

        Spacer(
            modifier = Modifier
                .height(16.dp)
                .padding(padding)
        )

        Column {

            Spacer(modifier = Modifier.height(40.dp))

            if (carProfile.brand.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (carProfile.savedImagePath.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(start = 18.dp)
                        ) {
                            val imagePainter =
                                rememberAsyncImagePainter(model = carProfile.savedImagePath)

                            Image(
                                painter = imagePainter,
                                contentDescription = stringResource(R.string.car),
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
                            .padding(start = 16.dp)
                    ) {

                        val brandFontSize = when {
                            carProfile.brand.length < 10 -> 22.sp
                            carProfile.brand.length < 12 -> 20.sp
                            else -> 19.sp
                        }

                        Row {
                            Text(
                                text = carProfile.brand,
                                fontSize = brandFontSize,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(top = 16.dp)
                            )
                        }

                        val modelFontSize = when {
                            carProfile.model.length < 6 -> 34.sp
                            carProfile.model.length < 8 -> 32.sp
                            carProfile.model.length < 10 -> 24.sp
                            carProfile.model.length < 12 -> 17.sp
                            carProfile.model.length < 14 -> 15.sp
                            carProfile.model.length < 18 -> 13.sp
                            carProfile.model.length < 20 -> 11.sp
                            else -> 10.sp
                        }

                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = carProfile.model,
                                fontSize = modelFontSize,
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
                            text = stringResource(R.string.home_message1),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                                .alpha(0.7f)
                        )
                    }

                }


                if (carProfile.brand.isEmpty()) {
                    Text(
                        text = stringResource(R.string.home_message2),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                            .alpha(0.7f)
                    )

                    Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Button(onClick = {
                            navController.navigate("ProfileSettings")
                        }) {
                            Text(
                                text = stringResource(R.string.configure),
                                fontSize = 16.sp
                            )

                        }
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
                        stringResource(R.string.mechanic) -> ImageVector.vectorResource(id = R.drawable.manufacturing)
                        stringResource(R.string.electrician) -> ImageVector.vectorResource(id = R.drawable.lightbulb)
                        stringResource(R.string.coachbuilder) -> ImageVector.vectorResource(id = R.drawable.brush)
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
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
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
                                    contentDescription = stringResource(R.string.place),
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
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(34.dp)
                                    .padding(end = 4.dp),
                            )

                            Text(
                                text = stringResource(R.string.value_km, kmt),
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
                                    text = stringResource(R.string.value_euro, price),
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                    }
                }

            }

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
                        stringResource(R.string.electric) -> ImageVector.vectorResource(id = R.drawable.electric)
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
                                    text = stringResource(R.string.value_euro, price),
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
                                    formatPrice(rifornimento.uvalue)

                                val unitprice =
                                    if (rifornimento.type == stringResource(R.string.electric)) {
                                        stringResource(R.string.value_eur_kWh, uvalue)
                                    } else {
                                        stringResource(R.string.value_euro_liter, uvalue)
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
                                    contentDescription = stringResource(R.string.place),
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

                                val showunit =
                                    if (rifornimento.type == stringResource(R.string.electric)) {
                                        stringResource(R.string.value_kwh, totunit)
                                    } else {
                                        stringResource(R.string.value_l, totunit)
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
}



