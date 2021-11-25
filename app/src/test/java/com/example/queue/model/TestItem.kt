package com.example.queue.model

import com.example.queue.data.model.QueueItem

class TestItem constructor(private val id: String) : QueueItem{
    override fun toString(): String =
        id

}