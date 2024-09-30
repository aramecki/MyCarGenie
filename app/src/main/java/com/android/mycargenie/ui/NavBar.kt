package com.android.mycargenie.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.android.mycargenie.R

//Screens
object LibrettoScreen : Screen {
    private fun readResolve(): Any = LibrettoScreen

    @Composable
    override fun Content() {
        Text(
            text = "Libretto",
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

object ManutenzioneScreen : Screen {
    private fun readResolve(): Any = ManutenzioneScreen

    @Composable
    override fun Content() {
        Text(
            text = "Manutenzione",
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

object CarburanteScreen : Screen {
    private fun readResolve(): Any = CarburanteScreen

    @Composable
    override fun Content() {
        Text(
            text = "Carburante",
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

object ProfessionistiScreen : Screen {
    private fun readResolve(): Any = ProfessionistiScreen

    @Composable
    override fun Content() {
        Text(
            text = "Professionisti",
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

object RifornimentoScreen : Screen {
    private fun readResolve(): Any = RifornimentoScreen

    @Composable
    override fun Content() {
        Text(
            text = "Rifornimento",
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

//Defining the items
data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val screen: Screen
)

@Composable
fun MainApp() {
    val bottomNavItems = listOf(
        BottomNavItem("Libretto", Icons.Filled.Home, LibrettoScreen),
        BottomNavItem("Manutenzione", Icons.Filled.Notifications, ManutenzioneScreen),
        BottomNavItem("Carburante", Icons.Filled.Add, CarburanteScreen),
        BottomNavItem("Professionisti", Icons.Filled.Person, ProfessionistiScreen),
        BottomNavItem("Rifornimento", Icons.Filled.AddCircle, RifornimentoScreen)
    )

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    Navigator(bottomNavItems[selectedTabIndex].screen) { navigator ->
        Scaffold(
            bottomBar = {
                BottomNavigationBar(bottomNavItems, selectedTabIndex) { newIndex ->
                    selectedTabIndex = newIndex
                    navigator.replace(bottomNavItems[newIndex].screen)
                }
            }
        ) { innerPadding ->
            AnimatedVisibility(
                visible = true, //Implementare logica dell'animazione
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.padding(innerPadding)
            ) {
                bottomNavItems[selectedTabIndex].screen.Content()
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
                label = { Text(item.title) },
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavBarPreview() {
    // This is a simple wrapper to provide a mock navigation context.
    val bottomNavItems = listOf(
        BottomNavItem("Libretto", ImageVector.vectorResource(R.drawable.baseline_assignment_24), LibrettoScreen),
        BottomNavItem("Manutenzione", ImageVector.vectorResource(R.drawable.baseline_time_to_leave_24), ManutenzioneScreen),
        BottomNavItem("Carburante", ImageVector.vectorResource(R.drawable.baseline_local_gas_station_24), CarburanteScreen),
        BottomNavItem("Professionisti", ImageVector.vectorResource(R.drawable.baseline_store_24), ProfessionistiScreen),
        BottomNavItem("Rifornimento", ImageVector.vectorResource(R.drawable.baseline_location_pin_24), RifornimentoScreen)
    )

    // Set the selected index to see the active tab.
    var selectedIndex by remember { mutableIntStateOf(0) }

    // Use a Scaffold to show the bottom navigation bar.
    Scaffold(
        bottomBar = {
            BottomNavigationBar(bottomNavItems, selectedIndex) { newIndex ->
                selectedIndex = newIndex
            }
        }
    ) { innerPadding ->
        // You can display a placeholder content here.
        Box(modifier = Modifier.padding(innerPadding)) {
            bottomNavItems[selectedIndex].screen.Content()
        }
    }
}