package com.djf.noteboss.domain.usecase

import com.djf.noteboss.domain.model.InvalidNoteException
import com.djf.noteboss.domain.repository.NoteApiRepository
import com.djf.noteboss.domain.util.APIResult
import retrofit2.Response

class CreateLinkUseCase(val repo: NoteApiRepository) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(message: String): String {
        if (message.isBlank()) {
            throw InvalidNoteException("The message for link creation can't be blank")
        }
        val response: Response<APIResult> = repo.createLink(message)
        if (response.isSuccessful) {
            val posts = response.body()
            val uuid = posts?.uuid
            val key = posts?.key
            return "https://hidemymind.com/read/${uuid}#${key}"

        } else {
            throw InvalidNoteException(" request failed  " + response.message())
        }

    }
}