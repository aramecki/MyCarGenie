package com.android.mycargenie.pages.scadenze

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R
import com.android.mycargenie.shared.formatDateToShow

@Composable
fun ExpScreen(
    expirations: Expirations,
    navController: NavController
) {

    var backPressedOnce by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (backPressedOnce) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(4000)
            backPressedOnce = false
        }
    }

    BackHandler {
        if (backPressedOnce) {
            (context as? Activity)?.finish()
        } else {
            backPressedOnce = true
            Toast.makeText(context, "Premi di nuovo per chiudere.", Toast.LENGTH_SHORT).show()
        }
    }

    var isInsActive by remember { mutableStateOf(false) }
    var isTaxActive by remember { mutableStateOf(false) }
    var isRevActive by remember { mutableStateOf(false) }

    var insDrop by remember { mutableStateOf(false) }
    var revDrop by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("ExpirationsSettings")
            },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                    contentDescription = "${stringResource(R.string.settings)} ${stringResource(R.string.deadlines)}"
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.deadlines),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .alpha(0.2f)
            )


            Column(
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                if (expirations.inscheck) {

                    if (expirations.insstart.isNotEmpty() || expirations.insend.isNotEmpty() || expirations.insdues != 0 || expirations.insplace.isNotEmpty() || expirations.insprice != 0.0f) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(12.dp)
                         ) {
                            isInsActive = true
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.insurance),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                if (expirations.insstart.isNotEmpty() || expirations.insdues != 0) {
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowDropDown,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier
                                            .size(34.dp)
                                            .alpha(0.5f)
                                            .clickable { insDrop = !insDrop }
                                            .graphicsLayer {
                                                rotationZ = if (insDrop) 180f else 0f
                                            }
                                    )
                                }

                                if (expirations.insnot) {
                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.notifications),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier
                                                .size(22.dp)
                                                .alpha(0.5f)
                                        )
                                    }
                                }
                            }

                            AnimatedVisibility(
                                visible = insDrop,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                if (expirations.insstart.isNotEmpty()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(top = 2.dp, bottom = 2.dp)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.start),
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier
                                                .alpha(0.5f)
                                                .padding(end = 4.dp)
                                        )
                                        Text(
                                            text = formatDateToShow(expirations.insstart),
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier
                                                .alpha(0.5f)
                                        )
                                        if (expirations.insdues != 0) {
                                            Column(
                                                horizontalAlignment = Alignment.End,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                Row {
                                                    Text(
                                                        expirations.insdues.toString(),
                                                        fontSize = 18.sp,
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                        modifier = Modifier
                                                            .alpha(0.5f)
                                                            .padding(end = 4.dp)
                                                    )
                                                    Text(
                                                        stringResource(R.string.dues),
                                                        fontSize = 18.sp,
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                        modifier = Modifier
                                                            .alpha(0.5f)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (expirations.insend.isNotEmpty()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.watch),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(end = 4.dp)
                                            .alpha(0.5f)
                                    )
                                    Text(
                                        text = formatDateToShow(expirations.insend),
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier
                                            .alpha(0.5f)
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp)
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
                    if (expirations.taxdate.isNotEmpty() || expirations.taxprice != 0.0f) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(12.dp)
                    ) {
                            isTaxActive = true
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${stringResource(R.string.tax)} ${stringResource(R.string.automotive)}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                if (expirations.taxnot) {
                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp)
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.notifications),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier
                                                .size(22.dp)
                                                .alpha(0.5f)
                                        )
                                    }
                                }
                            }

                            if (expirations.taxdate.isNotEmpty()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.watch),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(end = 4.dp)
                                            .alpha(0.5f)
                                    )
                                    Text(
                                        text = formatDateToShow(expirations.taxdate),
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier
                                            .alpha(0.5f)
                                    )
                                }
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
                    if (expirations.revlast.isNotEmpty() || expirations.revnext.isNotEmpty() || expirations.revplace.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(12.dp)
                        ) {
                                isRevActive = true
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(R.string.revision),
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    if (expirations.revlast.isNotEmpty()) {
                                        Icon(
                                            imageVector = Icons.Outlined.ArrowDropDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier
                                                .size(34.dp)
                                                .alpha(0.5f)
                                                .clickable { revDrop = !revDrop }
                                                .graphicsLayer {
                                                    rotationZ = if (revDrop) 180f else 0f
                                                }
                                        )
                                    }

                                    if (expirations.revnot) {
                                        Column(
                                            horizontalAlignment = Alignment.End,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(R.drawable.notifications),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .alpha(0.5f)
                                            )
                                        }
                                    }
                                }

                                AnimatedVisibility(
                                    visible = revDrop,
                                    enter = fadeIn() + expandVertically(),
                                    exit = fadeOut() + shrinkVertically()
                                ) {
                                    if (expirations.revlast.isNotEmpty()) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .padding(top = 2.dp, bottom = 2.dp)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.last),
                                                fontSize = 18.sp,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier
                                                    .alpha(0.5f)
                                                    .padding(end = 4.dp)
                                            )
                                            Text(
                                                text = formatDateToShow(expirations.revlast),
                                                fontSize = 18.sp,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier
                                                    .alpha(0.5f)
                                            )
                                        }
                                    }
                                }


                                if (expirations.revnext.isNotEmpty()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.watch),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier
                                                .size(20.dp)
                                                .padding(end = 4.dp)
                                                .alpha(0.5f)
                                        )
                                        Text(
                                            text = formatDateToShow(expirations.revnext),
                                            fontSize = 18.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier
                                                .alpha(0.5f)
                                        )
                                    }
                                }
                                if (expirations.revplace.isNotEmpty()) {
                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
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
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
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