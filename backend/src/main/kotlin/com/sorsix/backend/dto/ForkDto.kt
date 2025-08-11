package com.sorsix.backend.dto

import com.sorsix.backend.domain.Fork

data class ForkDto(
    /*
    * For creating fork request:
    * id should be null,
    * editorId should also be null since it will be pulled from AuthenticationManager
    *
    * For update of a fork request:
    * id should be required
    *
    * For fork details, id and originId shouldn't be null
    * */
    val id: Long?,

    val title: String,

    val description: String?,

    val alternativeDiagnosis: String?,

    val alternativeTreatment: String?,

    val analysisNotes: String?,

    val recommendations: String?,

    val originId: Long,

    val editorId: Long?
)

data class ForkListItem(
    val id: Long?,
    val title: String,
    val originId: Long,
    val editorId: Long,
)

fun Fork.toForkDto() = ForkDto(
    id = this.id,
    title = this.title,
    description = this.description,
    alternativeDiagnosis = this.alternativeDiagnosis,
    alternativeTreatment = this.alternativeTreatment,
    analysisNotes = this.analysisNotes,
    recommendations = this.recommendations,
    originId = this.origin.id!!,
    editorId = this.editor.id
)

fun Fork.toListItem() = ForkListItem(
    id = this.id,
    title = this.title,
    originId = this.origin.id!!,
    editorId = this.editor.id,
)

