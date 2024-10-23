package com.android.mycargenie.shared

import android.content.Context
import android.net.Uri
import androidx.datastore.core.IOException
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
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
            decimalSeparator = ','
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

//Funzione per formattare la data in gg/MM/yyyy
fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

//Funzione per salvare l'immagine del profilo
fun saveImgToMmry(context: Context, uri: Uri): String? {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val fileName = "profile_image_${System.currentTimeMillis()}.jpg"
    val file = File(context.filesDir, fileName)

    try {
        val outputStream: OutputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}