package com.sorsix.backend.exceptions

class ExaminationNotFoundException(id: Long) : NotFoundException("Examination with id $id not found")