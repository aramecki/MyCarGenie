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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
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
    onManEvent: (ManEvent) -> Unit,
    navController: NavController,
    viewModel: ManViewModel
) {

    val lazyListState = rememberLazyListState()
    var isLoading by remember { mutableStateOf(false) }

    val titleOpacity by remember {
        derivedStateOf {
            val offset = lazyListState.firstVisibleItemScrollOffset
            (1f - (offset / 30f).coerceIn(0f, 1f)) // Riduce l'opacità più rapidamente
        }
    }

    val isAtEndOfList = remember {
        derivedStateOf {
            val lastVisibleIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val isSmallList = state.men.size <= 3
            val distanceFromEnd = state.men.size - lastVisibleIndex

            //println("lastVisibleIndex: $lastVisibleIndex, State Size: ${state.men.size}, Distance: $distanceFromEnd")

            !isSmallList && distanceFromEnd <= 3
        }
    }

    LaunchedEffect(isAtEndOfList.value, state.men) {
        if (isAtEndOfList.value && !isLoading) {
            //println("Caricamento nuovi dati...")
            isLoading = true
            viewModel.loadMoreMen()
            isLoading = false
        }
    }

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SmallFloatingActionButton(onClick = {
                   onManEvent(ManEvent.SortMan)
                },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.sort),
                        contentDescription = "${stringResource(R.string.sort)} ${stringResource(R.string.maintenance)}"
                    )
                }

                FloatingActionButton(onClick = {
                    state.title.value = ""
                    state.type.value = ""
                    state.place.value = ""
                    state.date.value = ""
                    state.kmt.value = 0
                    state.description.value = ""
                    state.price.value = 0.0
                    navController.navigate("AddManScreen")
                },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "${stringResource(R.string.add)} ${stringResource(R.string.maintenance)}",
                    )
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            state = lazyListState,
            contentPadding = PaddingValues(
                start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                bottom = paddingValues.calculateBottomPadding()
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .graphicsLayer { alpha = titleOpacity }
                ) {
                    Text(
                        text = stringResource(R.string.maintenance),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .alpha(0.2f)
                        .graphicsLayer { alpha = titleOpacity }
                )
            }

            if (state.men.isEmpty()) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.add_event_message),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 32.dp, vertical = 16.dp)
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
                                text = stringResource(R.string.insert),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            } else {
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
            stringResource(R.string.mechanic) -> ImageVector.vectorResource(id = R.drawable.manufacturing)
            stringResource(R.string.electrician) -> ImageVector.vectorResource(id = R.drawable.lightbulb)
            stringResource(R.string.coachbuilder) -> ImageVector.vectorResource(id = R.drawable.brush)
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
                        contentDescription = stringResource(R.string.place),
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
                    contentDescription = stringResource(R.string.date),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .size(34.dp)
                        .padding(end = 4.dp),
                )

                Text(
                    text = stringResource(R.string.value_km, kmt),
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
                        text = stringResource(R.string.value_euro, price),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
    }
}