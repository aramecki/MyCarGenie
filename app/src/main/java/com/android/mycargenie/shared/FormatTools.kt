package com.android.mycargenie.shared

import android.content.Context
import android.net.Uri
import android.util.Log
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

//Funzione per formattare la data in Long
fun formatDateToLong(dateString: String): Long {
    Log.d("DebugDate", "Stringa ricevuta: $dateString")

    // Se la stringa è vuota, restituisce -1
    if (dateString.isEmpty()) {
        Log.w("DebugDate", "Stringa vuota ricevuta, restituisco -1")
        return -1
    }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    return try {
        val date: Date = dateFormat.parse(dateString) ?: throw IllegalArgumentException("Data non valida")
        date.time
    } catch (e: Exception) {
        e.printStackTrace()
        -1 // Fallback in caso di errore di parsing
    }
}

// Funzione per formattare la data
fun formatDateToShow(dateString: String): String {
    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    return try {
        val date = inputFormat.parse(dateString)
        outputFormat.format(date!!).lowercase(Locale.getDefault())
    } catch (e: Exception) {
        dateString
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


