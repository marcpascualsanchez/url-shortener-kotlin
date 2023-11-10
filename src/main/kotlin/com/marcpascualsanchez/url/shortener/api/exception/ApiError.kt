package com.marcpascualsanchez.url.shortener.api.exception

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.io.Serializable
import java.util.*

class ApiError @JsonCreator constructor(
    @field:JsonProperty("app_code") val appCode: String,
    @field:JsonProperty("message") val message: String,
) : Serializable {

    override fun toString(): String {
        return "ApiError(appCode='$appCode', message='$message')"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other.javaClass != this.javaClass) {
            return false
        }
        val apiError = other as ApiError
        return appCode == apiError.appCode && message == apiError.message
    }

    override fun hashCode(): Int {
        return Objects.hash(appCode, message)
    }

    companion object {
        fun of(appCode: @NotNull @NotEmpty String, message: String): ApiError {
            return ApiError(appCode, message)
        }
    }
}