package com.android.mycargenie.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.mycargenie.R
import com.android.mycargenie.pages.manutenzione.AddManScreen
import com.android.mycargenie.pages.manutenzione.ManViewModel
import com.android.mycargenie.pages.manutenzione.ManutenzioneScreen
import com.android.mycargenie.pages.manutenzione.ViewManScreen


// Screens (now simple Composables)
@Composable
fun LibrettoScreen() {
    Text(
        text = "Libretto",
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun CarburanteScreen() {
    Text(
        text = "Carburante",
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun ProfessionistiScreen() {
    Text(
        text = "Professionisti",
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun RifornimentoScreen() {
    Text(
        text = "Rifornimento",
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
fun MainApp(viewModel: ManViewModel) {
    // Creiamo un NavController da utilizzare per la navigazione
    val navController = rememberNavController()

    // Inizializzazione della navbar
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentBackStackEntry?.destination?.route

    // Determinare se la navbar deve essere visibile o meno
    val shouldShowBottomBar = currentDestination !in listOf("ViewManScreen/{index}", "AddManScreen")

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                BottomNavigationBar(
                    items = listOf(
                        BottomNavItem(
                            "Libretto",
                            ImageVector.vectorResource(id = R.drawable.assignment)
                        ) { LibrettoScreen() },
                        BottomNavItem(
                            "Manutenzione",
                            ImageVector.vectorResource(id = R.drawable.time_to_leave)
                        ) {
                            selectedTabIndex = 1 // Aggiorna l'indice selezionato per il NavBar
                            navController.navigate("ManutenzioneScreen")
                        },
                        BottomNavItem(
                            "Carburante",
                            ImageVector.vectorResource(id = R.drawable.gas_station)
                        ) { CarburanteScreen() },
                        BottomNavItem(
                            "Professionisti",
                            ImageVector.vectorResource(id = R.drawable.store)
                        ) { ProfessionistiScreen() },
                        BottomNavItem(
                            "Rifornimento",
                            ImageVector.vectorResource(id = R.drawable.location)
                        ) { RifornimentoScreen() }
                    ),
                    selectedIndex = selectedTabIndex,
                    onTabSelected = { index ->
                        selectedTabIndex = index
                        // Naviga in base all'indice selezionato
                        when (index) {
                            0 -> navController.navigate("HomeScreen")
                            1 -> navController.navigate("ManutenzioneScreen")
                            2 -> navController.navigate("CarburanteScreen")
                            3 -> navController.navigate("ProfessionistiScreen")
                            4 -> navController.navigate("RifornimentoScreen")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = "HomeScreen") {
                composable("HomeScreen") {
                    LibrettoScreen() // Mostra HomeScreen
                }
                composable("ManutenzioneScreen") {
                    ManutenzioneScreen(
                        state = viewModel.state.collectAsState().value,
                        navController = navController,
                        onEvent = viewModel::onEvent
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
                        viewModel = viewModel
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



