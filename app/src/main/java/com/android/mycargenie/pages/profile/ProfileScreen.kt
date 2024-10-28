package com.android.mycargenie.pages.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

data class CarProfile(
    var brand: String,
    var model: String,
    var displacement: Int,
    var power: Float,
    var horsepower: Float,
    var savedImagePath: String,
    var type: String,
    var fuel: String,
    var year: Int,
    var eco: String,
    var conf: String
)

@Composable
fun ProfileScreen(
    carProfile: CarProfile,
    navController: NavController
) {

    var localCarProfile by remember { mutableStateOf(carProfile) }

    LaunchedEffect(carProfile) {
        localCarProfile = carProfile
    }

    Column(
        modifier = Modifier
            .imePadding()
            .verticalScroll(rememberScrollState())

    ) {


        Spacer(modifier = Modifier.height(10.dp))

        if (carProfile.brand != "") {

            Row {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        navController.navigate("ProfileSettings")
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                            contentDescription = "Impostazioni",
                            modifier = Modifier
                                .size(42.dp)
                                .padding(top = 8.dp, end = 8.dp),
                        )
                    }
                }
            }

        }


        Spacer(modifier = Modifier.height(10.dp))

        if (carProfile.brand == "") {

            Spacer(modifier = Modifier.height(100.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Configura le informazioni sul tuo veicolo per visualizzarne il profilo.",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(8.dp)
                )


                Button(onClick = {
                    navController.navigate("ProfileSettings")
                }) {
                    Text("Configura")
                }
            }

        }


        if (carProfile.brand != "") {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                //.padding(start = 32.dp)
            ) {
                if (carProfile.savedImagePath != "") {
                    val imagePainter = rememberAsyncImagePainter(model = carProfile.savedImagePath)

                    Image(
                        painter = imagePainter,
                        contentDescription = "Automobile",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(250.dp),
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
                    carProfile.model.length < 5 -> 35.sp
                    carProfile.model.length < 10 -> 33.sp
                    carProfile.model.length < 15 -> 31.sp
                    carProfile.model.length < 20 -> 29.sp
                    else -> 27.sp
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

                //Cilindrata
                if (carProfile.displacement != 0) {

                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Cilindrata",
                            fontSize = 14.sp,

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
                                text = "${carProfile.displacement}",
                                fontSize = 23.sp,
                            )
                            Text(
                                text = "cc",
                                fontSize = 11.sp
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
                            text = "Potenza",
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
                                text = "${carProfile.power}",
                                fontSize = 23.sp,
                            )
                            Text(
                                text = "kW",
                                fontSize = 11.sp
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
                            text = "Cavalli",
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
                                text = "${carProfile.horsepower}",
                                fontSize = 23.sp
                            )
                            Text(
                                text = "CV",
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(35.dp))


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
                    if (carProfile.type.isNotEmpty()) {
                        Row {
                            Column {
                                Text(
                                    text = "Tipo",
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
                                        text = carProfile.type,
                                        fontSize = 24.sp,
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
                                    text = "Alimentazione",
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
                                    text = "Anno",
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


                    if (carProfile.eco.isNotEmpty()) {
                        //Inquinamento

                        Row {
                            Column {
                                Text(
                                    text = "Inquinamento",
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
