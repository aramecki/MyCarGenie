package com.android.mycargenie

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.android.mycargenie.data.ManDatabase
import com.android.mycargenie.data.RifDatabase
import com.android.mycargenie.pages.manutenzione.ManViewModel
import com.android.mycargenie.pages.rifornimento.RifViewModel
import com.android.mycargenie.pages.settings.SetViewModel
import com.android.mycargenie.ui.MainApp
import com.android.mycargenie.ui.theme.MyCarGenieTheme


class MainActivity : ComponentActivity() {


    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            ManDatabase::class.java,
            "man.db"
        ) .build()
    }

    private val rifdatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            RifDatabase::class.java,
            "rif.db"
        ) .build()
    }




    private val viewModel by viewModels<ManViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ManViewModel::class.java)) {
                    return ManViewModel(database.dao()) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class ManViewModel")
            }
        }
    }

    private val rifViewModel by viewModels<RifViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RifViewModel::class.java)) {
                    return RifViewModel(rifdatabase.dao()) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class RifViewModel")
            }
        }
    }

    class SetViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SetViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SetViewModel(application) as T // Passa l'application qui
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val setViewModel: SetViewModel by viewModels { SetViewModelFactory(application) }

/*
    private fun enableEdgeToEdge() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

 */

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            //deleteExistingDatabase()

            MyCarGenieTheme {


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val state by viewModel.state.collectAsState()
                    val rifState by rifViewModel.state.collectAsState()
                    val carProfile by setViewModel.carProfile.collectAsState() // Usa il carProfile di setViewModel

                    MainApp(
                        viewModel = viewModel,
                        rifViewModel = rifViewModel,
                        setViewModel = setViewModel,
                        onManEvent = viewModel::onEvent,
                        onRifEvent = rifViewModel::onEvent,
                        state = state,
                        rifState = rifState,
                        carProfile = carProfile
                    )
                }
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Logica per API 33+
                    finish()
                }
            })
        }




    }


    private fun deleteExistingDatabase() {

        val databaseName = "man.db"


        val deleted = this.deleteDatabase(databaseName)

        if (deleted) {
            Log.d("MainActivity", "Database $databaseName eliminato con successo.")
        } else {
            Log.d("MainActivity", "Il database $databaseName non esiste o non è stato eliminato.")
        }
    }
}








