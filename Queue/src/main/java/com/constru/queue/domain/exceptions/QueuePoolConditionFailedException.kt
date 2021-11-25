package com.constru.queue.domain.exceptions

import com.constru.queue.data.model.QueuePoolWrapper

class QueuePoolConditionFailedException(
    private val queuePool: QueuePoolWrapper,
    exception: Exception
) : Exception(exception)