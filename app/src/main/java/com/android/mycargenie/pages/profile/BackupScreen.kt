package com.android.mycargenie.pages.profile

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.android.mycargenie.shared.formatDateToString
import java.time.Instant

@Composable
fun BackupScreen(
    navController: NavController,
    backupPermissionHandler: BackupPermissionHandler
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val context = LocalContext.current

    backupPermissionHandler.initialize()

    val showDialog = remember { mutableStateOf(false) }
    val isSuccess = remember { mutableStateOf(false) }
    val restoredMan = remember { mutableStateOf(true) }
    var date: String
    var isManDatabase = true
    var databaseName = "man.db"

    // Launcher per creare un file di backup
    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/sql"),
        onResult = { uri ->
            if (uri != null) {
                exportDatabaseAsSql(context, uri, databaseName)

            } else {
                Log.e("Backup", "Nessun URI selezionato per il file di backup.")
            }
        }
    )

    val openFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                restoreDatabaseFromSql(context, uri, isManDatabase, databaseName) { success ->
                    isSuccess.value = success // Aggiornare lo stato con il risultato
                    showDialog.value = true // Mostra il dialogo
                }
            } else {
                Log.e("Restore", "Nessun file selezionato per il ripristino.")
            }
        }
    )


    /*
    var backPressedOnce by remember { mutableStateOf(false) }
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
            Toast.makeText(context, "Premi di nuovo per chiudere.", Toast.LENGTH_SHORT).show()
        }
    }

     */

    Scaffold/*(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
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
    )
     */{ padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Manutenzione"
            )

            // Maintenance backup
            Button(
                onClick = {
                    date = formatDateToString(Instant.now().toEpochMilli())
                    databaseName = "man.db"
                    createFileLauncher.launch("MCG Man $date.sql")
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text("Backup")
            }

            // Maintenance restore
            Button(
                onClick = {
                    isManDatabase = true
                    databaseName = "man.db"
                    restoredMan.value = true
                    openFileLauncher.launch(arrayOf("application/sql"))
                          },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ripristino")
            }

            HorizontalDivider()

            Text(
                text = "Rifornimento"
            )

            // Refueling backup
            Button(
                onClick = {
                    date = formatDateToString(Instant.now().toEpochMilli())
                    databaseName = "rif.db"
                    createFileLauncher.launch("MCG Rif $date.sql")
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text("Backup")
            }

            // Refueling restore
            Button(
                onClick = {
                    isManDatabase = false
                    databaseName = "rif.db"
                    restoredMan.value = false
                    openFileLauncher.launch(arrayOf("application/sql"))
                          },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ripristino")
            }

            // Dialog riavvio
            if (showDialog.value) {
                if (isSuccess.value) {
                    RestartAppDialog(
                        onDismiss = {
                            if (restoredMan.value) {
                                isManDatabase = false
                                databaseName = "rif.db"
                                openFileLauncher.launch(arrayOf("application/sql"))
                            } else {
                                isManDatabase = true
                                databaseName = "man.db"
                                openFileLauncher.launch(arrayOf("application/sql"))
                            }
                            showDialog.value = false
                        },
                        onRestart = {
                            restartApp(context)
                            showDialog.value = false
                        }
                    )
                } else {
                    AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        title = { Text("Errore nel ripristino") },
                        text = { Text("Verifica il file di backup, dopodiché riprova.") },
                        confirmButton = {
                            Button(onClick = { showDialog.value = false } ) {
                                Text("OK")
                            }
                        }
                    )
                }
            }
        }
    }
}



