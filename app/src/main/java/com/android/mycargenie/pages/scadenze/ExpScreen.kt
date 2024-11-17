package com.android.mycargenie.pages.scadenze

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                    contentDescription = stringResource(R.string.settings),
                    modifier = Modifier
                        .size(42.dp)
                        .padding(top = 8.dp, end = 8.dp),
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier
            .padding(top = 32.dp, start = 8.dp)
    ) {

        if (expirations.inscheck) {
            if (expirations.insstart.isNotEmpty() || expirations.insend.isNotEmpty() || expirations.insdues != 0 || expirations.insplace.isNotEmpty() || expirations.insprice != 0.0f) {
                isInsActive = true
                Text(
                    text = stringResource(R.string.insurance),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )

                if (expirations.insstart.isNotEmpty()) {
                    Text(text = "${stringResource(R.string.start)} ${stringResource(R.string.coverage)}: ${expirations.insstart}", fontSize = 20.sp)
                }
                if (expirations.insend.isNotEmpty()) {
                    Text(text = "${stringResource(R.string.end)} ${stringResource(R.string.coverage)}: ${expirations.insend}", fontSize = 20.sp)
                }
                if (expirations.insdues != 0) {
                    Text(text = "${stringResource(R.string.dues)}: ${expirations.insdues}", fontSize = 20.sp)
                }
                if (expirations.insplace.isNotEmpty()) {
                    Text(text = "${stringResource(R.string.insurer)}: ${expirations.insplace}", fontSize = 20.sp)
                }
                if (expirations.insprice != 0.0f) {
                    Text(text = "${stringResource(R.string.amount)} ${stringResource(R.string.total_e)}: ${expirations.insprice}€", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }

        if (expirations.taxcheck) {
            if(expirations.taxdate.isNotEmpty() || expirations.taxprice != 0.0f) {
                isTaxActive = true
                Text(
                    text = "${stringResource(R.string.tax)} ${stringResource(R.string.automotive)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )

                if (expirations.taxdate.isNotEmpty()) {
                    Text(text = "${stringResource(R.string.next)} ${stringResource(R.string.payment)}: ${expirations.taxdate}", fontSize = 20.sp)
                }
                if (expirations.taxprice != 0.0f) {
                    Text(text = "${stringResource(R.string.amount)}: ${expirations.taxprice}€", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }

        if (expirations.revcheck) {

            if (expirations.revlast.isNotEmpty() || expirations.revnext.isNotEmpty() || expirations.revplace.isNotEmpty()) {
                isRevActive = true
                Text(
                    text = stringResource(R.string.revision),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                )

                if (expirations.revlast.isNotEmpty()) {
                    Text(text = "${stringResource(R.string.last)} ${stringResource(R.string.revision)}: ${expirations.revlast}", fontSize = 20.sp)
                }
                if (expirations.revnext.isNotEmpty()) {
                    Text(text = "${stringResource(R.string.next)} ${stringResource(R.string.revision)}: ${expirations.revnext}", fontSize = 20.sp)
                }
                if (expirations.revplace.isNotEmpty()) {
                    Text(text = "${stringResource(R.string.revplace)}: ${expirations.revplace}", fontSize = 20.sp)
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
                fontSize = 20.sp,
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