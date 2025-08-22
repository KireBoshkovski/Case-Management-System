package com.sorsix.backend.domain.discussions

import com.sorsix.backend.domain.cases.PublicCase
import com.sorsix.backend.domain.users.User
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "discussions")
data class Discussion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discussion_id")
    val id: Long?,

    @Column(nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var description: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id", nullable = false)
    val publicCase: PublicCase,

    @OneToMany(
        mappedBy = "discussion",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val comments: MutableList<Comment> = mutableListOf()
)
