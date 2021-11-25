package com.constru.queue.domain.exceptions

import com.constru.queue.data.model.QueueItem
import kotlin.Exception

class QueuePredicateFailedException(
    private val queueItem: QueueItem,
    exception: Exception
) : Exception(exception)