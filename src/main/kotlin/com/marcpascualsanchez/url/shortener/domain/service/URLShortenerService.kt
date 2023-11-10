package com.marcpascualsanchez.url.shortener.domain.service

import com.marcpascualsanchez.url.shortener.domain.entity.URL
import com.marcpascualsanchez.url.shortener.domain.repository.ShortenedURLsRepository
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.*

@Service
class URLShortenerService(
    private val shortenedURLsRepository: ShortenedURLsRepository
) {
    fun createShorterURL(originalURL: String): String {
        val shortenedURL = short(originalURL)
        shortenedURLsRepository.save(URL(originalURL, shortenedURL))
        return shortenedURL
    }

    private fun short(URL: String): String {
        val hash = MD5.digest(URL.toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }

    fun findOriginalURLByShorterURL(shorterURL: String): String? {
        return shortenedURLsRepository.find(shorterURL)?.originalURL
    }

    companion object {
        val MD5: MessageDigest = MessageDigest.getInstance("MD5")
    }
}
