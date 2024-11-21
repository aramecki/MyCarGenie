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
import com.android.mycargenie.data.ManDao
import com.android.mycargenie.data.ManDatabase
import com.android.mycargenie.data.RifDao
import com.android.mycargenie.data.RifDatabase
import com.android.mycargenie.pages.home.HomeViewModel
import com.android.mycargenie.pages.libretto.LibrettoViewModel
import com.android.mycargenie.pages.manutenzione.ManViewModel
import com.android.mycargenie.pages.rifornimento.RifViewModel
import com.android.mycargenie.pages.scadenze.CustomNotificationManager
import com.android.mycargenie.pages.scadenze.ExpirationsViewModel
import com.android.mycargenie.pages.scadenze.PermissionHandler
import com.android.mycargenie.ui.MainApp
import com.android.mycargenie.ui.theme.MyCarGenieTheme


@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {

    private lateinit var permissionHandler: PermissionHandler

    private lateinit var notificationManager: CustomNotificationManager



    private val manDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            ManDatabase::class.java,
            "man.db"
        ) .build()
    }

    private val rifDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            RifDatabase::class.java,
            "rif.db"
        ) .build()
    }

    class HomeViewModelFactory(
        private val manDao: ManDao,
        private val rifDao: RifDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            Log.d("HomeViewModelFactory", "Creating HomeViewModel with $manDao, $rifDao")
            return HomeViewModel(manDao, rifDao) as T
        }
    }

    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(manDatabase.dao(), rifDatabase.dao())
    }


    private val manViewModel by viewModels<ManViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ManViewModel::class.java)) {
                    return ManViewModel(manDatabase.dao()) as T
                }
                throw IllegalArgumentException(getString(R.string.unkViewModel))
            }
        }
    }

    private val rifViewModel by viewModels<RifViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RifViewModel::class.java)) {
                    return RifViewModel(rifDatabase.dao()) as T
                }
                throw IllegalArgumentException(getString(R.string.unkViewModel))
            }
        }
    }

    class SetViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when {
                modelClass.isAssignableFrom(LibrettoViewModel::class.java) -> {
                    LibrettoViewModel(application) as T
                }
                modelClass.isAssignableFrom(ExpirationsViewModel::class.java) -> {
                    ExpirationsViewModel(application) as T
                }
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }

    private val librettoViewModel: LibrettoViewModel by viewModels { SetViewModelFactory(application) }

    private val expViewModel: ExpirationsViewModel by viewModels {SetViewModelFactory(application) }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        permissionHandler = PermissionHandler(this)

        setContent {

            //deleteExistingDatabase()

            MyCarGenieTheme {


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val carProfile by librettoViewModel.carProfile.collectAsState()
                    val expirations by expViewModel.expSettings.collectAsState()

                    MainApp(
                        homeViewModel = homeViewModel,
                        manViewModel = manViewModel,
                        rifViewModel = rifViewModel,
                        librettoViewModel = librettoViewModel,
                        expirationsViewModel = expViewModel,
                        carProfile = carProfile,
                        expirations = expirations,
                        permissionHandler = permissionHandler
                    )
                }
            }
        }

        notificationManager = CustomNotificationManager(this)

        notificationManager.createNotificationChannel()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            })
        }


    }

    @Suppress("unused")
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









