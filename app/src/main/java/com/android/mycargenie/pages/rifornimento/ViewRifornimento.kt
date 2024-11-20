package com.android.mycargenie.pages.rifornimento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    rifViewModel: RifViewModel,
    navController: NavController,
) {
    val rifIndex = navController.currentBackStackEntry?.arguments?.getInt("index")

    var showDeleteDialog by remember { mutableStateOf(false) }

    val rifItem = rifIndex?.takeIf { it in state.rifs.indices }?.let { state.rifs[it] }


    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SmallFloatingActionButton(onClick = {
                    showDeleteDialog = true
                },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "${stringResource(R.string.delete)} ${stringResource(R.string.refueling)}"
                    )
                }

                FloatingActionButton(onClick = {
                    navController.navigate("EditRifScreen/$rifIndex")
                },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "${stringResource(R.string.edit)} ${stringResource(R.string.refueling)}"
                    )
                }
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.padding(paddingValues)) {
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text(text = stringResource(R.string.confirm_question))
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            rifItem?.let { item ->
                                rifViewModel.onEvent(RifEvent.DeleteRif(item))
                                showDeleteDialog = false
                                navController.navigate("RifornimentoScreen")
                            }
                        }) {
                            Text(stringResource(R.string.delete))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteDialog = false }) {
                            Text(stringResource(R.string.cancel_up_low))
                        }
                    }
                )
            }
        }


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
                            text = "${stringResource(R.string.amount)}: ${stringResource(R.string.value_euro, price)}",
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
                            stringResource(R.string.electric) -> "${stringResource(R.string.eur_kwh)}: $uPrice"
                            else -> "${stringResource(R.string.eur_l)}: $uPrice"
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
                            stringResource(R.string.electric) -> "${stringResource(R.string.kWh)} ${stringResource(R.string.charged)}: $units"
                            else -> "${stringResource(R.string.l)} ${stringResource(R.string.refueled_s)}: $units"
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
                        text = "${stringResource(R.string.notes)}: ${rifItem.note}",
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
                            text = "${stringResource(R.string.km_maiusc)} ${stringResource(R.string.vehicle)}: $kmt ${stringResource(R.string.km_lower)}",
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
