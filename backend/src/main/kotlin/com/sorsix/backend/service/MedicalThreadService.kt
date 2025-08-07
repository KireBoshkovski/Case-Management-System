package com.sorsix.backend.service

import com.sorsix.backend.domain.MedicalThread
import com.sorsix.backend.domain.enums.ThreadStatus
import com.sorsix.backend.exceptions.MedicalThreadNotFoundException
import com.sorsix.backend.repository.MedicalThreadRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MedicalThreadService(
    private val threadRepository: MedicalThreadRepository,
    private val caseService: CaseService,
    private val doctorService: DoctorService,
    private val anonymizationService: AnonymizationService,

    ) {
    fun findAll(): List<MedicalThread> =
        threadRepository.findAll()

    fun findById(id: Long): MedicalThread =
        threadRepository.findById(id).orElseThrow { MedicalThreadNotFoundException(id) }

    fun findActiveThreads(): List<MedicalThread> =
        threadRepository.findByStatus(ThreadStatus.ACTIVE)

    fun save(thread: MedicalThread) =
        threadRepository.save(thread)

    fun deleteById(id: Long) =
        threadRepository.deleteById(id)

    @Transactional
    fun createThreadFromCase(
        caseId: Long,
        creatingDoctorId: Long,
        title: String
    ): MedicalThread {
        val case = caseService.findById(caseId)
        val creatingDoctor = doctorService.findById(creatingDoctorId)
        val thread = MedicalThread(
            title = title,
            anonymizedSymptoms = anonymizationService.anonymizeText(case.description.toString()),//TODO
            anonymizedPatientInfo = anonymizationService.anonymizeText(case.patient.toString()),//TODO
            originalCase = case,
            creatingDoctor = creatingDoctor,
        )
        return threadRepository.save(thread)
    }

    fun forkThread(
        originalThreadId: Long,
        forkingDoctorId: Long,
    ): MedicalThread {
        val originalThread = findById(originalThreadId)

        val forkedThread = MedicalThread(
            title = originalThread.title,
            anonymizedSymptoms = originalThread.anonymizedSymptoms,
            anonymizedPatientInfo = originalThread.anonymizedPatientInfo,
            status = ThreadStatus.ACTIVE,
            isEducational = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            originalCase = originalThread.originalCase,
            creatingDoctor = doctorService.findById(forkingDoctorId),
            parentThread = originalThread,
        )


        return threadRepository.save(forkedThread)
    }
}