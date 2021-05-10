package com.adewijayanto.defilmsapp3.data.remote

import com.adewijayanto.vo.StatusMessage

class ApiResponse<T>(val status: StatusMessage, val body: T, val message: String?) {
    companion object {
        fun <T> success(body: T): ApiResponse<T> = ApiResponse(StatusMessage.SUCCESS, body, null)

        fun <T> error(msg: String, body: T): ApiResponse<T> =
            ApiResponse(StatusMessage.ERROR, body, msg)

        fun <T> empty(msg: String, body: T): ApiResponse<T> =
            ApiResponse(StatusMessage.EMPTY, body, msg)
    }
}