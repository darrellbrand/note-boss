package com.djf.noteboss.presentation.notes.components

import com.djf.noteboss.domain.model.Note
import com.djf.noteboss.domain.util.NoteOrder
import com.djf.noteboss.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val order: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
