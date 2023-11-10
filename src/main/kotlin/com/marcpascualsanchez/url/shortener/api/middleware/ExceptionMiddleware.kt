package com.marcpascualsanchez.url.shortener.api.middleware

import com.marcpascualsanchez.url.shortener.api.response.exception.ErrorResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class ExceptionMiddleware {
    private val log: Logger = LoggerFactory.getLogger(ExceptionMiddleware::class.java)

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(exception: Exception): ResponseEntity<ErrorResponse> {
        log.error(exception.message, exception)
        return ResponseEntity(
            ErrorResponse.of("INTERNAL_SERVER_ERROR", exception.message ?: DEFAULT_ERROR_MESSAGE),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "Unexpected error"
    }
}