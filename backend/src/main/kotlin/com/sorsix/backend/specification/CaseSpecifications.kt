package com.sorsix.backend.specification

import com.sorsix.backend.domain.Case
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.JoinType

object CaseSpecifications {

    fun hasPatientId(patientId: Long?): Specification<Case>? =
        patientId?.let { id ->
            Specification { root, _, cb ->
                // either via join:
                val patient = root.join<Any, Any>("patient", JoinType.INNER)
                cb.equal(patient.get<Long>("id"), id)
                // or without join (works too):
                // cb.equal(root.get<Any>("patient").get<Long>("id"), id)
            }
        }

    fun isPublic(publicVal: Boolean?): Specification<Case>? =
        publicVal?.let { isPublic ->
            Specification { root, _, cb ->
                val path = root.get<Any>("publishedCase")
                if (isPublic) cb.isNotNull(path) else cb.isNull(path)
            }
        }

    fun searchInFields(searchTerm: String?): Specification<Case>? =
        searchTerm?.takeIf { it.isNotBlank() }?.let { term ->
            Specification { root, _, cb ->
                val p = "%${term.lowercase()}%"
                cb.or(
                    cb.like(cb.lower(root.get("description")), p),
                    cb.like(cb.lower(root.get("allergies")), p),
                    cb.like(cb.lower(root.get("bloodType")), p)
                )
            }
        }

    fun hasId(id: Long?): Specification<Case>? =
        id?.let { caseId ->
            Specification { root, _, cb -> cb.equal(root.get<Long>("id"), caseId) }
        }

    fun searchByTermOrId(searchTerm: String?): Specification<Case>? =
        searchTerm?.takeIf { it.isNotBlank() }?.let { term ->
            term.toLongOrNull()?.let { numericId ->
                Specification { root, _, cb ->
                    val p = "%${term.lowercase()}%"
                    cb.or(
                        cb.equal(root.get<Long>("id"), numericId),
                        cb.like(cb.lower(root.get("description")), p),
                        cb.like(cb.lower(root.get("allergies")), p),
                        cb.like(cb.lower(root.get("bloodType")), p)
                    )
                }
            } ?: searchInFields(term)
        }
}
