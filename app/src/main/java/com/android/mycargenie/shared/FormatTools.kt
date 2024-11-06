package com.android.mycargenie.shared

import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//Funzione per formattare il prezzo(Double) in modo da separare le migliaia con un punto e i decimali con una virgola
fun formatPrice(price: Double): String {
    val decimalFormat = DecimalFormat("#,##0.00").apply {
        decimalFormatSymbols = DecimalFormatSymbols().apply {
            groupingSeparator = '.'
            decimalSeparator = '.'
        }
    }
    return decimalFormat.format(price)
}


//Funzione per formattare i kilometri(Int) in modo da separare le migliaia e i milioni con un punto
fun formatKmt(kmt: Int): String {
    val kmtFormat = DecimalFormat("#,###").apply {
        decimalFormatSymbols = DecimalFormatSymbols().apply {
            groupingSeparator = '.'
        }
    }
    return kmtFormat.format(kmt)
}

//Funzione per formattare la cilindrata nell'orine di unità e decimi
fun formatDisplacement(displacement: Int): String {
    val formatted = (displacement / 1000.0)
    return "%.1f".format(formatted).replace(",", ".")
}


//Funzione per formattare la data in gg/MM/yyyy
fun formatDateToString(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

fun formatDateToLong(dateString: String): Long {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return try {
        val date: Date = dateFormat.parse(dateString) ?: throw IllegalArgumentException("Data non valida")
        date.time
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }

}

// Funzione per formattare l'ora
fun formatTime(timestamp: Long): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(Date(timestamp))
}


//Funzione per salvare l'immagine del profilo
fun saveImageToMmry(context: Context, imageUri: Uri): String {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val file = File(context.filesDir, "profile_image.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}



private const val TAG = "ReminderApp"
private const val CHANNEL_ID = "reminder_channel"

class CustomNotificationManager(private val context: Context) {

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private fun checkNotificationPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= 33) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun isNotificationEnabled(): Boolean {
        val channel = notificationManager.getNotificationChannel(CHANNEL_ID)
        return checkNotificationPermissions() &&
                notificationManager.areNotificationsEnabled() &&
                (channel == null || channel.importance != NotificationManager.IMPORTANCE_NONE)
    }

    fun createNotificationChannel() {
        val channelName = "Promemoria"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
            description = "Canale per le notifiche di promemoria"
            enableVibration(true)
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
        }

        notificationManager.createNotificationChannel(channel)
        Log.d(TAG, "Canale di notifica creato: $CHANNEL_ID")
    }

    private val scheduledTimestamps = mutableMapOf<String, MutableList<Long>>()

    fun scheduleNotification(timeInMillis: Long, title: String, message: String, category: String) {
        if (!isNotificationEnabled()) {
            Log.w(TAG, "Le notifiche non sono abilitate!")
            return
        }

        // Controlla se l'app ha il permesso di pianificare allarmi esatti
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !canScheduleExactAlarms(context)) {
            Log.w(TAG, "L'app non ha il permesso di pianificare allarmi esatti.")
            // Qui puoi guidare l'utente a concedere il permesso, se necessario.
            return
        }

        val delay = timeInMillis - System.currentTimeMillis()
        if (delay > 0) {
            // Crea un Intent per il BroadcastReceiver
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("notification_title", title)
                putExtra("notification_message", message)
                putExtra("notification_category", category)
            }

            val requestCode = "$category-notification-$timeInMillis".hashCode()

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Inizializza la lista per la categoria se non esiste
            scheduledTimestamps.computeIfAbsent(category) { mutableListOf() }
            // Aggiungi il timestamp alla categoria
            scheduledTimestamps[category]?.add(timeInMillis)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Imposta l'allarme
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

            Log.d(TAG, "Notifica programmata: $title in $delay ms il ${formatDateToString(timeInMillis)} alle ${formatTime(timeInMillis)}. Categoria: $category")
        } else {
            Log.d(TAG, "Notifica scaduta: $title in $delay ms il ${formatDateToString(timeInMillis)} alle ${formatTime(timeInMillis)}. Categoria: $category")
        }
    }

    fun getScheduledTimestamps(category: String): List<Long> {
        // Restituisce la lista di timestamp per la categoria specificata, o una lista vuota se non ci sono timestamp
        return scheduledTimestamps[category] ?: emptyList()
    }

    fun disableNotificationsForCategory(context: Context, category: String, timestamps: List<Long>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Cancella i PendingIntent trovati
        var numCancelled = 0
        timestamps.forEach { timeInMillis ->
            val requestCode = "$category-notification-$timeInMillis".hashCode()
            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra("notification_category", category)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )

            // Se il PendingIntent esiste, cancella
            pendingIntent?.let {
                alarmManager.cancel(it)
                numCancelled++
            }
        }

        Log.d(TAG, "Notifiche nella categoria '$category' disattivate. $numCancelled notifiche cancellate.")
    }

    fun disableNotifications(category: String) {
        // Disattiva le notifiche per la categoria specificata
        disableNotificationsForCategory(context, category, getScheduledTimestamps(category))

        // Cancella i timestamps dalla mappa se non servono più
        scheduledTimestamps.remove(category) // Rimuovi la categoria dalla mappa
    }



}



