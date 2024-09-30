package com.android.mycargenie.pages.manutenzione

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
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

@Composable
fun ManutenzioneScreen(
    state: ManState,
    navController: NavController,
    onEvent: (ManEvent) -> Unit
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
                    text = "Manutenzione", //Da inserire in strings stringResource(id = R.string.app_name),
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )


                IconButton(onClick = { onEvent(ManEvent.SortMan) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.sort),
                        contentDescription = "Ordina Manutenzione",
                        modifier = Modifier.size(35.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                state.title.value = ""
                state.description.value = ""
                navController.navigate("AddManScreen")
            }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Aggiungi Manutenzione")
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

            items(state.men.size) { index ->
                ManItem(
                    state = state,
                    index = index,
                    onEvent = onEvent
                )
            }

        }

    }

}

@Composable
fun ManItem(
    state: ManState,
    index: Int,
    onEvent: (ManEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = state.men[index].title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = state.men[index].description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

        }

        IconButton(
            onClick = {
                onEvent(ManEvent.DeleteMan(state.men[index]))
            }
        ) {

            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Elimina Manutenzione",
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )

        }

    }
}




