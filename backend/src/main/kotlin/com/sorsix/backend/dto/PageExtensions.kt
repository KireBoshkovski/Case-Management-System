package com.sorsix.backend.dto

import org.springframework.data.domain.Page

fun <T> Page<T>.toPageResponse(): PageResponse<T> =
    PageResponse(
        content = content,
        page = number,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
        hasNext = hasNext(),
        hasPrevious = hasPrevious()
    )
