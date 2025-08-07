package com.sorsix.backend.security

import com.sorsix.backend.domain.enums.UserRole
import com.sorsix.backend.domain.users.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val user: User) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority(user.role.authority))

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.email

    override fun isAccountNonExpired(): Boolean = user.isAccountNonExpired

    override fun isAccountNonLocked(): Boolean = user.isAccountNonLocked

    override fun isCredentialsNonExpired(): Boolean = user.isCredentialsNonExpired

    override fun isEnabled(): Boolean = user.isEnabled

    fun getRole(): UserRole = user.role

    fun getId(): Long = user.id

    fun getFirstName(): String = user.firstName

    fun getLastName(): String = user.lastName
}