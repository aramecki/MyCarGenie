package com.android.mycargenie.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.mycargenie.R
import com.android.mycargenie.pages.home.HomeScreen
import com.android.mycargenie.pages.home.HomeViewModel
import com.android.mycargenie.pages.libretto.CarProfile
import com.android.mycargenie.pages.libretto.LibrettoScreen
import com.android.mycargenie.pages.libretto.LibrettoSettingsScreen
import com.android.mycargenie.pages.libretto.LibrettoViewModel
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
import com.android.mycargenie.pages.scadenze.ExpScreen
import com.android.mycargenie.pages.scadenze.ExpSettingsScreen
import com.android.mycargenie.pages.scadenze.Expirations
import com.android.mycargenie.pages.scadenze.ExpirationsViewModel
import com.android.mycargenie.pages.scadenze.PermissionHandler

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val screen: @Composable () -> Unit
)


@Composable
fun MainApp(
    homeViewModel: HomeViewModel,
    manViewModel: ManViewModel,
    rifViewModel: RifViewModel,
    librettoViewModel: LibrettoViewModel,
    expirationsViewModel: ExpirationsViewModel,
    onManEvent: (ManEvent) -> Unit,
    onRifEvent: (RifEvent) -> Unit,
    state: ManState,
    rifState: RifState,
    carProfile: CarProfile,
    expirations: Expirations,
    permissionHandler: PermissionHandler
) {


    val navController = rememberNavController()

    val observedCarProfile by librettoViewModel.carProfile.collectAsState()
    val observedExpirations by expirationsViewModel.expSettings.collectAsState()

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = currentBackStackEntry?.destination?.route

    val shouldShowBottomBar = currentDestination !in listOf("ViewManScreen/{index}", "AddManScreen", "EditManScreen/{manIndex}", "ViewRifScreen/{index}", "AddRifScreen", "EditRifScreen/{rifIndex}", "ProfileSettings", "ExpirationsSettings")

    val shouldShowTopBar = currentDestination !in listOf("HomeScreen", "ProfileScreen")

    val statusBarHeight = with(LocalDensity.current) {
        WindowInsets.statusBars.getBottom(this).toDp()
    }

    val topBarHeight = statusBarHeight + 90.dp

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),


        topBar = {
            if (shouldShowTopBar) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(topBarHeight)
                        .background(NavigationBarDefaults.containerColor)
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (currentDestination) {
                        "ManutenzioneScreen", "RifornimentoScreen", "ExpirationsScreen" -> {
                            Text(
                                text = when (currentDestination) {
                                    "ManutenzioneScreen" -> "Manutenzione"
                                    "RifornimentoScreen" -> "Rifornimento"
                                    "ExpirationsScreen" -> "Scadenze"
                                    else -> ""
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .offset(y = 12.dp),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                //color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        "AddManScreen", "AddRifScreen", "ViewManScreen/{index}", "ViewRifScreen/{index}", "EditManScreen/{manIndex}", "EditRifScreen/{rifIndex}", "ProfileSettings", "ExpirationsSettings" -> {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .offset(y = 12.dp)
                                    .clickable { navController.popBackStack() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Indietro",
                                    modifier = Modifier
                                        .size(35.dp),
                                    //tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
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
                                //tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    if (currentDestination == "ExpirationsScreen") {
                        Spacer(modifier = Modifier.height(48.dp))
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
                                                manViewModel.onEvent(ManEvent.DeleteMan(item))
                                                showDeleteDialog = false
                                                navController.navigate("ManutenzioneScreen")
                                            }


                                            "ViewRifScreen/{index}" -> rifItem?.let { item ->
                                                rifViewModel.onEvent(RifEvent.DeleteRif(item))
                                                showDeleteDialog = false
                                                navController.navigate("RifornimentoScreen")
                                            }
                                        }

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
                                        //tint = MaterialTheme.colorScheme.onPrimary
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
                                        //tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }

                        }
                    }
                }
            }
        },

        bottomBar = {

            LaunchedEffect(navController) {
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    selectedTabIndex = when (destination.route) {
                        "HomeScreen" -> 0
                        "ManutenzioneScreen" -> 1
                        "RifornimentoScreen" -> 2
                        "ExpirationsScreen" -> 3
                        "ProfileScreen" -> 4
                        else -> 0
                    }
                }
            }

            if (shouldShowBottomBar) {
                    BottomNavigationBar(
                        items = listOf(
                            BottomNavItem(
                                "Home",
                                ImageVector.vectorResource(id = R.drawable.home)
                            ) {
                                selectedTabIndex = 0
                                navController.navigate("HomeScreen")
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
                                "Scadenze",
                                ImageVector.vectorResource(id = R.drawable.calendar)
                            ) {
                                selectedTabIndex = 3
                                navController.navigate("ExpirationsScreen")
                            },
                            BottomNavItem(
                                "Libretto",
                                ImageVector.vectorResource(id = R.drawable.assignment)
                            ) {
                                selectedTabIndex = 4
                                navController.navigate("ProfileScreen")
                            }
                        ),
                        selectedIndex = selectedTabIndex,
                        onTabSelected = { index ->
                            selectedTabIndex = index

                            when (index) {
                                0 -> navController.navigate("HomeScreen")
                                1 -> navController.navigate("ManutenzioneScreen")
                                2 -> navController.navigate("RifornimentoScreen")
                                3 -> navController.navigate("ExpirationsScreen")
                                4 -> navController.navigate("ProfileScreen")
                            }
                        }
                    )
            }
        }
    ) { innerPadding ->


        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            NavHost(navController = navController, startDestination = "HomeScreen") {

                composable("HomeScreen",
                    enterTransition = { slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) }
                ) {
                    HomeScreen(
                        manState = manViewModel.state.collectAsState().value,
                        rifState = rifViewModel.state.collectAsState().value,
                        homeViewModel = homeViewModel,
                        carProfile = observedCarProfile,
                        navController = navController
                    )
                }

                composable("ManutenzioneScreen",
                    enterTransition = {
                        val previousScreen = navController.previousBackStackEntry?.destination?.route
                        when (previousScreen) {
                            "HomeScreen" -> slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "RifornimentoScreen", "ExpirationsScreen", "ProfileScreen", "AddManScreen", "ViewManScreen/{index}" -> slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    },
                    exitTransition = {
                        val nextScreen = navController.currentBackStackEntry?.destination?.route
                        when (nextScreen) {
                            "HomeScreen" -> slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "RifornimentoScreen", "ExpirationsScreen", "ProfileScreen", "AddManScreen", "ViewManScreen/{index}" -> slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    },
                    popEnterTransition = {
                        val previousScreen = navController.previousBackStackEntry?.destination?.route
                        when (previousScreen) {
                            "HomeScreen" -> slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "RifornimentoScreen", "ExpirationsScreen", "ProfileScreen", "AddManScreen", "ViewManScreen/{index}" -> slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    },
                    popExitTransition = {
                        val nextScreen = navController.currentBackStackEntry?.destination?.route
                        when (nextScreen) {
                            "HomeScreen" -> slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "RifornimentoScreen", "ExpirationsScreen", "ProfileScreen", "AddManScreen", "ViewManScreen/{index}" -> slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    }
                ) {
                    ManutenzioneScreen(
                        state = manViewModel.state.collectAsState().value,
                        navController = navController,
                        viewModel = manViewModel
                    )
                }

                composable("AddManScreen",
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
                ) {
                    AddManScreen(
                        state = manViewModel.state.collectAsState().value,
                        navController = navController,
                        onEvent = manViewModel::onEvent
                    )
                }

                composable("ViewManScreen/{index}",
                    arguments = listOf(navArgument("index") { type = NavType.IntType }),
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
                ) {
                    ViewManScreen(
                        state = manViewModel.state.collectAsState().value,
                        navController = navController,
                    )

                    }

                composable("EditManScreen/{manIndex}",
                    arguments = listOf(navArgument("manIndex") { type = NavType.IntType }),
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
                ) {
                    EditManScreen(
                        state = manViewModel.state.collectAsState().value,
                        navController = navController,
                        onEvent = manViewModel::onEvent
                    )
                }

                composable("RifornimentoScreen",
                    enterTransition = {
                        val previousScreen = navController.previousBackStackEntry?.destination?.route
                        when (previousScreen) {
                            "HomeScreen", "ManutenzioneScreen" -> slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "ExpirationsScreen", "ProfileScreen", "AddRifScreen", "ViewRifScreen/{index}" -> slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    },
                    exitTransition = {
                        val nextScreen = navController.currentBackStackEntry?.destination?.route
                        when (nextScreen) {
                            "HomeScreen", "ManutenzioneScreen" -> slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "ExpirationsScreen", "ProfileScreen", "AddRifScreen", "ViewRifScreen/{index}" -> slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    },
                    popEnterTransition = {
                        val previousScreen = navController.previousBackStackEntry?.destination?.route
                        when (previousScreen) {
                            "HomeScreen", "ManutenzioneScreen" -> slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "ExpirationsScreen", "ProfileScreen", "AddRifScreen", "ViewRifScreen/{index}" -> slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    },
                    popExitTransition = {
                        val nextScreen = navController.currentBackStackEntry?.destination?.route
                        when (nextScreen) {
                            "HomeScreen", "ManutenzioneScreen" -> slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "ExpirationsScreen", "ProfileScreen", "AddRifScreen", "ViewRifScreen/{index}" -> slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    }
                    ) {
                    RifornimentoScreen(
                        state = rifViewModel.state.collectAsState().value,
                        navController = navController,
                        viewModel = rifViewModel
                    )
                }
                composable("AddRifScreen",
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
                ) {
                    AddRifScreen(
                        state = rifViewModel.state.collectAsState().value,
                        navController = navController,
                        onEvent = rifViewModel::onEvent
                    )
                }
                composable("ViewRifScreen/{index}",
                    arguments = listOf(navArgument("index") { type = NavType.IntType }),
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
                ) {
                    ViewRifScreen(
                        state = rifViewModel.state.collectAsState().value,
                        navController = navController,
                    )

                }

                composable("EditRifScreen/{rifIndex}",
                    arguments = listOf(navArgument("rifIndex") { type = NavType.IntType }),
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
                ) {
                    EditRifScreen(
                        state = rifViewModel.state.collectAsState().value,
                        navController = navController,
                        onEvent = rifViewModel::onEvent
                    )
                }

                composable("ExpirationsScreen",
                    enterTransition = {
                        val previousScreen = navController.previousBackStackEntry?.destination?.route
                        when (previousScreen) {
                            "HomeScreen", "ManutenzioneScreen", "RifornimentoScreen" -> slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "ProfileScreen", "ExpirationsSettings" -> slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    },
                    exitTransition = {
                        val nextScreen = navController.currentBackStackEntry?.destination?.route
                        when (nextScreen) {
                            "HomeScreen", "ManutenzioneScreen", "RifornimentoScreen" -> slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "ProfileScreen", "ExpirationsSettings" -> slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    },
                    popEnterTransition = {
                        val previousScreen = navController.previousBackStackEntry?.destination?.route
                        when (previousScreen) {
                            "HomeScreen", "ManutenzioneScreen", "RifornimentoScreen" -> slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "ProfileScreen", "ExpirationsSettings" -> slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    },
                    popExitTransition = {
                        val nextScreen = navController.currentBackStackEntry?.destination?.route
                        when (nextScreen) {
                            "HomeScreen", "ManutenzioneScreen", "RifornimentoScreen" -> slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            "ProfileScreen", "ExpirationsSettings" -> slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    }
                ) {
                    ExpScreen(
                        expirations = observedExpirations,
                        navController = navController
                    )
                }

                composable("ExpirationsSettings",
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
                ) {
                    ExpSettingsScreen(
                        expirations = expirations,
                        expirationsViewModel = expirationsViewModel,
                        navController = navController,
                        permissionHandler = permissionHandler
                    )
                }


                composable("ProfileScreen",
                    enterTransition = {
                        val previousScreen = navController.previousBackStackEntry?.destination?.route
                        when (previousScreen) {
                            "ProfileSettings" -> slideInHorizontally(initialOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                                      },
                    exitTransition = {
                        val nextScreen = navController.currentBackStackEntry?.destination?.route
                        when (nextScreen) {
                            "ProfileSettings" -> slideOutHorizontally(targetOffsetX = { -1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                            else -> slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow))
                        }
                    },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) }
                ) {
                    LibrettoScreen(
                        carProfile = observedCarProfile,
                        navController = navController
                    )
                }

                composable("ProfileSettings",
                    enterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessLow)) },
                    popEnterTransition = { slideInHorizontally(initialOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) },
                    popExitTransition = { slideOutHorizontally(targetOffsetX = { 1200 }, animationSpec = spring(stiffness = Spring.StiffnessMedium)) }
                ) {
                    LibrettoSettingsScreen(
                        carProfile = carProfile,
                        librettoViewModel = librettoViewModel,
                        navController = navController,
                        context = LocalContext.current
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

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val fontSize = when {
        screenWidth <= 360 -> 8.sp
        screenWidth <= 460 -> 9.sp
        else -> 10.sp
    }

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = {
                    Text(
                    item.title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = fontSize
                    ),
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) },
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}



