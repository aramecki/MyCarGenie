package com.android.mycargenie.pages.rifornimento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun RifornimentoScreen(
    state: RifState,
    navController: NavController,
    onEvent: (RifEvent) -> Unit
) {


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
                Text(
                    text = "Rifornimento", //Da inserire in strings
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )


                IconButton(onClick = { onEvent(RifEvent.SortRif) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.sort),
                        contentDescription = "Ordina Rifornimento",
                        modifier = Modifier.size(35.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                state.type.value = ""
                state.place.value = ""
                state.price.value = 0.0
                state.uvalue.value = 0.0
                state.totunit.value = 0.0
                state.date.value = ""
                state.note.value = ""
                state.kmt.value = 0
                navController.navigate("AddRifScreen")
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Aggiungi Rifornimento")
            }
        }
    ) { paddingValues ->

        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(state.rif.size) { index ->
                RifItem(
                    state = state,
                    index = index,
                    navController = navController
                )
            }

        }

    }

}

@Composable
fun RifItem(
    state: RifState,
    index: Int,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
            .clickable {
                navController.navigate("ViewRifScreen/$index")
            }
    ) {

        //Icona tipo
        val icon = when (state.rif[index].type) {
            "Elettrico" -> ImageVector.vectorResource(id = R.drawable.electric)
            else -> ImageVector.vectorResource(id = R.drawable.oil)
        }

        val decimalFormat = DecimalFormat("#,##0.00")


        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = state.rif[index].type,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(end = 4.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                Column {

                    //Data
                    Text(
                        text = state.rif[index].date,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    //Prezzo
                    val price = decimalFormat.format(state.rif[index].price).replace('.', ',')

                    Text(
                        text = "$price €",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            ) {
                //Luogo
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.location),
                            contentDescription = "Luogo",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .size(34.dp)
                                .padding(end = 4.dp),
                        )
                        Text(
                            text = state.rif[index].place,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                //Prezzo per unità
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val uvalue = decimalFormat.format(state.rif[index].uvalue).replace('.', ',')

                    val unitprice = if (state.rif[index].type == "Elettrico") {
                        "$uvalue €/kWh"
                    } else {
                        "$uvalue €/l"
                    }

                    Text(
                        text = unitprice,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            ) {

                //Unità totali
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val totunit = decimalFormat.format(state.rif[index].totunit).replace('.', ',')

                    val showunit = if (state.rif[index].type == "Elettrico") {
                        "$totunit kWh"
                    } else {
                        "$totunit l"
                    }

                    Text(
                        text = showunit,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )

                }
            }
        }
    }
}