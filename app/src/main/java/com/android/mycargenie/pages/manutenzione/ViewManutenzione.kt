package com.android.mycargenie.pages.manutenzione

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
import androidx.compose.material.icons.rounded.DateRange
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
fun ViewManScreen(
    state: ManState,
    manViewModel: ManViewModel,
    navController: NavController,
) {
    val manIndex = navController.currentBackStackEntry?.arguments?.getInt("index")

    var showDeleteDialog by remember { mutableStateOf(false) }

    val manItem = manIndex?.takeIf { it in state.men.indices }?.let { state.men[it] }

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
                        contentDescription = "${stringResource(R.string.delete)} ${stringResource(R.string.maintenance)}"
                    )
                }

                FloatingActionButton(onClick = {
                    navController.navigate("EditManScreen/$manIndex")
                },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "${stringResource(R.string.edit)} ${stringResource(R.string.maintenance)}"
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
                            manItem?.let { item ->
                                manViewModel.onEvent(ManEvent.DeleteMan(item))
                                showDeleteDialog = false
                                navController.navigate("ManutenzioneScreen")
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

        if (manItem != null) {
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
                        contentDescription = stringResource(R.string.back_to_maintenance),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable {
                                navController.navigate("ManutenzioneScreen")
                            }
                    )

                    val titleLength = manItem.title.length

                    Text(
                        text = manItem.title,
                        fontSize = if (titleLength > 28) 20.sp else 24.sp,
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
                    val typeIcon = when (manItem.type) {
                        stringResource(R.string.mechanic) -> ImageVector.vectorResource(id = R.drawable.manufacturing)
                        stringResource(R.string.electrician) -> ImageVector.vectorResource(id = R.drawable.lightbulb)
                        stringResource(R.string.coachbuilder) -> ImageVector.vectorResource(id = R.drawable.brush)
                        else -> ImageVector.vectorResource(id = R.drawable.repair)
                    }

                    // Data
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = stringResource(R.string.date),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text(
                            text = manItem.date,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }

                //Luogo
                if (!manItem.place.isEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.location),
                            contentDescription = stringResource(R.string.place),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text(
                            text = manItem.place,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }
                }

                //Tipo
                if (!manItem.type.isEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = typeIcon,
                            contentDescription = stringResource(R.string.type),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text(
                            text = manItem.type,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }
                }

                //Kilometri
                if (manItem.kmt != 0) {
                    val kmt = formatKmt(manItem.kmt)
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

                //Prezzo
                if (!manItem.price.isNaN()) {
                    val price = formatPrice(manItem.price)
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
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 18.sp
                            ),
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }
                }

                //Descrizione
                if (!manItem.description.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 16.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.assignment),
                            contentDescription = stringResource(R.string.description),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(30.dp)
                        )
                        Text(
                            text = manItem.description,
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
