package com.djf.noteboss.domain.usecase

import com.djf.noteboss.domain.model.InvalidNoteException
import com.djf.noteboss.domain.model.Note
import com.djf.noteboss.domain.repository.NoteRepository

class InsertNoteUseCase(private val noteRepository: NoteRepository) {

    suspend operator fun invoke(note: Note) {

        if (note.title.isBlank()) {
            throw InvalidNoteException("The title can't be blank")
        }
        if (note.content.isBlank()) {
            throw InvalidNoteException("The content can't be blank")
        }
        noteRepository.insertNote(note)
    }
}