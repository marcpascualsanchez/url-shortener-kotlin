package com.marcpascualsanchez.url.shortener

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    stepNotifications = true,
    features = ["src/test/resources/features"],
    glue = ["com.marcpascualsanchez.kotlin.spring.boilerplate.steps"],
    plugin = ["pretty"],
    tags = "not @ignored",
)
class CucumberRunner
