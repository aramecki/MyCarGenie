package com.android.mycargenie.pages.profile

import android.content.Intent
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.mycargenie.R
import com.android.mycargenie.pages.manutenzione.ManEvent
import com.android.mycargenie.pages.rifornimento.RifEvent
import com.android.mycargenie.shared.formatDateToString
import java.time.Instant

@Composable
fun BackupScreen(
    navController: NavController,
    backupPermissionHandler: BackupPermissionHandler,
    onManEvent: (ManEvent) -> Unit,
    onRifEvent: (RifEvent) -> Unit,
) {
    val context = LocalContext.current

    backupPermissionHandler.initialize()

    val showDialog = remember { mutableStateOf(false) }
    val showWrongFileDialog = remember { mutableStateOf(false) }
    val isSuccess = remember { mutableStateOf(false) }
    val restoredMan = remember { mutableStateOf(true) }
    var date: String
    var isManDatabase = true
    var databaseName by remember { mutableStateOf("man.db") }

    var isLoading by remember { mutableStateOf(false) }

    // Per creare file backup
    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/sql"),
        onResult = { uri ->
            if (uri != null) {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                Log.e("Backup", "Esporto $databaseName.")
                exportDatabaseAsSql(context, uri, databaseName)
                isLoading = false
            } else {
                isLoading = false
                Log.e("Backup", "Nessun URI selezionato per il file di backup.")
            }
        }
    )

    val openFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                val fileName = context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    cursor.getString(nameIndex)
                }

                if (fileName != null && fileName.endsWith(".sql")) {
                    isLoading = true
                    if (databaseName == "man.db") {
                        onManEvent(ManEvent.DeleteAllMan)
                    } else {
                        onRifEvent(RifEvent.DeleteAllRif)
                    }
                    restoreDatabaseFromSql(context, uri, isManDatabase, databaseName) { success ->
                        isSuccess.value = success
                        isLoading = false
                        showDialog.value = true
                    }
                } else {
                    showWrongFileDialog.value = true
                    Log.e("Restore", "File selezionato non .sql.")
                }
            } else {
                Log.e("Restore", "Nessun file selezionato.")
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, top = 14.dp, bottom = 8.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.back),
                contentDescription = "Back to profile view screen", //To put in strings xml
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable {
                        navController.navigate("ProfileScreen")
                    }
            )
            Text(
                text = "Impostazioni Libretto", //To put in strings xml
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(start = 24.dp)
            )
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .alpha(0.2f)
                .padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Text(
                text = stringResource(R.string.maintenance),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Maintenance backup
            Button(
                onClick = {
                    date = formatDateToString(Instant.now().toEpochMilli())
                    databaseName = "man.db"
                    createFileLauncher.launch("MCG Man $date.sql")
                    isLoading = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Text(stringResource(R.string.backup))
            }

            // Maintenance restore
            Button(
                onClick = {
                    isManDatabase = true
                    databaseName = "man.db"
                    restoredMan.value = true
                    openFileLauncher.launch("*/*")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text(stringResource(R.string.restore))
            }

            HorizontalDivider()

            Text(
                text = stringResource(R.string.refueling),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 24.dp)
            )

            // Refueling backup
            Button(
                onClick = {
                    date = formatDateToString(Instant.now().toEpochMilli())
                    databaseName = "rif.db"
                    Log.e("Backup", "Hai scelto di esportare $databaseName.")
                    createFileLauncher.launch("MCG Rif $date.sql")
                    isLoading = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
            ) {
                Text(stringResource(R.string.backup))
            }

            // Refueling restore
            Button(
                onClick = {
                    isManDatabase = false
                    databaseName = "rif.db"
                    restoredMan.value = false
                    openFileLauncher.launch("*/*")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Text(stringResource(R.string.restore))
            }

            // Icona caricamento
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            Text(
                text = stringResource(R.string.proceed_restore_alert),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.error
            )

            // Dialog riavvio
            if (showDialog.value) {
                if (isSuccess.value) {
                    RestartAppDialog(
                        onDismiss = {
                            if (restoredMan.value) {
                                isManDatabase = false
                                databaseName = "rif.db"
                                openFileLauncher.launch("*/*")
                            } else {
                                isManDatabase = true
                                databaseName = "man.db"
                                openFileLauncher.launch("*/*")
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
                        title = { Text(stringResource(R.string.error_in_restoring)) },
                        text = { Text(stringResource(R.string.verify_backup_file)) },
                        confirmButton = {
                            Button(onClick = { showDialog.value = false }) {
                                Text(stringResource(R.string.ok))
                            }
                        }
                    )
                }
            }

            // Dialog file non valido
            if (showWrongFileDialog.value) {
                AlertDialog(
                    onDismissRequest = { showWrongFileDialog.value = false },
                    title = { Text(stringResource(R.string.error_in_restoring)) },
                    text = { Text(stringResource(R.string.not_valid_file)) },
                    confirmButton = {
                        Button(onClick = { showWrongFileDialog.value = false }) {
                            Text(stringResource(R.string.ok))
                        }
                    }
                )
            }
        }
    }
}



