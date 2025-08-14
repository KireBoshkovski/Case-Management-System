package com.sorsix.backend.config

import com.sorsix.backend.domain.*
import com.sorsix.backend.domain.enums.CaseStatus
import com.sorsix.backend.domain.enums.ThreadStatus
import com.sorsix.backend.domain.users.Doctor
import com.sorsix.backend.domain.users.Patient
import com.sorsix.backend.repository.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.LocalDateTime

@Configuration
class DataInitializer {

    @Bean
    fun seedData(
        // users
        doctorRepository: DoctorRepository,
        patientRepository: PatientRepository,

        // medical data
        caseRepository: CaseRepository,
        examinationRepository: ExaminationRepository,

        // public data
        publicCaseRepository: PublicCaseRepository,
        publicExaminationRepository: PublicExaminationRepository,

        // threads
        medicalThreadRepository: MedicalThreadRepository,
        threadAttachmentRepository: ThreadAttachmentRepository,
        threadDiscussionRepository: ThreadDiscussionRepository,

        // forks
        forkRepository: ForkRepository
    ) = CommandLineRunner {
        // idempotency guard
        if (doctorRepository.count() > 0L) return@CommandLineRunner

        val now = LocalDateTime.now()

        // --- Doctors ---
        val d1 = doctorRepository.save(
            Doctor(
                email = "house.md@hospital.test",
                password = "{noop}password1",
                firstName = "Gregory",
                lastName = "House",
                phoneNumber = "070111222",
                specialization = "Diagnostics",
                department = "Internal Medicine"
            )
        )
        val d2 = doctorRepository.save(
            Doctor(
                email = "meredith.grey@hospital.test",
                password = "{noop}password2",
                firstName = "Meredith",
                lastName = "Grey",
                phoneNumber = "070333444",
                specialization = "General Surgery",
                department = "Surgery"
            )
        )
        val d3 = doctorRepository.save(
            Doctor(
                email = "john.carter@hospital.test",
                password = "{noop}password3",
                firstName = "John",
                lastName = "Carter",
                phoneNumber = "070555666",
                specialization = "Emergency Medicine",
                department = "ER"
            )
        )

        // --- Patients ---
        val p1 = patientRepository.save(
            Patient(
                email = "alice@patient.test",
                password = "{noop}alice123",
                firstName = "Alice",
                lastName = "Doe",
                phoneNumber = "071111111",
                dateOfBirth = LocalDate.of(1992, 5, 14),
                gender = "F",
                address = "Main St 1, City"
            )
        )
        val p2 = patientRepository.save(
            Patient(
                email = "bob@patient.test",
                password = "{noop}bob123",
                firstName = "Bob",
                lastName = "Smith",
                phoneNumber = "072222222",
                dateOfBirth = LocalDate.of(1984, 11, 3),
                gender = "M",
                address = "Second St 2, City"
            )
        )
        val p3 = patientRepository.save(
            Patient(
                email = "carol@patient.test",
                password = "{noop}carol123",
                firstName = "Carol",
                lastName = "Johnson",
                phoneNumber = "073333333",
                dateOfBirth = LocalDate.of(2001, 2, 21),
                gender = "F",
                address = "Third St 3, City"
            )
        )

        // --- Cases (private/internal) ---
        val c1 = caseRepository.save(
            Case(
                id = null,
                bloodType = "A+",
                allergies = "Penicillin",
                description = "Intermittent chest pain and shortness of breath.",
                treatmentPlan = "ECG, troponin, cardiology consult.",
                status = CaseStatus.ACTIVE,
                createdAt = now.minusDays(7),
                updatedAt = now.minusDays(6),
                patient = p1,
                doctor = d1
            )
        )
        val c2 = caseRepository.save(
            Case(
                id = null,
                bloodType = "O-",
                allergies = null,
                description = "Abdominal pain with mild fever.",
                treatmentPlan = "Ultrasound, bloodwork, observation.",
                status = CaseStatus.ACTIVE,
                createdAt = now.minusDays(5),
                updatedAt = now.minusDays(4),
                patient = p2,
                doctor = d2
            )
        )
        val c3 = caseRepository.save(
            Case(
                id = null,
                bloodType = "B+",
                allergies = "Nuts",
                description = "Left arm fracture post fall.",
                treatmentPlan = "X-ray, reduction, immobilization.",
                status = CaseStatus.CLOSED,
                createdAt = now.minusDays(30),
                updatedAt = now.minusDays(28),
                patient = p3,
                doctor = d3
            )
        )

        // --- Examinations (link to Case via Case.examinations collection) ---
        val e1 = examinationRepository.save(
            Examination(
                id = null,
                examinationType = "ECG",
                findings = "Sinus tachycardia",
                results = "HR 110 bpm",
                notes = "No ST elevation",
                vitalSigns = """{"bp":"130/85","hr":110}""",
                examinationDate = now.minusDays(6),
                doctor = d1
            )
        )
        val e2 = examinationRepository.save(
            Examination(
                id = null,
                examinationType = "Troponin",
                findings = "Negative",
                results = "Within normal limits",
                notes = "Repeat in 6 hours",
                vitalSigns = null,
                examinationDate = now.minusDays(6).plusHours(1),
                doctor = d1
            )
        )
        val e3 = examinationRepository.save(
            Examination(
                id = null,
                examinationType = "Abdominal Ultrasound",
                findings = "Mild hepatic steatosis",
                results = "No acute findings",
                notes = null,
                vitalSigns = """{"temp":"37.8"}""",
                examinationDate = now.minusDays(4),
                doctor = d2
            )
        )

        // Attach examinations to cases (unidirectional @OneToMany with @JoinColumn in Case)
        c1.examinations.addAll(listOf(e1, e2))
        c2.examinations.add(e3)
        caseRepository.saveAll(listOf(c1, c2))

        // --- PublicCase + PublicExaminations (anonymized/published data) ---
        val pe1 = PublicExamination(
            id = null,
            originalExaminationId = e1.id!!,
            examinationType = e1.examinationType,
            findings = e1.findings,
            results = e1.results,
            notes = e1.notes,
            vitalSigns = e1.vitalSigns,
            examinationDate = e1.examinationDate,
            examiningDoctorSpecialty = d1.specialization,
            publishedAt = now.minusDays(6)
        )
        val pe2 = PublicExamination(
            id = null,
            originalExaminationId = e3.id!!,
            examinationType = e3.examinationType,
            findings = e3.findings,
            results = e3.results,
            notes = e3.notes,
            vitalSigns = e3.vitalSigns,
            examinationDate = e3.examinationDate,
            examiningDoctorSpecialty = d2.specialization,
            publishedAt = now.minusDays(4)
        )
        publicExaminationRepository.saveAll(listOf(pe1, pe2))

        val pub1 = publicCaseRepository.save(
            PublicCase(
                id = null,
                bloodType = c1.bloodType,
                allergies = c1.allergies,
                description = "Chest pain case (anonymized).",
                treatmentPlan = "Cardiology consult, monitoring.",
                patientAgeRange = "30-40",
                patientGender = "F",
                createdAt = now.minusDays(6),
                updatedAt = now.minusDays(5),
                examinations = mutableListOf(pe1)
            )
        )
        val pub2 = publicCaseRepository.save(
            PublicCase(
                id = null,
                bloodType = c2.bloodType,
                allergies = c2.allergies,
                description = "Abdominal pain (anonymized).",
                treatmentPlan = "Observation and supportive tx.",
                patientAgeRange = "35-45",
                patientGender = "M",
                createdAt = now.minusDays(4),
                updatedAt = now.minusDays(3),
                examinations = mutableListOf(pe2)
            )
        )

        // --- Medical Threads ---
        val t1 = medicalThreadRepository.save(
            MedicalThread(
                title = "Chest pain differential",
                anonymizedSymptoms = "Substernal chest pain, SOB, onset with exertion.",
                anonymizedPatientInfo = "Female, 33y, non-smoker",
                status = ThreadStatus.ACTIVE,
                isEducational = false,
                originalCase = c1,
                creatingDoctor = d1
            )
        )
        val t2 = medicalThreadRepository.save(
            MedicalThread(
                title = "Abdominal pain workup",
                anonymizedSymptoms = "Diffuse abdominal pain, low-grade fever.",
                anonymizedPatientInfo = "Male, 40y",
                status = ThreadStatus.ACTIVE,
                isEducational = true,
                originalCase = c2,
                creatingDoctor = d2,
                parentThread = null
            )
        )

        // --- Thread Attachments ---
        val att1 = threadAttachmentRepository.save(
            ThreadAttachment(
                thread = t1,
                fileName = "ecg_initial.png",
                contentType = "image/png",
                url = "https://example.test/files/ecg_initial.png"
            )
        )
        val att2 = threadAttachmentRepository.save(
            ThreadAttachment(
                thread = t2,
                fileName = "ultrasound_report.pdf",
                contentType = "application/pdf",
                url = "https://example.test/files/ultrasound_report.pdf"
            )
        )

        // --- Thread Discussions ---
        val td1 = threadDiscussionRepository.save(
            ThreadDiscussion(
                content = "Consider non-STEMI; repeat troponin and ECG.",
                diagnosisSuggestion = "NSTEMI",
                confidenceLevel = 6,
                thread = t1,
                doctor = d3
            )
        )
        val td2 = threadDiscussionRepository.save(
            ThreadDiscussion(
                content = "Could be biliary colic vs viral gastroenteritis.",
                diagnosisSuggestion = "Biliary colic",
                confidenceLevel = 5,
                thread = t2,
                doctor = d1,
                parentDiscussion = null
            )
        )
        // a reply to td2
        threadDiscussionRepository.save(
            ThreadDiscussion(
                content = "Agree on biliary; check LFTs and RUQ tenderness.",
                diagnosisSuggestion = "Biliary colic",
                confidenceLevel = 7,
                thread = t2,
                doctor = d2,
                parentDiscussion = td2
            )
        )

        // --- Forks (based on public cases) ---
        forkRepository.save(
            Fork(
                title = "Alternative: GERD focus",
                description = "Focus on reflux management and PPI trial.",
                alternativeDiagnosis = "GERD",
                alternativeTreatment = "PPI + lifestyle",
                analysisNotes = "Symptoms worse post meals.",
                recommendations = "Endoscopy if persistent.",
                origin = pub1,
                editor = d1
            )
        )
        forkRepository.save(
            Fork(
                title = "Surgical consult pathway",
                description = "If pain persists, surgical consult.",
                alternativeDiagnosis = "Appendicitis (low prob.)",
                alternativeTreatment = "Observation, surgical eval if rebound tenderness",
                analysisNotes = "No guarding, low fever.",
                recommendations = "Re-check labs in 24h.",
                origin = pub2,
                editor = d2
            )
        )
    }
}
