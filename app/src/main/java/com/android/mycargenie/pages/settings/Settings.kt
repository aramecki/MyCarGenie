package com.android.mycargenie.pages.settings

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.android.mycargenie.R

@Composable
fun SettingsScreen() {

    IconButton(onClick = {
        TODO()
    }) {

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.euro_symbol),
            contentDescription = null
            )
    }

}