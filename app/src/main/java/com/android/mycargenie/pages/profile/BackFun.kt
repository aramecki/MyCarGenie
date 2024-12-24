package com.android.mycargenie.pages.profile

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.mycargenie.data.ManDatabase
import com.android.mycargenie.data.RifDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BackupPermissionHandler(private val activity: ComponentActivity) {

    //private var onPermissionsGranted: (() -> Unit)? = null

    private val requestBackupPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            //Log.d(TAG, "Permesso notifiche concesso")
            checkOldReadPermission()
        } else {
            //Log.d(TAG, "Permesso notifiche negato")
        }
    }

    fun initialize() {
        checkAndRequestMemoryPermissions()
    }

    private fun checkAndRequestMemoryPermissions() {
        //Log.d(TAG, "Inizio controllo permessi")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            Log.d(TAG, "Controllo permessi per API level 29 o inferiore.")
            checkOldWritePermission()
        //} else {
            //Log.d(TAG, "Controllo permessi per API level 30 o superiore")
            // Vedere API Storage Access Framework (SAF) o salvare i file in directory private dell'app.
            //onPermissionsGranted?.invoke()
        }
    }

    private fun checkOldWritePermission() {
        when {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "Permesso scrittura già concesso")
                checkOldReadPermission()
            }
            activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                showOldWritePermissionRationale()
            }
            else -> {
                Log.d(TAG, "Richiesta permesso scrittura")
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    requestBackupPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private fun checkOldReadPermission() {
        when {
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "Permesso lettura già concesso")
            }

            activity.shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showOldReadPermissionRationale()
            }

            else -> {
                Log.d(TAG, "Richiesta permesso lettura")
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    requestBackupPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private fun showOldWritePermissionRationale() {
        AlertDialog.Builder(activity)
            .setTitle("Permesso richiesto")
            .setMessage("Questa applicazione ha bisogno della tua autorizzazione per salvare file.")
            .setPositiveButton("Concedi") { _, _ ->
                requestBackupPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            .setNegativeButton("Annulla") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun showOldReadPermissionRationale() {
        AlertDialog.Builder(activity)
            .setTitle("Permesso richiesto")
            .setMessage("Questa applicazione ha bisogno della tua autorizzazione per leggere file.")
            .setPositiveButton("Concedi") { _, _ ->
                requestBackupPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .setNegativeButton("Annulla") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}

fun exportDatabaseAsSql(context: Context, outputUri: Uri, databaseName: String): Boolean {
    return try {
        val databasePath = context.getDatabasePath(databaseName)
        SQLiteDatabase.openDatabase(databasePath.path, null, SQLiteDatabase.OPEN_READONLY).use { db ->
            context.contentResolver.openOutputStream(outputUri)?.use { outputStream ->
                db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null).use { cursor ->
                    while (cursor.moveToNext()) {
                        val tableName = cursor.getString(0)
                        if (tableName != "android_metadata" && tableName != "sqlite_sequence" && tableName != "room_master_table") {
                            outputStream.write("-- Table: $tableName\n".toByteArray())

                            val tableData = db.rawQuery("SELECT * FROM $tableName", null)
                            while (tableData.moveToNext()) {
                                val values = (0 until tableData.columnCount).joinToString(", ") { i ->
                                    "\"${tableData.getString(i)}\""
                                }
                                outputStream.write("INSERT INTO $tableName VALUES ($values);\n".toByteArray())
                            }
                            tableData.close()
                        }
                    }
                }
                Log.d("Backup", "Backup $databaseName completato con successo.")
                true
            } ?: run {
                Log.e("Backup", "Impossibile aprire lo stream di output per ${databaseName}.")
                false
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("Backup", "Errore durante il backup di ${databaseName}: ${e.message}")
        false
    }
}

fun restoreDatabaseFromSql(context: Context, uri: Uri, isManDatabase: Boolean, databaseName: String, callback: (Boolean) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
       try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val sqlStatements = inputStream.bufferedReader().use { it.readText() }
                val database = if (isManDatabase) {
                    Room.databaseBuilder(
                        context,
                        ManDatabase::class.java,
                        databaseName
                    ).build()
                } else {
                    Room.databaseBuilder(
                        context,
                        RifDatabase::class.java,
                        databaseName
                    ).build()
                }

                database.runInTransaction {
                    val db = database.openHelper.writableDatabase
                    db.execSQL(sqlStatements)
                }
            }
            Log.d("Restore", "Ripristino $databaseName completato con successo.")
            callback(true)
        } catch (e: Exception) {
            Log.e("Restore", "Errore durante il ripristino di ${databaseName}: ${e.message}")
           callback(false)
        }
    }
}

@Composable
fun RestartAppDialog(onDismiss: () -> Unit, onRestart: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Riavvio richiesto") },
        text = { Text("Riavvia l'app per completare il ripristino.") },
        confirmButton = {
            Button(onClick = onRestart) {
                Text("Riavvia")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Voglio Ripristinare Altro")
            }
        }
    )
}

fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    Runtime.getRuntime().exit(0)
}