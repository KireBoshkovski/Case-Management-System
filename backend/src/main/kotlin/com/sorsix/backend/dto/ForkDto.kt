package com.sorsix.backend.dto

import com.sorsix.backend.domain.Fork

data class ForkDto (
    val id: Long,
    val title: String,
)

fun Fork.toForkDto() = this.id?.let {
    ForkDto(
        id = it,
        title = this.title
    )
}