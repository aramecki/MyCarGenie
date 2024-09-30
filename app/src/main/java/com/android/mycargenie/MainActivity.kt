package com.android.mycargenie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.android.mycargenie.Data.NotesDatabase
import com.android.mycargenie.pages.manutenzione.AddNoteScreen
import com.android.mycargenie.pages.manutenzione.NotesViewModel
import com.android.mycargenie.ui.theme.MyCarGenieTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.android.mycargenie.pages.manutenzione.ManutenzioneScreen

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java,
            "notes.db"
        ).build()
    }

    private val viewModel by viewModels<NotesViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return NotesViewModel(database.dao()) as T
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

                    val state by viewModel.state.collectAsState()
                    val navController = rememberNavController()

                    NavHost(navController= navController, startDestination = "NotesScreen") {
                        composable("NotesScreen") {
                            ManutenzioneScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                        composable("AddNoteScreen") {
                            AddNoteScreen(
                                state = state,
                                navController = navController,
                                onEvent = viewModel::onEvent
                            )
                        }
                    }

                }
            }
        }
    }
}



