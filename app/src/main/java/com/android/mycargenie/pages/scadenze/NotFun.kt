package com.android.mycargenie.pages.scadenze

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
import com.android.mycargenie.R
import com.android.mycargenie.shared.formatDateToString
import com.android.mycargenie.shared.formatTime

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
        //Log.d(TAG, "Canale di notifica creato: $CHANNEL_ID")
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
            return
        }

        val delay = timeInMillis - System.currentTimeMillis()
        if (delay > 0) {
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

            scheduledTimestamps.computeIfAbsent(category) { mutableListOf() }
            scheduledTimestamps[category]?.add(timeInMillis)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

            Log.d(TAG, "Notifica programmata: $title in $delay ms il ${formatDateToString(timeInMillis)} alle ${formatTime(timeInMillis)}. Categoria: $category")
        } else {
            Log.d(TAG, "Notifica scaduta: $title in $delay ms il ${formatDateToString(timeInMillis)} alle ${formatTime(timeInMillis)}. Categoria: $category")
        }
    }

    private fun getScheduledTimestamps(category: String): List<Long> {
        return scheduledTimestamps[category] ?: emptyList()
    }

    private fun disableNotificationsForCategory(context: Context, category: String, timestamps: List<Long>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

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

            pendingIntent?.let {
                alarmManager.cancel(it)
                numCancelled++
            }
        }

        Log.d(TAG, "Notifiche nella categoria '$category' disattivate. $numCancelled notifiche cancellate.")
    }

    fun disableNotifications(category: String) {
        disableNotificationsForCategory(context, category, getScheduledTimestamps(category))

        scheduledTimestamps.remove(category)
    }

}


class PermissionHandler(private val activity: ComponentActivity) {

    private var onPermissionsGranted: (() -> Unit)? = null

    private val requestNotificationPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            //Log.d(TAG, "Permesso notifiche concesso")
            checkAlarmPermission()
        } else {
            //Log.d(TAG, "Permesso notifiche negato")
        }
    }

    fun initialize() {
        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        //Log.d(TAG, "Inizio controllo permessi")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            //Log.d(TAG, "Controllo permessi per API level 31 o superiore")
            checkNotificationPermission()
        } else {
            //Log.d(TAG, "API level insufficiente, nessun permesso richiesto per allarmi esatti.")
            onPermissionsGranted?.invoke()
        }
    }

    private fun checkNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                //Log.d(TAG, "Permesso notifiche già concesso")
                checkAlarmPermission()
            }
            activity.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                showPermissionRationale()
            }
            else -> {
                //Log.d(TAG, "Richiesta permesso notifiche")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    private fun checkAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (alarmManager.canScheduleExactAlarms()) {
                //Log.d(TAG, "Permesso per allarmi esatti già concesso")
                onPermissionsGranted?.invoke()
            } else {
                //Log.d(TAG, "Permesso per allarmi esatti non ancora concesso")
                showAlarmPermissionRationale()
            }
        } else {
            //Log.d(TAG, "API level inferiore a 31, nessun permesso per allarmi esatti richiesto.")
            onPermissionsGranted?.invoke()
        }
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(activity)
            .setTitle("Permesso richiesto")
            .setMessage("Questa applicazione ha bisogno della tua autorizzazione per inviare notifiche. Questo è necessario per informarti sugli eventi importanti.")
            .setPositiveButton("OK") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Annulla") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun showAlarmPermissionRationale() {
        AlertDialog.Builder(activity)
            .setTitle("Permesso richiesto")
            .setMessage("Nelle ultime versioni di Android le applicazioni hanno bisogno della tua autorizzazione per inviare notifiche. Sarai indirizzato ad una pagina delle impostazioni dove potrai concedere questa autorizzazione.")
            .setPositiveButton("Concedi") { _, _ ->
                requestAlarmPermission()
            }
            .setNegativeButton("Annulla") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun requestAlarmPermission() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${activity.packageName}")
            }
        } else {
            TODO("VERSION.SDK_INT < S")
        }
        try {
            activity.startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            //Log.e(TAG, "Errore nel tentativo di aprire le impostazioni per il permesso di allarmi esatti", e)
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
            .setSmallIcon(R.drawable.ic_stat_name)
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
        true
    }
}