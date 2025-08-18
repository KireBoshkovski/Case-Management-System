package com.sorsix.backend.service.specification

import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification

object FieldFilterSpecification {
    fun <T, V : Comparable<V>> greaterThan(field: String, value: V?): Specification<T>? =
        if (value == null) null
        else Specification { root, _, criteriaBuilder ->
            val path = fieldToPath<V>(field, root as Root<*>)
            criteriaBuilder.greaterThan(path, value)
        }


    fun <T, V : Comparable<V>> lessThan(field: String, value: V?): Specification<T>? =
        if (value == null) null
        else Specification { root, _, criteriaBuilder ->
            val path = fieldToPath<V>(field, root as Root<*>)
            criteriaBuilder.lessThan(path, value)
        }

    fun <T, V> filterEqualsT(field: String, value: V?): Specification<T>? =
        if (value == null) null
        else Specification { root, _, criteriaBuilder ->
            val path = fieldToPath<V>(field, root as Root<*>)
            criteriaBuilder.equal(path, value)
        }

    fun <T> filterContainsText(fields: List<String>, value: String?): Specification<T>? =
        if (value.isNullOrEmpty()) null
        else Specification { root, _, criteriaBuilder ->
            val predicates = fields.map { field ->
                criteriaBuilder.like(
                    criteriaBuilder.lower(fieldToPath<String>(field, root)),
                    "%${value.lowercase()}%"
                )
            }
            criteriaBuilder.or(*predicates.toTypedArray())
        }

    private fun <V> fieldToPath(field: String, root: Root<*>): Path<V> {
        val parts = field.split(".")
        var path: Path<*> = root
        for (part in parts) {
            path = path.get<Any>(part)
        }
        @Suppress("UNCHECKED_CAST")
        return path as Path<V>
    }
}