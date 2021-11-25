package com.example.queue.domain.exceptions

import com.example.queue.data.model.QueueItem
import kotlin.Exception

class QueuePredicateFailedException(
    private val queueItem: QueueItem,
    exception: Exception
) : Exception(exception)