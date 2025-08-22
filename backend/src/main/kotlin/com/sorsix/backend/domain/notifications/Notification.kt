package com.sorsix.backend.domain.notifications

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notifications")
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    val type: NotificationType,

    @Column(nullable = false)
    val message: String,

    @Column(nullable = false)
    val discussionId: Long,

    val commentId: Long? = null,

    @Column(nullable = false)
    val fromUserId: Long,

    @Column(nullable = false)
    val timestamp: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var read: Boolean = false
)
