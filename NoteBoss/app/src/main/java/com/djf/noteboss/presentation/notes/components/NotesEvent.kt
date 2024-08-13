package com.djf.noteboss.presentation.notes.components

import com.djf.noteboss.domain.model.Note
import com.djf.noteboss.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    data class RestoreNote(val note: Note) : NotesEvent()
    data object ToggleOrderSection : NotesEvent()
}