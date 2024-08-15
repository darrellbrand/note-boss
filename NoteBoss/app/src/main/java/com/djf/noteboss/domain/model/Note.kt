package com.djf.noteboss.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.djf.noteboss.ui.theme.blueNote
import com.djf.noteboss.ui.theme.greenNote
import com.djf.noteboss.ui.theme.orangeNote
import com.djf.noteboss.ui.theme.purpleNote
import com.djf.noteboss.ui.theme.yellowNote

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val link: String? = null,
    val hasLink: Boolean = false,
    @PrimaryKey
    val id: Int? = null

) {
    companion object {
        val noteColors = listOf(orangeNote, yellowNote, blueNote, purpleNote, greenNote)
    }
}

class InvalidNoteException(message: String) : Exception(message) {

}