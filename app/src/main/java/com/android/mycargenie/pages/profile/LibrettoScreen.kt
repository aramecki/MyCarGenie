package com.android.mycargenie.pages.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.android.mycargenie.R

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LibrettoScreen(
    carProfile: CarProfile,
    navController: NavController
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

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
            Toast.makeText(context, R.string.press_again_to_close, Toast.LENGTH_SHORT).show()
        }
    }

    var localCarProfile by remember { mutableStateOf(carProfile) }

    LaunchedEffect(carProfile) {
        localCarProfile = carProfile
    }

    var isExpanded by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    FloatingActionButton(onClick = {
                        navController.navigate("ProfileSettings")
                    },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                            contentDescription = "${stringResource(R.string.settings)} ${stringResource(R.string.profile)}"
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
                ) {
                    FloatingActionButton(onClick = {
                        navController.navigate("BackupScreen")
                    },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.backup),
                            contentDescription = "${stringResource(R.string.settings)} ${stringResource(R.string.backup)}"
                        )
                    }
                }

                FloatingActionButton(onClick = {
                    isExpanded = !isExpanded
                },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                    Icon(
                        imageVector =if (isExpanded) Icons.Outlined.KeyboardArrowDown else Icons.Outlined.KeyboardArrowUp,
                        contentDescription = stringResource(R.string.settings)
                    )
                }
            }
        }
    ) { padding ->

        val fontScale = when {
            screenWidth <= 360 -> 0.8f
            else -> 1f
        }

        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        isExpanded = false
                    })
                }
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .graphicsLayer { alpha = 1f }
            ) {
                Text(
                    text = stringResource(R.string.profile),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .alpha(0.2f)
                    .graphicsLayer { alpha = 1f }
                    .padding(bottom = 16.dp)
            )

            if (carProfile.brand.isEmpty()) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    Text(
                        text = stringResource(R.string.configure_profile_message),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 32.dp, vertical = 16.dp)
                    )


                    Button(onClick = {
                        navController.navigate("ProfileSettings")
                    }) {
                        Text(
                            text = stringResource(R.string.configure),
                            fontSize = 16.sp
                        )
                    }
                }
            } else {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (carProfile.savedImagePath.isNotEmpty()) {
                        val imagePainter =
                            rememberAsyncImagePainter(model = carProfile.savedImagePath)

                        Image(
                            painter = imagePainter,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(
                                    when {
                                        screenWidth <= 360 -> 180.dp
                                        screenWidth <= 430 -> 220.dp
                                        else -> 250.dp
                                    }
                                ),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Column {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = carProfile.brand,
                            fontSize = 37.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .scale(fontScale)
                        )

                        val modelFontSize = when {
                            carProfile.model.length < 5 -> 34.sp
                            carProfile.model.length < 10 -> 32.sp
                            carProfile.model.length < 15 -> 29.sp
                            carProfile.model.length < 20 -> 25.sp
                            else -> 24.sp
                        }

                        Text(
                            text = carProfile.model,
                            fontSize = modelFontSize,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .scale(fontScale)
                        )

                        if (carProfile.conf.isNotEmpty()) {

                            val confFontSize = when {
                                carProfile.conf.length < 10 -> 18.sp
                                carProfile.conf.length < 20 -> 16.sp
                                else -> 14.sp
                            }

                            Text(
                                text = carProfile.conf,
                                fontSize = confFontSize,
                                modifier = Modifier
                                    .scale(fontScale)
                            )
                        }

                    }


                    Spacer(modifier = Modifier.height( when {
                        screenWidth <= 360 -> 0.dp
                        else -> 15.dp
                    }))


                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp)
                    ) {

                        val powerHorseFontSize = when {
                            carProfile.power.toString().length < 6 -> 24.sp
                            else -> 15.sp
                        }

                        val tagPadding = when {
                            screenWidth <= 360 -> 0.dp
                            else -> 3.dp
                        }

                        //Cilindrata
                        if (carProfile.displacement != 0) {

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.displacement),
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .scale(fontScale)
                                    )
                                Row(
                                    modifier = Modifier
                                        .border(
                                            border = ButtonDefaults.outlinedButtonBorder(),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                ) {

                                    Text(
                                        text = "${carProfile.displacement}",
                                        fontSize = powerHorseFontSize,
                                        modifier = Modifier
                                            .scale(fontScale)
                                    )
                                    Text(
                                        text = stringResource(R.string.cc),
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(tagPadding, bottom = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        //Potenza
                        if (carProfile.power > 0.0) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.power),
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .scale(fontScale)
                                )
                                Row(
                                    modifier = Modifier
                                        .border(
                                            border = ButtonDefaults.outlinedButtonBorder(),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${carProfile.power}",
                                        fontSize = powerHorseFontSize,
                                        modifier = Modifier
                                            .scale(fontScale)
                                    )
                                    Text(
                                        text = stringResource(R.string.kW),
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(tagPadding, bottom = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        //Cavalli
                        if (carProfile.horsepower > 0.0) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.horses),
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .scale(fontScale)
                                )
                                Row(
                                    modifier = Modifier
                                        .border(
                                            border = ButtonDefaults.outlinedButtonBorder(),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${carProfile.horsepower}",
                                        fontSize = powerHorseFontSize,
                                        modifier = Modifier
                                            .scale(fontScale)
                                    )
                                    Text(
                                        text = stringResource(R.string.CV),
                                        fontSize = 10.sp,
                                        modifier = Modifier
                                            .padding(tagPadding, bottom = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier
                        .height(when {
                            screenWidth <= 360 -> 20.dp
                            screenWidth <= 430 -> 30.dp
                            screenWidth <= 450 -> 35.dp
                            else -> 40.dp
                        }))


                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, end = 32.dp)
                    ) {

                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .weight(1f)
                        ) {

                            //Tipo
                            val typeFontSize = when {
                                carProfile.type.length < 10 -> 24.sp
                                carProfile.type.length < 13 -> 18.sp
                                else -> 15.sp
                            }

                            val typeInnerPadding = when {
                                carProfile.type.length < 10 -> 12.dp
                                else -> 14.dp
                            }

                            if (carProfile.type.isNotEmpty()) {
                                Row {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.type),
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .scale(fontScale)
                                        )
                                        Row(
                                            modifier = Modifier
                                                .border(
                                                    border = ButtonDefaults.outlinedButtonBorder(),
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(typeInnerPadding)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = carProfile.type,
                                                fontSize = typeFontSize,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .scale(fontScale)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }

                            //Alimentazione
                            if (carProfile.fuel.isNotEmpty()) {
                                Row {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.fuel),
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .scale(fontScale)
                                        )
                                        Row(
                                            modifier = Modifier
                                                .border(
                                                    border = ButtonDefaults.outlinedButtonBorder(),
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(12.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = carProfile.fuel,
                                                fontSize = 24.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .scale(fontScale)
                                            )
                                        }
                                    }
                                }

                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))


                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {

                            //Anno
                            if (carProfile.year != 0) {
                                Row {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.year),
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .scale(fontScale)
                                        )
                                        Row(
                                            modifier = Modifier
                                                .border(
                                                    border = ButtonDefaults.outlinedButtonBorder(),
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(12.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = carProfile.year.toString(),
                                                fontSize = 24.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .scale(fontScale)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }

                            //Inquinamento
                            if (carProfile.eco.isNotEmpty()) {
                                Row {
                                    Column {
                                        Text(
                                            text = stringResource(R.string.eco),
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .scale(fontScale)
                                        )
                                        Row(
                                            modifier = Modifier
                                                .border(
                                                    border = ButtonDefaults.outlinedButtonBorder(),
                                                    shape = RoundedCornerShape(6.dp)
                                                )
                                                .padding(12.dp)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = carProfile.eco,
                                                fontSize = 24.sp,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .scale(fontScale)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
