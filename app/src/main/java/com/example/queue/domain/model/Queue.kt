package com.example.queue.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.queue.data.model.QueueAction
import com.example.queue.data.model.QueueConsumer
import com.example.queue.data.model.QueueWrapper
import com.example.queue.data.model.QueueItem
import com.example.queue.domain.exceptions.QueueEmptyException
import com.example.queue.domain.exceptions.QueuePredicateFailedException
import java.util.function.Predicate


class Queue<T : QueueItem>(
    private val queueItems: ArrayList<T> = ArrayList(),
    private val action: (item: T) -> Unit,
    private val onComplete: () -> Unit,
    private val onError: (error: Throwable) -> Unit,
    private val predicate: Predicate<T>? = null,
) : QueueWrapper<T> {

    override fun push(item: T) = queueItems.add(item)

    override fun pushAll(items: List<T>) = queueItems.addAll(items)

    override fun pop(): T =
        if (queueItems.isNotEmpty()) queueItems.removeFirst()
        else throw QueueEmptyException(Exception("Couldn't pop.\nThe queue is empty."))

    override fun isEmpty(): Boolean = queueItems.isEmpty()

    override fun isNotEmpty(): Boolean = queueItems.isNotEmpty()

    override fun clear() = queueItems.clear()


    @RequiresApi(Build.VERSION_CODES.N)
    operator fun invoke() {
        try {
            queueItems.forEach {
                if (predicate != null && !predicate.test(it)) {
                    throw QueuePredicateFailedException(
                        it,
                        Exception("Could not run the function. Predicate failed.")
                    )
                }
                action(it)
            }
            onComplete()
            clear()
        } catch (exception: Exception) {
            onError(exception)
        }
    }
}