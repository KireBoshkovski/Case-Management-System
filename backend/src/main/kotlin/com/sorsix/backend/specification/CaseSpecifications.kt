package com.sorsix.backend.specification

import com.sorsix.backend.domain.Case
import org.springframework.data.jpa.domain.Specification

object CaseSpecifications {

    fun hasPatientId(patientId: Long?): Specification<Case>? {
        return patientId?.let { id ->
            Specification { root, _, cb ->
                cb.equal(root.get<Any>("patient").get<Long>("id"), id)
            }
        }
    }

    fun isPublic(publicVal: Boolean?): Specification<Case>? {
        return publicVal?.let { isPublic ->
            Specification { root, _, cb ->
                cb.equal(root.get<Boolean>("public"), isPublic)
            }
        }
    }

    fun searchInFields(searchTerm: String?): Specification<Case>? {
        return searchTerm?.takeIf { it.isNotBlank() }?.let { term ->
            Specification { root, _, cb ->
                val searchPattern = "%${term.lowercase()}%"
                cb.or(
                    cb.like(cb.lower(cb.treat(root.get<Any>("description"), String::class.java)), searchPattern),
                    cb.like(cb.lower(cb.treat(root.get<Any>("allergies"), String::class.java)), searchPattern),
                    cb.like(cb.lower(cb.treat(root.get<Any>("bloodType"), String::class.java)), searchPattern)
                )
            }
        }
    }

    fun hasId(id: Long?): Specification<Case>? {
        return id?.let { caseId ->
            Specification { root, _, cb ->
                cb.equal(root.get<Long>("id"), caseId)
            }
        }
    }

    fun searchByTermOrId(searchTerm: String?): Specification<Case>? {
        return searchTerm?.takeIf { it.isNotBlank() }?.let { term ->
            val numericId = term.toLongOrNull()
            if (numericId != null) {
                // If term is numeric, search by ID OR in text fields
                Specification { root, _, cb ->
                    val searchPattern = "%${term.lowercase()}%"
                    cb.or(
                        cb.equal(root.get<Long>("id"), numericId),
                        cb.like(cb.lower(cb.treat(root.get<Any>("description"), String::class.java)), searchPattern),
                        cb.like(cb.lower(cb.treat(root.get<Any>("allergies"), String::class.java)), searchPattern),
                        cb.like(cb.lower(cb.treat(root.get<Any>("bloodType"), String::class.java)), searchPattern)
                    )
                }
            } else {
                // Only search in text fields
                searchInFields(term)
            }
        }
    }
}