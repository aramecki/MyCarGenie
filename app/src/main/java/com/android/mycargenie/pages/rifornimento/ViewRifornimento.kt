package com.android.mycargenie.pages.rifornimento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R
import java.text.DecimalFormat

@Composable
fun ViewRifScreen(
    state: RifState,
    navController: NavController,
    viewModel: RifViewModel
) {
    val rifIndex = navController.currentBackStackEntry?.arguments?.getInt("index")

    val rifItem = rifIndex?.takeIf { it in state.rif.indices }?.let { state.rif[it] }

    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(

        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                ) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro",
                            modifier = Modifier.size(35.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }


                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row {

                        IconButton(onClick = {
                            navController.navigate("EditRifScreen/$rifIndex")
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Modifica",
                                modifier = Modifier
                                    .size(30.dp),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                        IconButton(onClick = {
                                showDeleteDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Elimina",
                                modifier = Modifier
                                    .size(30.dp),
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                    }

                }
            }
        }

    ) { paddingValues ->

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteDialog = false
                },
                title = {
                    Text(text = "Confermi?")
                },
                confirmButton = {
                    TextButton(onClick = {
                        rifItem?.let { item ->
                            viewModel.onEvent(RifEvent.DeleteRif(item))
                        }
                        showDeleteDialog = false
                        navController.popBackStack()
                    }) {
                        Text("Elimina")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                    }) {
                        Text("Annulla")
                    }
                }
            )
        }


        if (rifItem != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                ) {

                    //Icona tipo

                    val icon = when (rifItem.type) {
                        "Meccanico" -> ImageVector.vectorResource(id = R.drawable.manufacturing)
                        "Elettrauto" -> ImageVector.vectorResource(id = R.drawable.lightbulb)
                        "Carrozziere" -> ImageVector.vectorResource(id = R.drawable.brush)
                        else -> ImageVector.vectorResource(id = R.drawable.repair)
                    }


                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
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

                            //Titolo

                            val fontSize = when {
                                rifItem.title.length < 20 -> 30.sp
                                rifItem.title.length < 30 -> 24.sp
                                rifItem.title.length < 40 -> 17.sp
                                else -> 14.sp
                            }

                            Column {
                                Text(
                                    text = rifItem.title,
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
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.location),
                                contentDescription = "Luogo",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(30.dp)
                            )
                            Text(
                                text = rifItem.place,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                    }
                }


                Spacer(modifier = Modifier.height(32.dp))


                Text(
                    text = "Descrizione: ${rifItem.description}",
                    style = MaterialTheme.typography.bodyMedium
                )


                Spacer(modifier = Modifier.height(32.dp))


                Row {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                    ) {
                        //Kilometri

                        val formatter = DecimalFormat("#,###")
                        val formattedKmt = formatter.format(rifItem.kmt)

                        Text(
                            text = "$formattedKmt km",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp
                            )
                        )
                    }

                    //Prezzo

                    val decimalFormat = DecimalFormat("#,##0.00")
                    val price = decimalFormat.format(rifItem.price).replace('.', ',')

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "$price €",
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
                    text = "Errore: Elemento non trovato.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            // Valutare se tornare automaticamente indietro:
            // navController.popBackStack()
        }
    }
}
