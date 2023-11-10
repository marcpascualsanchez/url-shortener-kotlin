package com.marcpascualsanchez.url.shortener.steps

import com.marcpascualsanchez.url.shortener.BaseApplication
import com.marcpascualsanchez.url.shortener.api.response.ShortenedURLCreationResponse
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
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType

@CucumberContextConfiguration
class CommonSteps : BaseApplication() {

    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, String>

    @LocalServerPort
    private var port = 0
    private val testRestTemplate = TestRestTemplate()
    val baseUrl: String
        get() = "http://localhost:$port"
    private var currentResponse = ""

    @Given("there is no shortened URLs saved for {string}")
    fun `there is no shortened URLs saved for`(originalURL: String) {
        redisTemplate.delete(originalURL)
    }

    @When("requested to short the URL {string}")
    fun `requested to short the URL`(originalURL: String) {
        currentResponse = baseCall(
            HttpEntity("{\"url\":\"$originalURL\"}", getDefaultHeaders()),
            "$baseUrl/api/v1/url-shortener/create",
            HttpMethod.POST
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
        assertThat(currentResponse).isEqualTo("{\"shortenedURL\":\"$expectedShortURL\"}")
    }

    private fun baseCall(
        entity: HttpEntity<*>?,
        path: String,
        httpMethod: HttpMethod
    ): String {
        val response = testRestTemplate.exchange(path, httpMethod, entity, String::class.java)
        return response.body!!
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