package com.djf.noteboss.domain.repository

import com.djf.noteboss.domain.util.APIResult
import retrofit2.Response

interface NoteApiRepository {
    suspend fun createLink(message: String) : Response<APIResult>
}