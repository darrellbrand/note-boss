package com.djf.noteboss.domain.usecase

import com.djf.noteboss.domain.model.Note
import com.djf.noteboss.domain.repository.NoteRepository

class DeleteNoteUseCase(private val noteRepository: NoteRepository) {

    suspend operator fun invoke(note: Note)  {
        noteRepository.deleteNote(note)
    }
}