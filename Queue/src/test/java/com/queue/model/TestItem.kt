package com.queue.model

import com.constru.queue.data.model.QueueItem

class TestItem constructor(private val id: String) : QueueItem {
    override fun toString(): String =
        id

}