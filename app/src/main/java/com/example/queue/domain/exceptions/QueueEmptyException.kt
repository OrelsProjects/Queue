package com.example.queue.domain.exceptions

import kotlin.Exception

class QueueEmptyException(exception: Exception): Exception(exception)