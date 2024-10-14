package com.android.mycargenie.shared

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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