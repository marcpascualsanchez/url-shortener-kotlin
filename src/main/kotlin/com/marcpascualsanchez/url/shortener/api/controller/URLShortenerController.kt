package com.marcpascualsanchez.url.shortener.api.controller

import com.marcpascualsanchez.url.shortener.api.request.ShortenedURLCreationRequest
import com.marcpascualsanchez.url.shortener.api.response.ShortenedURLCreationResponse
import com.marcpascualsanchez.url.shortener.domain.service.URLShortenerService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("")
class URLShortenerController(
    private val urlShortenerService: URLShortenerService
) {

    @PostMapping("/api/v1/url-shortener/create")
    fun createShortenedURL(
        @Valid @RequestBody request: ShortenedURLCreationRequest
    ): ShortenedURLCreationResponse {
        return ShortenedURLCreationResponse(urlShortenerService.createShorterURL(request.URL))
    }

    @GetMapping("/s/{shortURL}")
    fun redirectToOriginalURL(
        @PathVariable shortURL: String,
        response: HttpServletResponse
    ) {
        val originalURL = urlShortenerService.findOriginalURLByShorterURL(shortURL)
        if (originalURL == null) {
            response.status = 404
        } else {
            response.setHeader("Location", originalURL)
            response.status = 302
        }
    }
}