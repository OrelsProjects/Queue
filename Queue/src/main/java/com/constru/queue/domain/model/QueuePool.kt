package com.constru.queue.domain.model

import com.constru.queue.data.model.QueueItem
import com.constru.queue.data.model.QueuePoolWrapper
import com.constru.queue.data.model.QueueWrapper
import com.constru.queue.domain.exceptions.QueuePoolConditionFailedException
import java.lang.Exception

class QueuePool<T : QueueWrapper<QueueItem>>(
    queueList: List<T> = ArrayList(),
    private val condition: (() -> Boolean)? = null,
) : QueuePoolWrapper,
    QueueWrapper<T> {

    private val queuesPool: ArrayList<T> = ArrayList(queueList)

    override fun push(item: T): Boolean = queuesPool.add(item)

    override fun pushAll(items: List<T>): Boolean = queuesPool.addAll(items)

    override fun pop(): T = queuesPool.removeFirst()

    override fun isEmpty(): Boolean = queuesPool.isEmpty()

    override fun isNotEmpty(): Boolean = queuesPool.isNotEmpty()

    override fun clear() = queuesPool.clear()

    override fun remove(item: T): Boolean = queuesPool.remove(item)

    private fun filterEmptyQueues() =
        queuesPool.filter { queue -> queue.isEmpty() }
            .forEach {
                remove(it)
            }

    override fun run() {
        if (condition == null || condition.invoke()) {
            filterEmptyQueues()
            queuesPool.forEach {
                it.run()
            }
        } else throw QueuePoolConditionFailedException(this, Exception("Condition failed"))
    }

}