package com.android.mycargenie.pages.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R

data class CarProfile(
    var brand: String,
    var model: String,
    var engine: String,
    var power: String,
    var horsepower: String
)

@Composable
fun ProfileScreen(
    carProfile: CarProfile,
    navController: NavController
) {

    var localCarProfile by remember { mutableStateOf(carProfile) }

    LaunchedEffect(carProfile) {
        localCarProfile = carProfile
    }

    Row {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = {
                navController.navigate("ProfileSettings")
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


    Spacer(modifier = Modifier.height(40.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(start = 32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.subaru_baracca),
                contentDescription = "Automobile",
                modifier = Modifier
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp)
        ) {
            Row {
                Text(
                    text = carProfile.brand,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(top = 16.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = carProfile.model,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row {

                Text(
                    text = carProfile.engine,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(end = 4.dp)
                )

                Text(
                    text = carProfile.power,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(end = 4.dp)
                )

                Text(
                    text = carProfile.horsepower,
                    fontSize = 16.sp
                )
            }
        }
    }

}