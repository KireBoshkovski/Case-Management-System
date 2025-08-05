package com.sorsix.backend.exceptions

class MedicalThreadNotFoundException(id: Long) : NotFoundException("Medical thread with id $id not found")
