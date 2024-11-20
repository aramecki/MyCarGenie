package com.android.mycargenie.pages.manutenzione

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
import androidx.compose.material.icons.rounded.DateRange
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
                    val icon = when (manItem.type) {
                        stringResource(R.string.mechanic) -> ImageVector.vectorResource(id = R.drawable.manufacturing)
                        stringResource(R.string.electrician) -> ImageVector.vectorResource(id = R.drawable.lightbulb)
                        stringResource(R.string.coachbuilder) -> ImageVector.vectorResource(id = R.drawable.brush)
                        else -> ImageVector.vectorResource(id = R.drawable.repair)
                    }


                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = manItem.type,
                                    modifier = Modifier
                                        .size(45.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            //Titolo

                            val fontSize = when {
                                manItem.title.length < 20 -> 30.sp
                                manItem.title.length < 30 -> 24.sp
                                manItem.title.length < 40 -> 17.sp
                                else -> 14.sp
                            }

                            Column {
                                Text(
                                    text = manItem.title,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontSize = fontSize
                                    ),
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 8.dp)
                                )
                            }

                        }
                    }


                Spacer(modifier = Modifier.height(30.dp))


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
                                contentDescription = stringResource(R.string.date),
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                            Text(
                                text = manItem.date,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .padding(start = 4.dp)
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
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
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                    }
                }


                Spacer(modifier = Modifier.height(32.dp))


                Text(
                    text = "${stringResource(R.string.description)}: ${manItem.description}",
                    style = MaterialTheme.typography.bodyMedium
                )


                Spacer(modifier = Modifier.height(32.dp))


                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                    ) {
                        //Kilometri

                        val kmt = formatKmt(manItem.kmt)

                        Text(
                            text = stringResource(R.string.value_km, kmt),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp
                            )
                        )
                    }

                    //Prezzo

                    val price = formatPrice(manItem.price)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.value_euro, price),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp
                            ),
                            modifier = Modifier
                                .align(Alignment.End)
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
