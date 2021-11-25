package com.example.queue.data.model

interface QueueConsumer<T> {
    fun run(t: T)
}