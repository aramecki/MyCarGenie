package com.android.mycargenie.pages.manutenzione

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R
import com.android.mycargenie.shared.formatKmt
import com.android.mycargenie.shared.formatPrice

@Composable
fun ManutenzioneScreen(
    state: ManState,
    navController: NavController,
    viewModel: ManViewModel
) {

    // The optimization of the element loading has been obtained with the use of AI

    val lazyListState = rememberLazyListState()

    val isAtEndOfList = remember {
        derivedStateOf {
            val lastVisibleIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val distanceFromEnd = state.men.size - lastVisibleIndex
            println("lastVisibleIndex: $lastVisibleIndex, State Size: ${state.men.size}, Distance: $distanceFromEnd")
            distanceFromEnd <= 3
        }
    }

    LaunchedEffect(isAtEndOfList.value, state.men) {
        println("isAtEndOfList.value: ${isAtEndOfList.value}")
        if (isAtEndOfList.value) {
            println("Caricamento nuovi dati...")
            viewModel.loadMoreMen()
        }
    }

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(onClick = {
                state.title.value = ""
                state.type.value = ""
                state.place.value = ""
                state.date.value = ""
                state.kmt.value = 0
                state.description.value = ""
                state.price.value = 0.0
                navController.navigate("AddManScreen")
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Aggiungi Manutenzione")
            }
        }
    ) { paddingValues ->

        if (state.men.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aggiungi la tua prima manutenzione per visualizzare una card.",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(start = 32.dp, top = 82.dp, bottom = 15.dp, end = 32.dp)
                )

                Button(onClick = {
                    state.title.value = ""
                    state.type.value = ""
                    state.place.value = ""
                    state.date.value = ""
                    state.kmt.value = 0
                    state.description.value = ""
                    state.price.value = 0.0
                    navController.navigate("AddManScreen")
                }) {
                    Text(
                        text = "Inserisci",
                        fontSize = 16.sp
                    )
                }

            }
        } else {

            LazyColumn(
                state = lazyListState,
                contentPadding = PaddingValues(
                    top = 8.dp,
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding()
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(state.men.size) { index ->
                    ManItem(
                        state = state,
                        index = index,
                        navController = navController
                    )
                }

                if (state.isLoading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }

            }

        }

    }

}

@Composable
fun ManItem(
    state: ManState,
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
                navController.navigate("ViewManScreen/$index")
            }
    ) {
        val icon = when (state.men[index].type) {
            "Meccanico" -> ImageVector.vectorResource(id = R.drawable.manufacturing)
            "Elettrauto" -> ImageVector.vectorResource(id = R.drawable.lightbulb)
            "Carrozziere" -> ImageVector.vectorResource(id = R.drawable.brush)
            else -> ImageVector.vectorResource(id = R.drawable.repair)
        }

        Column(
            verticalArrangement = Arrangement.SpaceEvenly
        ) {


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = state.men[index].type,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(end = 4.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                Column {

                    //Titolo
                    Text(
                        text = state.men[index].title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                //Data
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = state.men[index].date,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }


            //Luogo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 4.dp)
            ) {
                val place = state.men[index].place

                if (place.isNotEmpty()) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.location),
                        contentDescription = "Luogo",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .size(34.dp)
                            .padding(end = 4.dp),
                    )
                    Text(
                        text = state.men[index].place,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    Spacer(modifier = Modifier
                        .height(34.dp)
                    )
                }
            }


            //Kilometri

            val kmt = formatKmt(state.men[index].kmt)

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.time_to_leave),
                    contentDescription = "Data",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(end = 4.dp),
                )

                Text(
                    text = "$kmt km",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )


                //Prezzo
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val price = formatPrice(state.men[index].price)

                    Text(
                        text = "$price €",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
    }
}