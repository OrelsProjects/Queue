package com.example.queue.data.model

interface QueueWrapper<T> {
    fun push(item: T): Boolean
    fun pushAll(items: List<T>): Boolean
    fun pop(): T
    fun isEmpty(): Boolean
    fun isNotEmpty(): Boolean
    fun clear()
}