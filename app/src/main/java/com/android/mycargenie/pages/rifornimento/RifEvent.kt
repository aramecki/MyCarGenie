package com.android.mycargenie.pages.rifornimento

import com.android.mycargenie.data.Rif

sealed interface RifEvent {
    data object SortRif : RifEvent

    data class DeleteRif(val rif: Rif): RifEvent

    data class SaveRif(
        val id: Int?,
        val type: String,
        val place: String,
        val price: Double,
        val uvalue: Double,
        val totunit: Double,
        val date: String,
        val note: String,
        val kmt: Int,

    ): RifEvent

    data class UpdateRif(
        val id: Int,
        val type: String,
        val place: String,
        val price: Double,
        val uvalue: Double,
        val totunit: Double,
        val date: String,
        val note: String,
        val kmt: Int,
    ): RifEvent

}