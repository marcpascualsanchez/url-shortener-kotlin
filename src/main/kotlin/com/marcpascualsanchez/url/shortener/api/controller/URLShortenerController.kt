package com.marcpascualsanchez.url.shortener.api.controller

import com.marcpascualsanchez.url.shortener.api.request.ShortenedURLCreationRequest
import com.marcpascualsanchez.url.shortener.api.response.ShortenedURLCreationResponse
import com.marcpascualsanchez.url.shortener.domain.service.URLShortenerService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/url-shortener")
class URLShortenerController(
    private val URLShortenerService: URLShortenerService
) {

    @PostMapping("/create")
    fun createShortenedURL(
        @Valid @RequestBody request: ShortenedURLCreationRequest
    ): ShortenedURLCreationResponse {
        return ShortenedURLCreationResponse(URLShortenerService.createShorterURL(request.URL))
    }
}