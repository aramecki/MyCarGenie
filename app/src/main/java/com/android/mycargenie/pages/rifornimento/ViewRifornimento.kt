package com.android.mycargenie.pages.rifornimento

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
import androidx.compose.ui.res.stringResource
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
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    //Icona tipo
                    val icon = when (rifItem.type) {
                        stringResource(R.string.electric) -> ImageVector.vectorResource(id = R.drawable.electric)
                        else -> ImageVector.vectorResource(id = R.drawable.oil)
                    }


                    Row(
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                    ) {
                        val price = formatPrice(rifItem.price)
                        Text(
                            text = "${R.string.amount}: ${stringResource(R.string.value_euro, price)}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 18.sp
                            )
                        )

                    }

                    //Prezzo per unità
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val uPrice = formatPrice(rifItem.uvalue)

                        val unitValue = when (rifItem.type) {
                            stringResource(R.string.electric) -> "${R.string.eur_kwh}: $uPrice"
                            else -> "${R.string.eur_l}: $uPrice"
                        }

                        Text(
                            text = unitValue,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp
                            )
                        )

                        //Unità totali

                        val units = formatPrice(rifItem.totunit)

                        val unitsText = when (rifItem.type) {
                            stringResource(R.string.electric) -> "${R.string.kWh} ${R.string.charged}: $units"
                            else -> "${R.string.l} ${R.string.refueled_s}: $units"
                        }

                        Text(
                            text = unitsText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 20.sp
                            ),
                            modifier = Modifier
                                .padding(top = 32.dp)
                        )

                    }
                }

                //Note
                if (rifItem.note.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(52.dp))

                    Text(
                        text = "${R.string.notes}: ${rifItem.note}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp
                        )
                    )
                }



                Spacer(modifier = Modifier.height(52.dp))


                Row {
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        //Kilometri

                        val kmt = formatKmt(rifItem.kmt)

                        Text(
                            text = "${R.string.km_maiusc} ${R.string.vehicle}: $kmt ${R.string.km_lower}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp
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
                    text = stringResource(R.string.element_not_found_err),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
