package com.marcpascualsanchez.url.shortener.steps

import com.marcpascualsanchez.url.shortener.BaseApplication
import com.marcpascualsanchez.url.shortener.api.response.ShortenedURLCreationResponse
import com.marcpascualsanchez.url.shortener.domain.repository.ShortenedURLsRepository
import com.marcpascualsanchez.url.shortener.domain.service.URLShortenerService
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.spring.CucumberContextConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.http.*
import org.springframework.stereotype.Service

@CucumberContextConfiguration
class CommonSteps : BaseApplication() {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>
    @Autowired
    lateinit var urlShortenerService: URLShortenerService

    @LocalServerPort
    private var port = 0
    private val testRestTemplate = TestRestTemplate()
    val baseUrl: String
        get() = "http://localhost:$port"
    private var currentResponse: ResponseEntity<String>? = null
    private var currentShortURL = ""

    @Given("there is no shortened URLs saved for {string}")
    fun `there is no shortened URLs saved for`(originalURL: String) {
        redisTemplate.delete(originalURL)
    }

    @Given("there is a shortened URLs saved for {string}")
    fun `there is a shortened URLs saved for`(originalURL: String) {
        currentShortURL = urlShortenerService.createShorterURL(originalURL)
    }

    @When("requested to short the URL {string}")
    fun `requested to short the URL`(originalURL: String) {
        currentResponse = baseCall(
            HttpEntity("{\"url\":\"$originalURL\"}", getDefaultHeaders()),
            "$baseUrl/api/v1/url-shortener/create",
            HttpMethod.POST
        )
    }

    @When("a request by the shorter URL is done")
    fun `a request by the shorter URL is done`() {
        currentResponse = baseCall(
            null,
            "$baseUrl/s/$currentShortURL",
            HttpMethod.GET
        )
    }

    @Then("there is a shortened URL saved for URL {string}")
    fun `there is a shortened URL saved for URL`(originalURL: String) {
        val allKeys = redisTemplate.scan(ScanOptions.scanOptions().match(ALL_REDIS_KEYS).build()).stream().toList()
        val keyForURL = allKeys.find { redisTemplate.opsForValue().get(it) == originalURL }
        assertThat(keyForURL).isNotNull
    }

    @Then("the response body contains short URL hash {string}")
    fun `the response body contains short URL`(expectedShortURL: String) {
        assertThat(currentResponse!!.body).isEqualTo("{\"shortenedURL\":\"$expectedShortURL\"}")
    }

    @Then("the response body is a redirection to {string}")
    fun `the response body is a redirection to`(redirectedURL: String) {
        assertThat(currentResponse!!.statusCode).isEqualTo(HttpStatus.FOUND)
        assertThat(currentResponse!!.headers["Location"]!!.first()).isEqualTo(redirectedURL)
    }

    private fun baseCall(
        entity: HttpEntity<*>?,
        path: String,
        httpMethod: HttpMethod
    ): ResponseEntity<String>? {
        return testRestTemplate.exchange(path, httpMethod, entity, String::class.java)
    }

    private fun getDefaultHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers["Content-Type"] = MediaType.APPLICATION_JSON_VALUE
        return headers
    }

    companion object {
        const val ALL_REDIS_KEYS = "*"
    }
}