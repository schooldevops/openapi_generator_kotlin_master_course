package com.schooldevops.kotlin.basicrest.controller

import com.schooldevops.kotlin.basicrest.api.GreetApi
import com.schooldevops.kotlin.basicrest.model.GreetGET200Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class GeetingController: GreetApi {
    override fun greetGET(): ResponseEntity<GreetGET200Response> {

        return ResponseEntity.ok(GreetGET200Response().message("Hello Greeting"))
    }
}