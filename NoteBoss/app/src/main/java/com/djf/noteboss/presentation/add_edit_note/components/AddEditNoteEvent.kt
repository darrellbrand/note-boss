package com.djf.noteboss.presentation.add_edit_note.components

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.ui.focus.FocusState
import com.djf.noteboss.domain.model.Note


sealed  class AddEditNoteEvent {
    data class EnteredTitle(val value: String) :AddEditNoteEvent()
    data class ChangeTitleFocus(val focus: FocusState) :AddEditNoteEvent()
    data class EnteredContent(val value: String) :AddEditNoteEvent()
    data class ChangeContentFocus(val focus: FocusState) :AddEditNoteEvent()
    data class ChangeColor(val color : Int) :AddEditNoteEvent()
    data class ToggleSwitch(val enabled : Boolean) :AddEditNoteEvent()
    data object SaveNote: AddEditNoteEvent()
}
