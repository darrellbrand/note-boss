package com.djf.noteboss.presentation.add_edit_note.components

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djf.noteboss.domain.model.InvalidNoteException
import com.djf.noteboss.domain.model.Note
import com.djf.noteboss.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNoteUseCase(noteId)?.also {
                        currentNoteId = noteId
                        _noteTitle.value = _noteTitle.value.copy(
                            text = it.title,
                            isHintVisible = false
                        )
                        _noteContent.value = _noteContent.value.copy(
                            text = it.content
                        )
                        _noteColor.intValue = it.color
                        _noteContent.value = _noteContent.value.copy(
                            isLinkEnabled = it.hasLink,
                            link = it.link ?: "",
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    private var currentNoteId: Int? = null
    private val _noteTitle = mutableStateOf(NoteTextFieldState())
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState())
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _noteColor = mutableIntStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onEvent(event: AddEditNoteEvent) {
        when (event) {

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.intValue = event.color
            }

            is AddEditNoteEvent.ChangeContentFocus -> _noteContent.value = _noteContent.value.copy(
                isHintVisible = !event.focus.isFocused && noteContent.value.text.isBlank()
            )

            is AddEditNoteEvent.EnteredContent -> _noteContent.value =
                _noteContent.value.copy(text = event.value)

            is AddEditNoteEvent.EnteredTitle -> _noteTitle.value =
                _noteTitle.value.copy(text = event.value)

            is AddEditNoteEvent.ChangeTitleFocus -> _noteTitle.value = _noteTitle.value.copy(
                isHintVisible = !event.focus.isFocused && noteTitle.value.text.isBlank()
            )

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        if (noteContent.value.isLinkEnabled) {
                            _noteContent.value = _noteContent.value.copy(
                                link = noteUseCases.createLinkUseCase(noteContent.value.text)
                            )
                        }
                        noteUseCases.insertNoteUseCase(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId,
                                link = if (noteContent.value.isLinkEnabled) noteContent.value.link else "",
                                hasLink = noteContent.value.isLinkEnabled
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "empty exception"
                            )
                        )
                    }
                }
            }

            is AddEditNoteEvent.ToggleSwitch -> {
                _noteContent.value = _noteContent.value.copy(isLinkEnabled = event.enabled)
            }
        }

    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent() {}
        data object SaveNote : UiEvent()
    }
}