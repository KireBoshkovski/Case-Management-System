package com.sorsix.backend.service

import com.sorsix.backend.domain.MedicalThread
import com.sorsix.backend.domain.enums.ThreadStatus
import com.sorsix.backend.dto.CreateThreadRequest
import com.sorsix.backend.exceptions.MedicalThreadNotFoundException
import com.sorsix.backend.repository.MedicalThreadRepository
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MedicalThreadService(
    private val threadRepository: MedicalThreadRepository,
    private val caseService: CaseService,
    private val doctorService: DoctorService,
    private val anonymizationService: AnonymizationService
) {
    fun getActive(pageable: Pageable): Page<MedicalThread> =
        threadRepository.findByStatus(ThreadStatus.ACTIVE, pageable)

    fun getForksOf(threadId: Long, pageable: Pageable): Page<MedicalThread> =
        threadRepository.findByParentThreadId(threadId, pageable)

    fun findById(id: Long): MedicalThread =
        threadRepository.findById(id).orElseThrow { MedicalThreadNotFoundException(id) }

    @Transactional
    fun createThread(req: CreateThreadRequest): MedicalThread {
        val case = caseService.findById(req.caseId)
        val creator = doctorService.findById(req.creatingDoctorId)
        val thread = MedicalThread(
            title = req.title,
            anonymizedSymptoms = anonymizationService.anonymizeText(case.description.toString()),
            anonymizedPatientInfo = anonymizationService.anonymizeText(case.patient.toString()),
            status = ThreadStatus.ACTIVE,
            isEducational = false,
            originalCase = case,
            creatingDoctor = creator
        )
        return threadRepository.save(thread)
    }

    @Transactional
    fun forkThread(originalThreadId: Long, forkingDoctorId: Long): MedicalThread {
        val original = findById(originalThreadId)
        val doctor = doctorService.findById(forkingDoctorId)
        val fork = original.copy(
            id = 0,
            isEducational = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            creatingDoctor = doctor,
            parentThread = original
        )
        return threadRepository.save(fork)
    }

    @Transactional
    fun closeThread(threadId: Long): MedicalThread {
        val t = findById(threadId)
        val updated = t.copy(status = ThreadStatus.CLOSED, updatedAt = LocalDateTime.now())
        return threadRepository.save(updated)
    }
}
