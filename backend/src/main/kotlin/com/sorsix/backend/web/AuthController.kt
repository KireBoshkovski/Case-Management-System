package com.sorsix.backend.web

import com.sorsix.backend.dto.security.JwtResponse
import com.sorsix.backend.dto.security.LoginRequest
import com.sorsix.backend.dto.security.SignUpRequest
import com.sorsix.backend.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
@Validated
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<JwtResponse> =
        ResponseEntity.ok(authService.signIn(loginRequest))

    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signUpRequest: SignUpRequest): ResponseEntity<String> {
        authService.signUp(signUpRequest)
        return ResponseEntity.ok("User registered successfully!")
    }
}