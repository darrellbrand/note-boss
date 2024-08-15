package com.djf.noteboss.data.data_source

import com.djf.noteboss.domain.util.APIRequest
import com.djf.noteboss.domain.util.APIResult
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NoteApiService {
    @POST("create")
    suspend fun createLink(@Body newLink: APIRequest): Response<APIResult>
}