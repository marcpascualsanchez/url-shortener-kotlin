package com.marcpascualsanchez.url.shortener.api.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ShortenedURLCreationRequest(
    @JsonProperty("url")
    val URL: String,
)
