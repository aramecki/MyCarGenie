package com.android.mycargenie.pages.rifornimento

import com.android.mycargenie.data.Rif

sealed interface RifEvent {
    data object SortRif : RifEvent

    data class DeleteRif(val rif: Rif): RifEvent

    data class SaveRif(
        val id: Int?,
        val title: String,
        val type: String,
        val place: String,
        val date: String,
        val kmt: Int,
        val description: String,
        val price: Double,
    ): RifEvent

    data class UpdateRif(
        val id: Int,
        val title: String,
        val type: String,
        val place: String,
        val date: String,
        val kmt: Int,
        val description: String,
        val price: Double,
    ): RifEvent

}