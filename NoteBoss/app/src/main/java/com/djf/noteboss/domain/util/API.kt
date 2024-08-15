package com.djf.noteboss.domain.util

import java.util.UUID

data class APIResult(val uuid: UUID, val key: String)

data class APIRequest(
    val note: String,
    val encryption_type: Int,
    val lifetime: Int,
    val burn_after_read: Boolean
)