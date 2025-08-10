package com.sorsix.backend.service

import com.sorsix.backend.domain.enums.UserRole
import com.sorsix.backend.domain.users.Doctor
import com.sorsix.backend.domain.users.Patient
import com.sorsix.backend.dto.security.JwtResponse
import com.sorsix.backend.dto.security.LoginRequest
import com.sorsix.backend.dto.security.SignUpRequest
import com.sorsix.backend.repository.UserRepository
import com.sorsix.backend.security.CustomUserDetails
import com.sorsix.backend.security.JWTUtility
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
    fun signIn(loginRequest: LoginRequest): JwtResponse? {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
        )

        val userDetails = authentication.principal as CustomUserDetails
        val jwt = jwtUtility.generateJwtToken(userDetails)

        return JwtResponse(
            accessToken = jwt,
            id = userDetails.getId(),
            role = userDetails.getRole()
        )
    }

    fun signUp(signUpRequest: SignUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.email)) {
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

            else -> throw IllegalArgumentException("Invalid role for registration")
        }

        userRepository.save(user)
    }
}