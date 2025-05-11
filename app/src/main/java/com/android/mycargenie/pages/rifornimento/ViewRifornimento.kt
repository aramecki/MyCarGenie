package com.android.mycargenie.pages.rifornimento

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 150.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 14.dp, bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.back),
                        contentDescription = "Back to refueling view screen", //To put in strings xml
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable {
                                navController.navigate("RifornimentoScreen")
                            }
                    )

                    Text(
                        text = rifItem.date,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(start = 24.dp)
                    )
                }

                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .alpha(0.2f)
                        .padding(bottom = 16.dp)
                )

                //Icona tipo
                val icon = when (rifItem.type) {
                    stringResource(R.string.electric) -> ImageVector.vectorResource(id = R.drawable.electric)
                    else -> ImageVector.vectorResource(id = R.drawable.oil)
                }

                //Prezzo complessivo
                if (!rifItem.price.isNaN()) {
                    val price = formatPrice(rifItem.price)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.euro_symbol),
                            contentDescription = stringResource(R.string.price),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text(
                            text = stringResource(R.string.value_euro, price),
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }
                }

                //Unità complessive
                if (!rifItem.totunit.isNaN()) {
                    val units = formatPrice(rifItem.totunit)

                    val unitsText = when (rifItem.type) {
                        stringResource(R.string.electric) -> "$units${stringResource(R.string.kWh)}"
                        else -> "$units${stringResource(R.string.l)}"
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(R.string.total_e),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text(
                            text = unitsText,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }
                }

                //Prezzo per unità
                if (!rifItem.uvalue.isNaN()) {
                    val uPrice = formatPrice(rifItem.uvalue)

                    val unitValue = when (rifItem.type) {
                        stringResource(R.string.electric) -> "$uPrice${stringResource(R.string.eur_kwh)}"
                        else -> "$uPrice${stringResource(R.string.eur_l)}"
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.check),
                            contentDescription = stringResource(R.string.unit_cost),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text(
                            text = unitValue,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }
                }

                //Luogo
                if (!rifItem.place.isEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.gas_station),
                            contentDescription = stringResource(R.string.place),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text(
                            text = rifItem.place,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }
                }

                //Kilometri
                if (rifItem.kmt != 0) {
                    val kmt = formatKmt(rifItem.kmt)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.time_to_leave),
                            contentDescription = stringResource(R.string.kilometers),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text(
                            text = stringResource(R.string.value_km, kmt),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 18.sp
                            ),
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }
                }

                //Note
                if (!rifItem.note.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.assignment),
                            contentDescription = stringResource(R.string.notes),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text(
                            text = rifItem.note,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(start = 16.dp)
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
