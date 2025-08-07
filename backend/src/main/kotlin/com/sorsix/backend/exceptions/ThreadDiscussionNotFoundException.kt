package com.sorsix.backend.exceptions

class ThreadDiscussionNotFoundException(id: Long) : NotFoundException("Thread discussion with id $id not found")
