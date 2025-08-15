package com.sorsix.backend.exceptions

class CaseNotFoundException(id: Long) : NotFoundException("Case with id $id not found")