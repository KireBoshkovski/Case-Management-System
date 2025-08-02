package com.sorsix.backend.exceptions

class PatientNotFoundException(id: Long) : NotFoundException("Patient with $id not found")
