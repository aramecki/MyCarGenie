package com.android.mycargenie.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.mycargenie.R
import com.android.mycargenie.pages.libretto.LibrettoScreen
import com.android.mycargenie.pages.manutenzione.AddManScreen
import com.android.mycargenie.pages.manutenzione.EditManScreen
import com.android.mycargenie.pages.manutenzione.ManEvent
import com.android.mycargenie.pages.manutenzione.ManState
import com.android.mycargenie.pages.manutenzione.ManViewModel
import com.android.mycargenie.pages.manutenzione.ManutenzioneScreen
import com.android.mycargenie.pages.manutenzione.ViewManScreen
import com.android.mycargenie.pages.rifornimento.AddRifScreen
import com.android.mycargenie.pages.rifornimento.EditRifScreen
import com.android.mycargenie.pages.rifornimento.RifEvent
import com.android.mycargenie.pages.rifornimento.RifState
import com.android.mycargenie.pages.rifornimento.RifViewModel
import com.android.mycargenie.pages.rifornimento.RifornimentoScreen
import com.android.mycargenie.pages.rifornimento.ViewRifScreen


// Screens

@Composable
fun ProfessionistiScreen() {
    Text(
        text = "Professionisti",
        modifier = Modifier.fillMaxSize()
    )
}


// Defining the items
data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val screen: @Composable () -> Unit
)

