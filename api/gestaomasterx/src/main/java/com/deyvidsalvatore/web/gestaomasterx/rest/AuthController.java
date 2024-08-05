package com.deyvidsalvatore.web.gestaomasterx.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Login endpoint"));
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Logout endpoint"));
    }
}
