package com.android.mycargenie.pages.scadenze

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R

@Composable
fun ExpScreen(
    expirations: Expirations,
    navController: NavController
) {

    var isInsActive by remember { mutableStateOf(false) }
    var isTaxActive by remember { mutableStateOf(false) }
    var isRevActive by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("ExpirationsSettings")
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                    contentDescription = "${stringResource(R.string.settings)} ${stringResource(R.string.deadlines)}"
                )
            }
        }
    ) { padding ->

        Spacer(modifier = Modifier.height(16.dp).padding(padding))

        Column(
            modifier = Modifier
                .padding(top = 32.dp)
        ) {
            if (expirations.inscheck) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(12.dp)
                ) {
                    if (expirations.insstart.isNotEmpty() || expirations.insend.isNotEmpty() || expirations.insdues != 0 || expirations.insplace.isNotEmpty() || expirations.insprice != 0.0f) {
                        isInsActive = true
                        Text(
                            text = stringResource(R.string.insurance),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                        )

                        if (expirations.insstart.isNotEmpty()) {
                            Text(
                                text = "${stringResource(R.string.start)} ${stringResource(R.string.coverage)}: ${expirations.insstart}",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                        }
                        if (expirations.insend.isNotEmpty()) {
                            Text(
                                text = "${stringResource(R.string.end)} ${stringResource(R.string.coverage)}: ${expirations.insend}",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                        }
                        if (expirations.insdues != 0) {
                            Text(
                                text = "${stringResource(R.string.dues)}: ${expirations.insdues}",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            if (expirations.insplace.isNotEmpty()) {
                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.store),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier
                                            .size(26.dp)
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = expirations.insplace,
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            if (expirations.insprice != 0.0f) {
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${expirations.insprice}€",
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            if (expirations.taxcheck) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(12.dp)
                ) {
                    if (expirations.taxdate.isNotEmpty() || expirations.taxprice != 0.0f) {
                        isTaxActive = true
                        Text(
                            text = "${stringResource(R.string.tax)} ${stringResource(R.string.automotive)}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                        )

                        if (expirations.taxdate.isNotEmpty()) {
                            Text(
                                text = "${stringResource(R.string.next_m)} ${stringResource(R.string.payment)}: ${expirations.taxdate}",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        if (expirations.taxprice != 0.0f) {
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "${expirations.taxprice}€",
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                        }

                    }
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            if (expirations.revcheck) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(12.dp)
                ) {
                    if (expirations.revlast.isNotEmpty() || expirations.revnext.isNotEmpty() || expirations.revplace.isNotEmpty()) {
                        isRevActive = true
                        Text(
                            text = stringResource(R.string.revision),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                        )

                        if (expirations.revlast.isNotEmpty()) {
                            Text(
                                text = "${stringResource(R.string.last)} ${stringResource(R.string.revision)}: ${expirations.revlast}",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        if (expirations.revnext.isNotEmpty()) {
                            Text(
                                text = "${stringResource(R.string.next_m)} ${stringResource(R.string.revision)}: ${expirations.revnext}",
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        if (expirations.revplace.isNotEmpty()) {
                            Row {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.store),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier
                                        .size(26.dp)
                                )
                                Text(
                                    text = expirations.revplace,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                        }

                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (!isInsActive && !isTaxActive && !isRevActive) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.configure_exp_message),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(start = 32.dp, top = 82.dp, bottom = 15.dp, end = 32.dp)
                )

                Button(onClick = {
                    navController.navigate("ExpirationsSettings")
                }) {
                    Text(
                        text = stringResource(R.string.configure),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}