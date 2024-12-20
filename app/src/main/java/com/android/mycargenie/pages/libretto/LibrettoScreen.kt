package com.android.mycargenie.pages.libretto

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.android.mycargenie.R

@Composable
fun LibrettoScreen(
    carProfile: CarProfile,
    navController: NavController
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    println(screenWidth)

    var backPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (backPressedOnce) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(4000)
            backPressedOnce = false
        }
    }

    BackHandler {
        if (backPressedOnce) {
            (context as? Activity)?.finish()
        } else {
            backPressedOnce = true
            Toast.makeText(context, "Premi di nuovo per chiudere.", Toast.LENGTH_SHORT).show()
        }
    }

    var localCarProfile by remember { mutableStateOf(carProfile) }

    LaunchedEffect(carProfile) {
        localCarProfile = carProfile
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("ProfileSettings")
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier
                    .padding(bottom = 0.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                    contentDescription = "${stringResource(R.string.settings)} ${stringResource(R.string.profile)}"
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            if (carProfile.brand.isEmpty()) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 100.dp)
                ) {
                    Text(
                        text = stringResource(R.string.configure_profile_message),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp)
                    )


                    Button(onClick = {
                        navController.navigate("ProfileSettings")
                    }) {
                        Text(stringResource(R.string.configure))
                    }
                }
            } else {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (carProfile.savedImagePath.isNotEmpty()) {
                        val imagePainter =
                            rememberAsyncImagePainter(model = carProfile.savedImagePath)

                        Image(
                            painter = imagePainter,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(
                                    when {
                                        screenWidth <= 360 -> 180.dp
                                        screenWidth <= 430 -> 220.dp
                                        else -> 250.dp
                                    }
                                ),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Column {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = carProfile.brand,
                            fontSize = 37.sp,
                            fontWeight = FontWeight.Bold
                        )

                        val modelFontSize = when {
                            carProfile.model.length < 5 -> 34.sp
                            carProfile.model.length < 10 -> 32.sp
                            carProfile.model.length < 15 -> 29.sp
                            carProfile.model.length < 20 -> 25.sp
                            else -> 24.sp
                        }

                        Text(
                            text = carProfile.model,
                            fontSize = modelFontSize,
                            fontWeight = FontWeight.SemiBold
                        )

                        if (carProfile.conf.isNotEmpty()) {

                            val confFontSize = when {
                                carProfile.conf.length < 10 -> 18.sp
                                carProfile.conf.length < 20 -> 16.sp
                                else -> 14.sp
                            }

                            Text(
                                text = carProfile.conf,
                                fontSize = confFontSize,
                            )
                        }

                    }


                    Spacer(modifier = Modifier.height(15.dp))


                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    ) {

                        val powerHorseFontSize = when {
                            carProfile.power.toString().length < 6 -> 24.sp
                            else -> 15.sp
                        }

                        val powerHorseFontScale = when {
                            screenWidth <= 360 -> 0.8f
                            else -> 1f
                        }

                        val tagPadding = when {
                            screenWidth <= 360 -> 0.dp
                            else -> 3.dp
                        }

                        //Cilindrata
                        if (carProfile.displacement != 0) {

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.displacement),
                                    fontSize = 14.sp
                                    )
                                Row(
                                    modifier = Modifier
                                        .border(
                                            border = ButtonDefaults.outlinedButtonBorder(),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                ) {

                                    Text(
                                        text = "${carProfile.displacement}",
                                        fontSize = powerHorseFontSize,
                                        modifier = Modifier
                                            .scale(powerHorseFontScale)
                                    )
                                    Text(
                                        text = stringResource(R.string.cc),
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(tagPadding, bottom = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        //Potenza
                        if (carProfile.power > 0.0) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.power),
                                    fontSize = 14.sp
                                )
                                Row(
                                    modifier = Modifier
                                        .border(
                                            border = ButtonDefaults.outlinedButtonBorder(),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${carProfile.power}",
                                        fontSize = powerHorseFontSize,
                                        modifier = Modifier
                                            .scale(powerHorseFontScale)
                                    )
                                    Text(
                                        text = stringResource(R.string.kW),
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(tagPadding, bottom = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        //Cavalli
                        if (carProfile.horsepower > 0.0) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.horses),
                                    fontSize = 14.sp
                                )
                                Row(
                                    modifier = Modifier
                                        .border(
                                            border = ButtonDefaults.outlinedButtonBorder(),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${carProfile.horsepower}",
                                        fontSize = powerHorseFontSize,
                                        modifier = Modifier
                                            .scale(powerHorseFontScale)
                                    )
                                    Text(
                                        text = stringResource(R.string.CV),
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(tagPadding, bottom = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier
                        .height(when {
                            screenWidth <= 360 -> 20.dp
                            screenWidth <= 430 -> 30.dp
                            screenWidth <= 450 -> 35.dp
                            else -> 40.dp
                        }))


                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, end = 32.dp)
                    ) {

                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .weight(1f)
                        ) {

                            //Tipo
                            val typeFontSize = when {
                                carProfile.type.length < 10 -> 24.sp
                                carProfile.type.length < 13 -> 18.sp
                                else -> 15.sp
                            }

                            val typeInnerPadding = when {
                                carProfile.type.length < 10 -> 12.dp
                                carProfile.type.length < 13 -> 14.dp
                                else -> 14.dp
                            }

                            if (carProfile.type.isNotEmpty()) {
                                Row {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.type),
                                            fontSize = 14.sp
                                        )
                                        Row(
                                            modifier = Modifier
                                                .border(
                                                    border = ButtonDefaults.outlinedButtonBorder(),
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(typeInnerPadding)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = carProfile.type,
                                                fontSize = typeFontSize,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }

                            //Alimentazione
                            if (carProfile.fuel.isNotEmpty()) {
                                Row {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.fuel),
                                            fontSize = 14.sp
                                        )
                                        Row(
                                            modifier = Modifier
                                                .border(
                                                    border = ButtonDefaults.outlinedButtonBorder(),
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(12.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = carProfile.fuel,
                                                fontSize = 24.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                        }
                                    }
                                }

                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))


                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {

                            //Anno
                            if (carProfile.year != 0) {
                                Row {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.year),
                                            fontSize = 14.sp
                                        )
                                        Row(
                                            modifier = Modifier
                                                .border(
                                                    border = ButtonDefaults.outlinedButtonBorder(),
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(12.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = carProfile.year.toString(),
                                                fontSize = 24.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }

                            //Inquinamento
                            if (carProfile.eco.isNotEmpty()) {
                                Row {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.eco),
                                            fontSize = 14.sp
                                        )
                                        Row(
                                            modifier = Modifier
                                                .border(
                                                    border = ButtonDefaults.outlinedButtonBorder(),
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(12.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = carProfile.eco,
                                                fontSize = 24.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
