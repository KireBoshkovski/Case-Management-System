package com.sorsix.backend.exceptions

class ForkNotFoundException(id: Long) : NotFoundException("Fork with id $id not found")
