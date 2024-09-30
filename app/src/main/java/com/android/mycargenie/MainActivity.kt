package com.android.mycargenie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.android.mycargenie.data.ManDatabase
import com.android.mycargenie.pages.manutenzione.ManViewModel
import com.android.mycargenie.ui.MainApp
import com.android.mycargenie.ui.theme.MyCarGenieTheme

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            ManDatabase::class.java,
            "man.db"
        ).build()
    }

    private val viewModel by viewModels<ManViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ManViewModel(database.dao()) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCarGenieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Passiamo il viewModel a MainApp
                    MainApp(viewModel)
                }
            }
        }
    }
}