class PermissionHandler(private val activity: ComponentActivity) {

    companion object {
        private const val TAG = "PermissionHandler"
    }

    private val requestNotificationPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Permesso notifiche concesso")
        } else {
            Log.d(TAG, "Permesso notifiche negato")
        }
        // Dopo aver gestito il permesso per le notifiche, richiedi il permesso per gli allarmi
        checkAlarmPermission()
    }

    fun initialize() {
        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        Log.d(TAG, "Inizio controllo permessi")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d(TAG, "Controllo permessi per API level 31 o superiore")
            // Controllo per notifiche
            checkNotificationPermission()
        } else {
            Log.d(TAG, "API level insufficiente, nessun permesso richiesto per allarmi esatti.")
        }
    }

    private fun checkNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "Permesso notifiche già concesso")
                // Permesso notifiche già concesso, passa alla verifica del permesso per gli allarmi
                checkAlarmPermission()
            }
            activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                showPermissionRationale()
            }
            else -> {
                Log.d(TAG, "Richiesta permesso notifiche")
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun checkAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (alarmManager.canScheduleExactAlarms()) {
                Log.d(TAG, "Permesso per allarmi esatti già concesso")
            } else {
                Log.d(TAG, "Permesso per allarmi esatti non ancora concesso")
                showAlarmPermissionRationale() // Solo se il permesso non è stato concesso
            }
        } else {
            Log.d(TAG, "API level inferiore a 31, nessun permesso per allarmi esatti richiesto.")
        }
    }

    private fun showPermissionRationale() {
        // Mostra un dialog per spiegare perché serve il permesso per le notifiche
        AlertDialog.Builder(activity)
            .setTitle("Permesso richiesto")
            .setMessage("Questa applicazione ha bisogno del permesso per inviare notifiche. Questo è necessario per informarti sugli eventi importanti.")
            .setPositiveButton("OK") { _, _ ->
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("Annulla") { dialog, _ ->
                dialog.dismiss() // Chiudi il dialogo se l'utente annulla
            }
            .create()
            .show()
    }

    private fun showAlarmPermissionRationale() {
        AlertDialog.Builder(activity)
            .setTitle("Permesso per allarmi esatti richiesto")
            .setMessage("Questa applicazione ha bisogno del permesso per pianificare allarmi esatti. Questo è necessario per assicurarti di ricevere promemoria precisi.")
            .setPositiveButton("Concedi") { _, _ ->
                requestAlarmPermission()
            }
            .setNegativeButton("Annulla") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun requestAlarmPermission() {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.parse("package:${activity.packageName}")
        }
        try {
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Errore nel tentativo di aprire le impostazioni per il permesso di allarmi esatti", e)
        }
    }
}


class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("notification_title") ?: "Promemoria"
        val message = intent.getStringExtra("notification_message") ?: "Hai un promemoria!"

        sendNotification(context, title, message)
    }

    private fun sendNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Assicurati che il canale esista
        val channelId = "reminder_channel"
        val channel = NotificationChannel(channelId, "Promemoria", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Canale per le notifiche di promemoria"
        }
        notificationManager.createNotificationChannel(channel)

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .build()

        notificationManager.notify(0, notification)
    }
}

private fun canScheduleExactAlarms(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.canScheduleExactAlarms()
    } else {
        true // Prima di Android 12, il permesso è implicito
    }
}



