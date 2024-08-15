package com.djf.noteboss.data.repository

import com.djf.noteboss.data.data_source.NoteApiService
import com.djf.noteboss.domain.repository.NoteApiRepository
import com.djf.noteboss.domain.util.APIRequest
import com.djf.noteboss.domain.util.APIResult
import retrofit2.Response

class NoteApiRepositoryImpl(val api : NoteApiService) : NoteApiRepository {
    override suspend fun createLink(message: String) : Response<APIResult>{
        val request = APIRequest(note = message, lifetime = 0, encryption_type = 0, burn_after_read = false)
        val res = api.createLink(newLink = request)
        return res
    }
}