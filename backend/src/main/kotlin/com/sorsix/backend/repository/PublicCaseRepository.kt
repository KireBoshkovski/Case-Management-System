package com.sorsix.backend.repository

import com.sorsix.backend.domain.cases.PublicCase
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PublicCaseRepository : JpaRepository<PublicCase, Long>, JpaSpecificationExecutor<PublicCase> {
    @Modifying
    @Query("UPDATE PublicCase p SET p.viewsCount = p.viewsCount+1 WHERE p.id = :id")
    fun incrementViewsCount(@Param("id") id: Long)

    @Query("SELECT p FROM PublicCase p ORDER BY p.viewsCount DESC limit 5")
    fun findAllOrderByViewsDesc(): List<PublicCase>
}