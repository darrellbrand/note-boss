package com.djf.noteboss.presentation.notes.components

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djf.noteboss.domain.model.Note
import com.djf.noteboss.domain.usecase.NoteUseCases
import com.djf.noteboss.domain.util.NoteOrder
import com.djf.noteboss.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(private val useCase: NoteUseCases) : ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state
    private var lastDeletedNote: Note? = null
    private var getNotesJob: Job? = null

    init {
        getNotes(state.value.order)
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    lastDeletedNote = event.note
                    useCase.deleteNoteUseCase(event.note)
                }
            }

            is NotesEvent.Order -> {
                if (event.noteOrder::class == _state.value.order::class
                    && event.noteOrder.orderType == _state.value.order.orderType
                ) {
                 return
                }
                _state.value = _state.value.copy(
                    order = event.noteOrder
                )
                getNotes(noteOrder = _state.value.order)
            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    useCase.insertNoteUseCase(lastDeletedNote ?: return@launch)
                    lastDeletedNote = null
                }
            }

            NotesEvent.ToggleOrderSection -> {
                _state.value = _state.value.copy(
                    isOrderSectionVisible = !_state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = useCase.getNotesUseCase(noteOrder)
            .onEach { notes ->
                _state.value = _state.value.copy(
                    notes = notes,
                    order = noteOrder
                )
            }.launchIn(viewModelScope)
    }
}