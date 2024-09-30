package com.android.mycargenie.pages.manutenzione

import com.android.mycargenie.data.Man

sealed interface ManEvent {
    object SortMan: ManEvent

    data class DeleteMan(val man: Man): ManEvent

    data class SaveMan(
        val title: String,
        val date: String,
        val description: String
    ): ManEvent
}