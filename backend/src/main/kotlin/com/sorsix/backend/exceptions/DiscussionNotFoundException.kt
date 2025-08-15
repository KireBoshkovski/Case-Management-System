package com.sorsix.backend.exceptions

class DiscussionNotFoundException(id: Long) : NotFoundException("Discussion with id $id Not Found")