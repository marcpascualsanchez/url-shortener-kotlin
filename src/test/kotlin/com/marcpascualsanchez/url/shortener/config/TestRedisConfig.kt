package com.marcpascualsanchez.url.shortener.config

import jakarta.annotation.PreDestroy
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import redis.embedded.RedisServer

@TestConfiguration
class TestRedisConfig(
    @Value("\${redis.host}") private val hostName: String,
    @Value("\${redis.port}") private val port: Int
) {
    lateinit var redisServer: RedisServer

    @Bean
    @Primary
    @DependsOn("redisServer")
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer().address = String.format("redis://%s:%s", hostName, port)
        return Redisson.create(config)
    }

    @Bean
    fun redisServer(): RedisServer {
        redisServer = RedisServer(port)
        redisServer.start()
        return redisServer
    }

    @PreDestroy
    fun stopRedis() {
        redisServer.stop()
    }
}