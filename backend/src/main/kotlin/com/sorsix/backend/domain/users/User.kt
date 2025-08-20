package com.sorsix.backend.domain.users

import com.sorsix.backend.domain.enums.UserRole
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
abstract class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "email", nullable = false, unique = true, length = 100)
    val email: String,

    @Column(name = "password", nullable = false, length = 255)
    val password: String,

    @Column(name = "first_name", nullable = false, length = 50)
    val firstName: String,

    @Column(name = "last_name", nullable = false, length = 50)
    val lastName: String,

    @Column(name = "phone_number", length = 20)
    val phoneNumber: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole,

    @Column(name = "is_enabled", nullable = false)
    open val isEnabled: Boolean = true,

    @Column(name = "is_account_non_expired", nullable = false)
    open val isAccountNonExpired: Boolean = true,

    @Column(name = "is_account_non_locked", nullable = false)
    open val isAccountNonLocked: Boolean = true,

    @Column(name = "is_credentials_non_expired", nullable = false)
    open val isCredentialsNonExpired: Boolean = true,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    open val createdAt: LocalDateTime = LocalDateTime.now(),
)