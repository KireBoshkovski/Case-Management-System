package com.sorsix.backend.exceptions

class DoctorNotFoundException(id: Long) : NotFoundException("Doctor with id $id not found")
