package com.android.mycargenie.pages.scadenze

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.mycargenie.R

@Composable
fun ExpScreen(
    navController: NavController
) {

    Row {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = {
                navController.navigate("ExpirationsSettings")
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                    contentDescription = "Impostazioni",
                    modifier = Modifier
                        .size(42.dp)
                        .padding(top = 8.dp, end = 8.dp),
                )
            }
        }
    }

    Column {

        Text(
            text = "Assicurazione RCA"
        )

        Text(
            text = "Bollo Auto"
        )

        Text(
            text = "Revisione"
        )

        Text(
            text = "Patente di Guida"
        )

    }

}