@Composable
fun MainApp(
    viewModel: ManViewModel,
    rifViewModel: RifViewModel,
    onManEvent: (ManEvent) -> Unit,
    onRifEvent: (RifEvent) -> Unit,
    state: ManState,
    rifState: RifState
) {


    val navController = rememberNavController()

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentBackStackEntry?.destination?.route

    val shouldShowBottomBar = currentDestination !in listOf("ViewManScreen/{index}", "AddManScreen", "EditManScreen/{manIndex}", "ViewRifScreen/{index}", "AddRifScreen", "EditRifScreen/{rifIndex}")

    val shouldShowTopBar = currentDestination !in listOf("LibrettoScreen")

    val statusBarHeight = with(LocalDensity.current) {
        WindowInsets.statusBars.getBottom(this).toDp() // Ottieni l'altezza della status bar
    }

    val topBarHeight = statusBarHeight + 90.dp

    Scaffold(
        modifier = Modifier.fillMaxSize(),


        topBar = {
            if (shouldShowTopBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(topBarHeight)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (currentDestination) {
                        "ManutenzioneScreen", "RifornimentoScreen" -> {
                            Text(
                                text = when (currentDestination) {
                                    "ManutenzioneScreen" -> "Manutenzione"
                                    "RifornimentoScreen" -> "Rifornimento"
                                    else -> ""
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .offset(y = 12.dp),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        "AddManScreen", "AddRifScreen", "ViewManScreen/{index}", "ViewRifScreen/{index}", "EditManScreen/{manIndex}", "EditRifScreen/{rifIndex}" -> {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .offset(y = 12.dp)
                                .clickable {
                                        navController.popBackStack()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Indietro",
                                    modifier = Modifier
                                        .size(35.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        else -> {
                            // Altre eventuali azioni o un blocco vuoto se non è richiesto nulla
                        }
                    }

                    if (currentDestination == "ManutenzioneScreen" || currentDestination == "RifornimentoScreen") {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .offset(y = 12.dp)
                                .clickable {
                                    when (currentDestination) {
                                        "ManutenzioneScreen" -> onManEvent(ManEvent.SortMan)
                                        "RifornimentoScreen" -> onRifEvent(RifEvent.SortRif)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.sort),
                                contentDescription = "Ordina",
                                modifier = Modifier
                                    .size(35.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }



                    if (currentDestination == "ViewManScreen/{index}" || currentDestination == "ViewRifScreen/{index}") {
                        val rifIndex =
                            navController.currentBackStackEntry?.arguments?.getInt("index")
                        val manIndex =
                            navController.currentBackStackEntry?.arguments?.getInt("index")

                        var showDeleteDialog by remember { mutableStateOf(false) }

                        if (showDeleteDialog) {

                            val rifItem = rifIndex?.takeIf { it in rifState.rifs.indices }
                                ?.let { rifState.rifs[it] }
                            val manItem =
                                manIndex?.takeIf { it in state.men.indices }?.let { state.men[it] }

                            AlertDialog(
                                onDismissRequest = {
                                    showDeleteDialog = false
                                },
                                title = {
                                    Text(text = "Confermi?")
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        when (currentDestination) {
                                            "ViewManScreen/{index}" -> manItem?.let { item ->
                                                viewModel.onEvent(ManEvent.DeleteMan(item))
                                            }

                                            "ViewRifScreen/{index}" -> rifItem?.let { item ->
                                                rifViewModel.onEvent(RifEvent.DeleteRif(item))
                                            }
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

                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .offset(y = 12.dp)
                                        .clickable {
                                            when (currentDestination) {
                                                "ViewManScreen/{index}" -> navController.navigate("EditManScreen/$manIndex")
                                                "ViewRifScreen/{index}" -> navController.navigate("EditRifScreen/$rifIndex")

                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "Modifica",
                                        modifier = Modifier
                                            .size(30.dp),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .offset(y = 12.dp)
                                        .clickable {
                                                showDeleteDialog = true
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Elimina",
                                        modifier = Modifier
                                            .size(30.dp),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }

                        }
                    }
                }
            }
        },

        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    items = listOf(
                        BottomNavItem(
                            "Libretto",
                            ImageVector.vectorResource(id = R.drawable.assignment)
                        ) {
                            selectedTabIndex = 0
                            navController.navigate(("LibrettoScreen")

                            )
                          },
                        BottomNavItem(
                            "Manutenzione",
                            ImageVector.vectorResource(id = R.drawable.time_to_leave)
                        ) {
                            selectedTabIndex = 1
                            navController.navigate("ManutenzioneScreen")
                        },
                        BottomNavItem(
                            "Rifornimento",
                            ImageVector.vectorResource(id = R.drawable.gas_station)
                        ) {
                            selectedTabIndex = 2
                            navController.navigate("RifornimentoScreen")
                          },
                        BottomNavItem(
                            "Professionisti",
                            ImageVector.vectorResource(id = R.drawable.store)
                        ) { ProfessionistiScreen() },
                        BottomNavItem(
                            "Carburanti",
                            ImageVector.vectorResource(id = R.drawable.location)
                        ) { TODO() }
                    ),
                    selectedIndex = selectedTabIndex,
                    onTabSelected = { index ->
                        selectedTabIndex = index

                        when (index) {
                            0 -> navController.navigate("LibrettoScreen")
                            1 -> navController.navigate("ManutenzioneScreen")
                            2 -> navController.navigate("RifornimentoScreen")
                            3 -> navController.navigate("ProfessionistiScreen")
                            4 -> navController.navigate("CarburantiScreen")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        ) {
            NavHost(navController = navController, startDestination = "LibrettoScreen") {

                composable("LibrettoScreen") {
                    LibrettoScreen(
                        state = viewModel.state.collectAsState().value,
                        rifState = rifViewModel.state.collectAsState().value,
                        navController = navController
                    )
                }
                composable("ManutenzioneScreen") {
                    ManutenzioneScreen(
                        state = viewModel.state.collectAsState().value,
                        navController = navController,
                        //onEvent = viewModel::onEvent
                    )
                }
                composable("AddManScreen",
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                    },
                    exitTransition = {
                        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                    }
                ) {
                    AddManScreen(
                        state = viewModel.state.collectAsState().value,
                        navController = navController,
                        onEvent = viewModel::onEvent
                    )
                }

                composable("ViewManScreen/{index}",
                    arguments = listOf(navArgument("index") { type = NavType.IntType }),
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                    },
                    exitTransition = {
                        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                    }
                ) {
                    ViewManScreen(
                        state = viewModel.state.collectAsState().value,
                        navController = navController,
                    )

                    }

                composable("EditManScreen/{manIndex}",
                    arguments = listOf(navArgument("manIndex") { type = NavType.IntType }),
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                    },
                    exitTransition = {
                        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                    }
                ) {
                    EditManScreen(
                        state = viewModel.state.collectAsState().value,
                        navController = navController,
                        onEvent = viewModel::onEvent
                    )
                }

                composable("RifornimentoScreen") {
                    RifornimentoScreen(
                        state = rifViewModel.state.collectAsState().value,
                        navController = navController,
                        //onEvent = rifViewModel::onEvent
                    )
                }
                composable("AddRifScreen",
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                    },
                    exitTransition = {
                        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                    }
                ) {
                    AddRifScreen(
                        state = rifViewModel.state.collectAsState().value,
                        navController = navController,
                        onEvent = rifViewModel::onEvent
                    )
                }
                composable("ViewRifScreen/{index}",
                    arguments = listOf(navArgument("index") { type = NavType.IntType }),
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                    },
                    exitTransition = {
                        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                    }
                ) {
                    ViewRifScreen(
                        state = rifViewModel.state.collectAsState().value,
                        navController = navController,
                    )

                }

                composable("EditRifScreen/{rifIndex}",
                    arguments = listOf(navArgument("rifIndex") { type = NavType.IntType }),
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn(animationSpec = tween(500))
                    },
                    exitTransition = {
                        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut(animationSpec = tween(500))
                    }
                ) {
                    EditRifScreen(
                        state = rifViewModel.state.collectAsState().value,
                        navController = navController,
                        onEvent = rifViewModel::onEvent
                    )
                }



            }
            }
        }
    }


@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = {
                    Text(
                    item.title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 9.sp
                    )
                    ) },
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}



