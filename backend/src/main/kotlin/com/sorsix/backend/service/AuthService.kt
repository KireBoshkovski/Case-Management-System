package com.sorsix.backend.service

import com.sorsix.backend.config.security.CustomUserDetails
import com.sorsix.backend.config.security.JWTUtility
import com.sorsix.backend.domain.enums.UserRole
import com.sorsix.backend.domain.users.Doctor
import com.sorsix.backend.domain.users.Patient
import com.sorsix.backend.dto.security.JwtResponse
import com.sorsix.backend.dto.security.LoginRequest
import com.sorsix.backend.dto.security.SignUpRequest
import com.sorsix.backend.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    val authenticationManager: AuthenticationManager,
    val jwtUtility: JWTUtility,
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun signIn(loginRequest: LoginRequest): JwtResponse? {
        logger.info("Attempting sign in for email: [{}]", loginRequest.email)

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        )

        val userDetails = authentication.principal as CustomUserDetails
        val jwt = jwtUtility.generateJwtToken(userDetails)

        logger.info("Successful sign in for user ID: [{}], role: [{}]", userDetails.getId(), userDetails.getRole())

        return JwtResponse(
            accessToken = jwt,
            id = userDetails.getId(),
            role = userDetails.getRole()
        )
    }

    fun signUp(signUpRequest: SignUpRequest) {
        logger.info("Attempting signup for email: [{}], role: [{}]", signUpRequest.email, signUpRequest.role)

        if (userRepository.existsByEmail(signUpRequest.email)) {
            logger.warn("Sign up failed - email already in use: [{}]", signUpRequest.email)
            throw IllegalArgumentException("Email is already in use!")
        }

        val user = when (signUpRequest.role) {
            UserRole.PATIENT -> Patient(
                email = signUpRequest.email,
                password = passwordEncoder.encode(signUpRequest.password),
                firstName = signUpRequest.firstName,
                lastName = signUpRequest.lastName,
                phoneNumber = signUpRequest.phoneNumber,
                dateOfBirth = signUpRequest.dateOfBirth!!,
                gender = signUpRequest.gender,
                address = signUpRequest.address
            )

            UserRole.DOCTOR -> Doctor(
                email = signUpRequest.email,
                password = passwordEncoder.encode(signUpRequest.password),
                firstName = signUpRequest.firstName,
                lastName = signUpRequest.lastName,
                phoneNumber = signUpRequest.phoneNumber,
                specialization = signUpRequest.specialization!!,
                department = signUpRequest.department!!
            )

            else -> {
                logger.error("Invalid role for registration: [{}]", signUpRequest.role)
                throw IllegalArgumentException("Invalid role for registration")
            }
        }

        userRepository.save(user)
        logger.info("Successfully registered user: [{}], role: [{}]", user.email, user.role)
    }
}