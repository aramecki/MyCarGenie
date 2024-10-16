package com.android.mycargenie.pages.rifornimento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R
import com.android.mycargenie.shared.formatKmt
import com.android.mycargenie.shared.formatPrice

@Composable
fun ViewRifScreen(
    state: RifState,
    navController: NavController,
) {
    val rifIndex = navController.currentBackStackEntry?.arguments?.getInt("index")

    val rifItem = rifIndex?.takeIf { it in state.rifs.indices }?.let { state.rifs[it] }


    Scaffold { paddingValues ->

        if (rifItem != null) {
            Column(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = paddingValues.calculateBottomPadding()
                    )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    //Icona tipo
                    val icon = when (rifItem.type) {
                        "Elettrico" -> ImageVector.vectorResource(id = R.drawable.electric)
                        else -> ImageVector.vectorResource(id = R.drawable.oil)
                    }


                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Column {
                            Icon(
                                imageVector = icon,
                                contentDescription = rifItem.type,
                                modifier = Modifier
                                    .size(45.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        //Data
                        Column {
                            Text(
                                text = rifItem.date,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontSize = 30.sp
                                ),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                            )
                        }


                        //Spacer(modifier = Modifier.height(30.dp))

                        /*
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {


                                    Column {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.DateRange,
                                                contentDescription = "Data",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier
                                                    .size(30.dp)
                                            )
                                            Text(
                                                text = rifItem.date,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier
                                                    .padding(start = 4.dp)
                                            )
                                        }
                                    }


                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                //.fillMaxWidth()
                        ) {
                            /*
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                             */
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.location),
                                contentDescription = "Luogo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(24.dp)
                            )
                        }

                         */

                        Column(
                            modifier = Modifier
                                .padding(start = 4.dp)
                        ) {
                            Text(
                                text = rifItem.place,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                    }

                }



                Spacer(modifier = Modifier.height(32.dp))

                Row {
                    //Prezzo
                    val price = formatPrice(rifItem.price)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "$price €",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 20.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Note: ${rifItem.note}",
                    style = MaterialTheme.typography.bodyMedium
                )



                Spacer(modifier = Modifier.height(32.dp))


                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                    ) {
                        //Kilometri

                        val kmt = formatKmt(rifItem.kmt)

                        Text(
                            text = "$kmt km",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp
                            )
                        )
                    }


                }



            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Errore: Elemento non trovato.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
