package com.android.mycargenie.pages.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
    var engine: String,
    var power: String,
    var horsepower: String,
    var imageUri: String? = null
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

    Column {


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


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    //.padding(start = 32.dp)
            ) {
                if (carProfile.imageUri != null) {
                    val imagePainter = rememberAsyncImagePainter(model = carProfile.imageUri)

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


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = carProfile.brand,
                fontSize = 34.sp,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = carProfile.model,
                fontSize = 42.sp,
                //fontWeight = FontWeight.SemiBold
            )
        }


        Spacer(modifier = Modifier.height(10.dp))


            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            ) {

                Column(
                    modifier = Modifier
                        //.fillMaxWidth(0.5f)
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
                    ) {

                        Text(
                            text = "${carProfile.engine}cc",
                            fontSize = 24.sp,
                        )

                    }
                }


                Column(
                    modifier = Modifier
                        //.padding(start = 32.dp)
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
                    ) {
                        Text(
                            text = "${carProfile.power}kW",
                            fontSize = 24.sp,
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        //.padding(start = 32.dp)
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
                    ) {
                        Text(
                            text = "${carProfile.horsepower}CV",
                            fontSize = 24.sp
                        )
                    }
                }

            }



            Spacer(modifier = Modifier.height(34.dp))


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier
                        //.fillMaxWidth()
                ) {

                    //Tipo
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            //.padding(start = 32.dp)
                    ) {
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
                        ) {
                            Text(
                                text = "Berlina",
                                fontSize = 24.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            //.padding(start = 32.dp)
                    ) {
                        //Alimentazione
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
                        ) {
                            Text(
                                text = "Elettrica",
                                fontSize = 24.sp
                            )
                        }
                    }

                }


                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                ) {
                    //Immatricolazione
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            //.padding(start = 32.dp)
                    ) {
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
                        ) {
                            Text(
                                text = "2006",
                                fontSize = 24.sp
                            )
                        }
                    }


                    //Inquinamento
                    Column(
                        modifier = Modifier
                            //.padding(start = 32.dp)
                    ) {
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
                        ) {
                            Text(
                                text = "Euro 4",
                                fontSize = 24.sp
                            )
                        }
                    }
                }

            }




    }
}