package com.marcpascualsanchez.url.shortener.infrastructure.repository.redis

import com.marcpascualsanchez.url.shortener.domain.entity.URL
import com.marcpascualsanchez.url.shortener.domain.repository.ShortenedURLsRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RedisShortenedURLsRepository(
    @Value("\${redis.time-to-live}") private val ttlValue: Long,
    @Value("\${redis.time-unit}") private val ttlUnit: TimeUnit,
    private val redisTemplate: RedisTemplate<String, String>,
) : ShortenedURLsRepository {
    override fun save(URL: URL) {
        redisTemplate.opsForValue()
            .set(
                URL.shortenedURL,
                URL.originalURL,
                ttlValue,
                ttlUnit,
            )
    }

    override fun find(shortenedURL: String): URL? {
        val originalURL = redisTemplate.opsForValue()[shortenedURL] ?: return null
        return URL(originalURL, shortenedURL)
    }
}