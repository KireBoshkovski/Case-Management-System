package com.sorsix.backend.specification

import com.sorsix.backend.domain.cases.Case
import com.sorsix.backend.domain.cases.PublicCase
import com.sorsix.backend.domain.cases.PublicExamination
import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification

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

    fun hasDoctorId(doctorId: Long?): Specification<Case>? =
        doctorId?.let { id ->
            Specification { root, _, cb ->
                val doctor = root.join<Any, Any>("doctor", JoinType.INNER)
                cb.equal(doctor.get<Long>("id"), id)
            }
        }

//    fun isPublic(publicVal: Boolean?): Specification<Case>? =
//        publicVal?.let { isPublic ->
//            Specification { root, _, cb ->
//                val path = root.get<Any>("publishedCase")
//                if (isPublic) cb.isNotNull(path) else cb.isNull(path)
//            }
//        }

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

    fun publicCaseSearch(searchTerm: String?): Specification<PublicCase>? =
        searchTerm?.takeIf { it.isNotBlank() }?.let { term ->
            Specification { root, _, cb ->
                val p = "%${term.lowercase()}%"

                val examinations = root.join<PublicCase, PublicExamination>("examinations", JoinType.LEFT)

                cb.or(
                    cb.like(cb.lower(root.get("description")), p),
                    cb.like(cb.lower(root.get("allergies")), p),
                    cb.like(cb.lower(root.get("bloodType")), p),
                    cb.like(cb.lower(root.get("treatmentPlan")), p),
                    cb.like(cb.lower(root.get("patientAgeRange")), p),
                    cb.like(cb.lower(root.get("patientGender")), p),

                    cb.like(cb.lower(examinations.get("examinationType")), p),
                    cb.like(cb.lower(examinations.get("findings")), p),
                    cb.like(cb.lower(examinations.get("results")), p),
                    cb.like(cb.lower(examinations.get("notes")), p),
                    cb.like(cb.lower(examinations.get("vitalSigns")), p)
                )
            }
        }
}