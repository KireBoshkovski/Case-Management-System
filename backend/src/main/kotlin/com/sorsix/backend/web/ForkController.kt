package com.sorsix.backend.web

import com.sorsix.backend.dto.ForkDto
import com.sorsix.backend.dto.toForkDto
import com.sorsix.backend.dto.toListItem
import com.sorsix.backend.security.CustomUserDetails
import com.sorsix.backend.service.ForkService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/forks")
class ForkController(
    val forkService: ForkService
) {
    @GetMapping
    fun getAllForks() = forkService.findAll().map { it.toListItem() }

    @GetMapping("/{id}")
    fun getForkById(@PathVariable id: Long) = ResponseEntity.ok(forkService.findById(id).map { it.toForkDto() })

    @PostMapping
    fun createFork(@RequestBody forkData: ForkDto, @AuthenticationPrincipal userDetails: CustomUserDetails) =
        ResponseEntity.ok(forkService.createFork(forkData, userDetails).toForkDto())

    @PutMapping("/{id}")
    fun updateFork(@PathVariable id: Long, fork: ForkDto) = ResponseEntity.ok(forkService.update(id, fork).toForkDto())

    @DeleteMapping("/{id}")
    fun deleteForkById(@PathVariable id: Long) = ResponseEntity.ok(forkService.deleteById(id))

    @GetMapping("/origin/{id}")
    fun listForksFromSpecificCase(@PathVariable id: Long) = forkService.findAllByOriginId(id).map { it.toListItem() }
}