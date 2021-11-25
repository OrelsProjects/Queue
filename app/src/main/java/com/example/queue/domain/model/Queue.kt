package com.example.queue.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.queue.data.model.QueueWrapper
import com.example.queue.data.model.QueueItem
import com.example.queue.domain.exceptions.QueueEmptyException
import com.example.queue.domain.exceptions.QueuePredicateFailedException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.function.Predicate
import kotlin.collections.ArrayList


class Queue<T : QueueItem>(
    private val queueItems: ArrayList<T> = ArrayList(),
    private val action: (item: T) -> Unit,
    private val onComplete: () -> Unit,
    private val onError: (error: Throwable) -> Unit,
    private val predicate: Predicate<T>? = null,
) : QueueWrapper<T> {

    /**
     * Each queue needs an ID
     */
    val id = UUID.randomUUID()

    /**
     * Run the invoke in the background, so multiple queues can run simultaneously.
     */
    private val networkScope = CoroutineScope(Dispatchers.IO)

    override fun push(item: T) = queueItems.add(item)

    override fun pushAll(items: List<T>) = queueItems.addAll(items)

    override fun pop(): T =
        if (queueItems.isNotEmpty()) queueItems.removeFirst()
        else throw QueueEmptyException(Exception("Couldn't pop.\nThe queue is empty."))

    override fun isEmpty(): Boolean = queueItems.isEmpty()

    override fun isNotEmpty(): Boolean = queueItems.isNotEmpty()

    override fun clear() = queueItems.clear()

    override fun remove(item: T): Boolean = queueItems.remove(item)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun run() {
        try {
            networkScope.launch {
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
            }
        } catch (exception: Exception) {
            onError(exception)
        }
    }
}