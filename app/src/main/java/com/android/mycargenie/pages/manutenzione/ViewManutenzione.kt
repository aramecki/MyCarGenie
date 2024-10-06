package com.android.mycargenie.pages.manutenzione

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.mycargenie.R


@Composable
fun ViewManScreen(
    state: ManState,
    navController: NavController
) {
    val manIndex = navController.currentBackStackEntry?.arguments?.getInt("index")

    val manItem = manIndex?.takeIf { it in state.men.indices }?.let { state.men[it] }

    // Visualizzazione dettagli manutenzione
    Scaffold { paddingValues ->
        if (manItem != null) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                ) {

                    Text(
                        text = manItem.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            
                            .fillMaxWidth(0.5f)
                    )

                    val icon = when (manItem.type) {
                        "Meccanico" -> ImageVector.vectorResource(id = R.drawable.manufacturing)
                        "Elettrauto" -> ImageVector.vectorResource(id = R.drawable.lightbulb)
                        "Carrozziere" -> ImageVector.vectorResource(id = R.drawable.brush)
                        else -> ImageVector.vectorResource(id = R.drawable.repair)
                    }

                    Icon(
                        imageVector = icon,
                        contentDescription = manItem.type,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .size(30.dp)
                            .padding(end = 8.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                }



                //Spacer(modifier = Modifier.height(16.dp))



                Text(
                    text = "Tipo: ${manItem.type}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Luogo: ${manItem.place}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Data: ${manItem.date}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Km: ${manItem.kmt}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Descrizione: ${manItem.description}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Prezzo: ${manItem.price.toString().replace('.', ',')} €",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Errore: scheda di manutenzione non trovata.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            // Valutare se tornare automaticamente indietro:
            // navController.popBackStack()
        }
    }
}
