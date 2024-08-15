package com.djf.noteboss.presentation.add_edit_note.components

data class NoteTextFieldState(
    val text: String ="",
    val hint: String = "",
    val isHintVisible: Boolean = true,
    val isLinkEnabled : Boolean = false,
    val link : String = ""
)