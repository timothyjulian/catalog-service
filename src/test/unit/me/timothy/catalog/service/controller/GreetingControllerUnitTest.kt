package me.timothy.catalog.service.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import me.timothy.catalog.service.contoller.GreetingController
import me.timothy.catalog.service.service.GreetingService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [GreetingController::class])
@AutoConfigureWebTestClient
class GreetingControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var greetingServiceMock: GreetingService

    @Test
    fun retrieveGreeting() {
        val name = "Timo"

        every { greetingServiceMock.retrieveGreeting(any()) } returns "$name, Hello from default profile MOCK"

        val returnResult =
            webTestClient.get().uri("/v1/greetings/{name}", name).exchange().expectStatus().is2xxSuccessful.expectBody(
                String::class.java
            ).returnResult()

        Assertions.assertEquals("$name, Hello from default profile MOCK", returnResult.responseBody)
    }
}