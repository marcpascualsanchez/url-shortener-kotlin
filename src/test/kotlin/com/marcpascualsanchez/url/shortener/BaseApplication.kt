package com.marcpascualsanchez.url.shortener

import com.marcpascualsanchez.url.shortener.config.TestRedisConfig
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [UrlShortenerApplication::class])
@ActiveProfiles("test")
@ContextConfiguration(classes = [TestRedisConfig::class])
abstract class BaseApplication
