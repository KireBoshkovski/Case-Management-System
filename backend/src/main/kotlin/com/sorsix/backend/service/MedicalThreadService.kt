package com.sorsix.backend.service

import com.sorsix.backend.domain.MedicalThread
import com.sorsix.backend.domain.enums.ThreadStatus
import com.sorsix.backend.repository.MedicalThreadRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MedicalThreadService(
    private val threadRepository: MedicalThreadRepository,
    private val caseService: CaseService,
    private val doctorService: DoctorService

) {
    fun findAll() = threadRepository.findAll()
    fun findById(id: Long) = threadRepository.findById(id).orElseThrow(
        { IllegalArgumentException("Thread with id $id not found") }//TODO
    )
    fun save(thread: MedicalThread) = threadRepository.save(thread)
    fun deleteById(id: Long) = threadRepository.deleteById(id)

    fun createThreadFromCase(
        caseId: Long,
        creatingDoctorId: Long,
        title: String
    ): MedicalThread {
        val case = caseService.findById(caseId)
        val creatingDoctor = doctorService.findById(creatingDoctorId)
        val thread = MedicalThread(
            title = title,
            anonymizedSymptoms = TODO(),
            anonymizedPatientInfo = TODO(),
            originalCase = case,
            creatingDoctor = case.doctor,
        )
        return threadRepository.save(thread)
    }

    fun forkThread(
        originalThreadId: Long,
        forkingDoctorId: Long,
        forkingHospitalId: Long
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

    fun findActiveThreads(): List<MedicalThread> {
        return threadRepository.findByStatus(ThreadStatus.ACTIVE)
    }
}