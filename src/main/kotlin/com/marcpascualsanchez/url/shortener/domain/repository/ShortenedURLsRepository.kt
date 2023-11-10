package com.marcpascualsanchez.url.shortener.domain.repository

import com.marcpascualsanchez.url.shortener.domain.entity.URL

interface ShortenedURLsRepository {
    fun save(URL: URL)
    fun find(shortenedURL: String): URL?
}