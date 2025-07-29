package com.sorsix.backend.web

import com.sorsix.backend.domain.Fork
import com.sorsix.backend.service.ForkService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/forks")
class ForkController(
    val forkService: ForkService
) {
    @GetMapping
    fun getAllForks() = ResponseEntity.ok(forkService.getAll())

    @GetMapping("/{id}")
    fun getForkById(@PathVariable id: Long) = ResponseEntity.ok(forkService.getById(id))

    @DeleteMapping("/{id}")
    fun deleteForkById(@PathVariable id: Long) = ResponseEntity.ok(forkService.deleteById(id))

    @PostMapping("/new")
    fun createFork(@RequestParam title: String, @RequestParam description: String, @RequestParam caseId: Long) =
        ResponseEntity.ok(
            forkService.createFork(title = title, description = description, caseId = caseId)
        )

    @PostMapping()
    fun updateFork(fork: Fork) = ResponseEntity.ok(forkService.update(fork))
}