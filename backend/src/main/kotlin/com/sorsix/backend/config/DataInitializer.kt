package com.sorsix.backend.config

import com.sorsix.backend.domain.*
import com.sorsix.backend.domain.enums.CaseStatus
import com.sorsix.backend.repository.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.LocalDateTime

@Configuration
class DataInitializer {

    @Bean
    fun initData(
        patientRepo: PatientRepository,
        doctorRepo: DoctorRepository,
        caseRepo: CaseRepository,
        examinationRepo: ExaminationRepository,
        forkRepo: ForkRepository
    ) = CommandLineRunner {

        // --- Doctors ---
        val doctor1 = doctorRepo.save(
            Doctor(0, "John", "Doe", "Cardiology", "+38970111222", "john@hospital.com", "Cardio Dept")
        )
        val doctor2 = doctorRepo.save(
            Doctor(0, "Ana", "Petrova", "Neurology", "+38970111333", "ana@hospital.com", "Neuro Dept")
        )

        // --- Patients ---
        val patient1 = patientRepo.save(
            Patient(
                0,
                "Jane",
                "Smith",
                LocalDate.of(1985, 5, 12),
                "Female",
                "+38970111233",
                "jane@example.com",
                "123 Main Street"
            )
        )
        val patient2 = patientRepo.save(
            Patient(
                0,
                "Marko",
                "Markovski",
                LocalDate.of(1992, 7, 19),
                "Male",
                "+38970222334",
                "marko@example.com",
                "456 Side Street"
            )
        )

        // --- Examinations ---
        val exam1 = examinationRepo.save(
            Examination(
                0,
                "ECG",
                "Normal rhythm",
                "OK",
                "None",
                """{"pulse":72}""",
                LocalDateTime.now().minusDays(1),
                doctor1
            )
        )
        val exam2 = examinationRepo.save(
            Examination(
                0,
                "MRI",
                "No abnormalities",
                "Clear",
                "Follow-up in 6 months",
                """{}""",
                LocalDateTime.now().minusDays(2),
                doctor2
            )
        )
        val exam3 = examinationRepo.save(
            Examination(
                0,
                "Blood Test",
                "Elevated WBC",
                "Infection suspected",
                null,
                """{"WBC":13000}""",
                LocalDateTime.now().minusDays(1),
                doctor1
            )
        )

        // --- Cases ---
        val case1 = caseRepo.save(
            Case(
                id = 0,
                public = true,
                bloodType = "A+",
                allergies = "Penicillin",
                description = "Chest pain episodes",
                treatmentPlan = "Monitor and medication",
                status = CaseStatus.ACTIVE,
                createdAt = LocalDateTime.now().minusDays(5),
                updatedAt = LocalDateTime.now(),
                patient = patient1,
                doctor = doctor1,
                examinations = mutableListOf(exam1, exam3)
            )
        )

        val case2 = caseRepo.save(
            Case(
                id = 0,
                public = false,
                bloodType = "O-",
                allergies = null,
                description = "Migraine symptoms",
                treatmentPlan = "MRI and neurological assessment",
                status = CaseStatus.ON_HOLD,
                createdAt = LocalDateTime.now().minusDays(10),
                updatedAt = LocalDateTime.now().minusDays(2),
                patient = patient2,
                doctor = doctor2,
                examinations = mutableListOf(exam2)
            )
        )

        // --- Forks ---
        forkRepo.save(
            Fork(
                title = "Cardiac or Panic?",
                description = "Check for psychological symptoms",
                alternativeDiagnosis = "Anxiety",
                alternativeTreatment = "Cognitive Behavioral Therapy",
                analysisNotes = "ECG ruled out major issues, patient under stress",
                recommendations = "Refer to psychologist",
                origin = case1,
                editor = doctor1
            )
        )

        forkRepo.save(
            Fork(
                title = "Cluster Headaches?",
                description = "Alternative explanation for migraines",
                alternativeDiagnosis = "Cluster headaches",
                alternativeTreatment = "Sumatriptan",
                analysisNotes = "Symptoms periodic, linked to seasonal changes",
                recommendations = "Neurology consultation",
                origin = case2,
                editor = doctor2
            )
        )

        println("✅ Sample data initialized successfully.")
    }
}
