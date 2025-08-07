package com.sorsix.backend.domain.enums

enum class UserRole(val authority: String) {
    PATIENT("ROLE_PATIENT"),
    DOCTOR("ROLE_DOCTOR"),
    ADMIN("ROLE_ADMIN")
}