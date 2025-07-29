package com.sorsix.backend.service

import com.sorsix.backend.domain.Fork
import com.sorsix.backend.repository.ForkRepository
import org.springframework.stereotype.Service

@Service
class ForkService(
    val forkRepoistory: ForkRepository
) {
    fun getAll() = forkRepoistory.findAll()
    fun getById(id: Long) = forkRepoistory.findById(id)
    fun save(fork: Fork) = forkRepoistory.save(fork)
    fun deleteById(id: Long) = forkRepoistory.deleteById(id)
}