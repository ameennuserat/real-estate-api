package com.graduation.realestateconsulting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/cors")
    public ResponseEntity<String> corsTest() {
        System.out.println("cors test");
        return ResponseEntity.ok("CORS is OK");
    }
}