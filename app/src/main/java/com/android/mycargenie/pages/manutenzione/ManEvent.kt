package com.android.mycargenie.pages.manutenzione

import com.android.mycargenie.data.Man

sealed interface ManEvent {
    data object SortMan : ManEvent

    data class DeleteMan(val man: Man): ManEvent

    data class SaveMan(
        val id: Int?,
        val title: String,
        val type: String,
        val place: String,
        val date: String,
        val kmt: Int,
        val description: String,
        val price: Double,
    ): ManEvent
}