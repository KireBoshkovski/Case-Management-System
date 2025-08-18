package com.sorsix.backend.dto

import com.sorsix.backend.domain.discussions.Discussion

data class DiscussionDetails(
    val title: String,
    val description: String,
    val user: String
)

fun Discussion.details() = DiscussionDetails(
    title = this.title,
    description = this.description,
    user = this.user.email
)