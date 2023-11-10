package com.marcpascualsanchez.url.shortener.api.response.exception

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.marcpascualsanchez.url.shortener.api.exception.ApiError
import java.io.Serializable

class ErrorResponse @JsonCreator constructor(@JsonProperty("error") val error: ApiError) : Serializable {

    override fun toString(): String {
        return "ErrorResponse(error=$error)"
    }

    companion object {
        fun of(appCode: String, message: String): ErrorResponse {
            return ErrorResponse(ApiError.of(appCode, message))
        }
    }
}