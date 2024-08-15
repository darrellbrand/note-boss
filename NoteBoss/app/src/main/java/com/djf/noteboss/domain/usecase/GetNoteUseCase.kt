package com.djf.noteboss.domain.usecase

import com.djf.noteboss.domain.model.Note
import com.djf.noteboss.domain.repository.NoteRepository

class GetNoteUseCase(private val noteRepository: NoteRepository) {


    suspend operator fun invoke(noteId: Int): Note? {
        return noteId.let { noteRepository.getNoteById(it) }
    }
}