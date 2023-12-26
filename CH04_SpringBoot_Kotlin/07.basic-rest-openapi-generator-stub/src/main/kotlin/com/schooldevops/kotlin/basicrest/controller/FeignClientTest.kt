package com.schooldevops.kotlin.basicrest.controller

import com.schooldevops.kotlin.basicrest.feign.DefaultApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FeignClientTest(@Autowired val defaultApi: DefaultApi) {

    @GetMapping("/test/greeting")
    fun greeting(): ResponseEntity<*> {
        return ResponseEntity.ok(defaultApi.greetGET())
    }

}