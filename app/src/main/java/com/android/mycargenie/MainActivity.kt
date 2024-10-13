package com.android.mycargenie

import android.os.Bundle
import android.util.Log
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
import com.android.mycargenie.data.RifDatabase
import com.android.mycargenie.pages.manutenzione.ManViewModel
import com.android.mycargenie.pages.rifornimento.RifViewModel
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




    private val viewModel by viewModels<ManViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ManViewModel(database.dao()) as T
                }
            }
        }
    )

    private val rifViewModel by viewModels<RifViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RifViewModel(rifdatabase.dao()) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {

            //deleteExistingDatabase()

            MyCarGenieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp(viewModel = viewModel, rifViewModel = rifViewModel)
                }
            }
        }
    }

    private fun deleteExistingDatabase() {

        val databaseName = "rif.db"


        val deleted = this.deleteDatabase(databaseName)

        if (deleted) {
            Log.d("MainActivity", "Database $databaseName eliminato con successo.")
        } else {
            Log.d("MainActivity", "Il database $databaseName non esiste o non è stato eliminato.")
        }
    }
}








