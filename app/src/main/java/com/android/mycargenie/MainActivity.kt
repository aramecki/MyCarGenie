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
import com.android.mycargenie.pages.profile.ProfileViewModel
import com.android.mycargenie.pages.rifornimento.RifViewModel
import com.android.mycargenie.pages.scadenze.ExpirationsViewModel
import com.android.mycargenie.shared.CustomNotificationManager
import com.android.mycargenie.shared.PermissionHandler
import com.android.mycargenie.ui.MainApp
import com.android.mycargenie.ui.theme.MyCarGenieTheme


@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {

    private lateinit var permissionHandler: PermissionHandler

    private lateinit var notificationManager: CustomNotificationManager



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
            return when {
                modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                    ProfileViewModel(application) as T
                }
                modelClass.isAssignableFrom(ExpirationsViewModel::class.java) -> {
                    ExpirationsViewModel(application) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    private val profileViewModel: ProfileViewModel by viewModels { SetViewModelFactory(application) }

    private val expViewModel: ExpirationsViewModel by viewModels {SetViewModelFactory(application) }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inizializza il gestore dei permessi
        permissionHandler = PermissionHandler(this)
        permissionHandler.initialize()



        setContent {

            //deleteExistingDatabase()

            MyCarGenieTheme {


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val state by viewModel.state.collectAsState()
                    val rifState by rifViewModel.state.collectAsState()
                    val carProfile by profileViewModel.carProfile.collectAsState()
                    val expirations by expViewModel.expSettings.collectAsState()

                    MainApp(
                        viewModel = viewModel,
                        rifViewModel = rifViewModel,
                        profileViewModel = profileViewModel,
                        expirationsViewModel = expViewModel,
                        onManEvent = viewModel::onEvent,
                        onRifEvent = rifViewModel::onEvent,
                        state = state,
                        rifState = rifState,
                        carProfile = carProfile,
                        expirations = expirations
                    )
                }
            }
        }

        // Inizializza il CustomNotificationManager
        notificationManager = CustomNotificationManager(this)

        // Crea il canale di notifica (se necessario)
        notificationManager.createNotificationChannel()


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









