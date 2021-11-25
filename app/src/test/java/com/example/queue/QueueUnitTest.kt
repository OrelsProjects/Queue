package com.example.queue

import com.example.queue.data.model.QueueItem
import com.example.queue.domain.exceptions.QueueEmptyException
import com.example.queue.domain.model.Queue
import com.example.queue.model.TestItem
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.math.exp

class QueueUnitTest {

    private lateinit var queueTrue: Queue<QueueItem>
    private lateinit var queueFalse: Queue<QueueItem>

    private val testList: ArrayList<QueueItem> =
        arrayListOf(TestItem("a"), TestItem("b"), TestItem("c"), TestItem("d"))
    private var timesAction = 0
    private var timesError = 0
    private var timesComplete = 0

    @Before
    fun before() {
        queueTrue = Queue(
            ArrayList(testList),
            action = ::onAction,
            onComplete = ::onComplete,
            onError = ::onError
        ) { true }
        queueFalse = Queue(
            ArrayList(testList),
            action = ::onAction,
            onComplete = ::onComplete,
            onError = ::onError
        ) { false }
    }

    @SuppressWarnings
    private fun onAction(item: QueueItem) = timesAction++

    private fun onComplete() = timesComplete++

    @SuppressWarnings
    private fun onError(error: Throwable) = timesError++

    @Test
    fun testTrueQueueInvoke() {
        queueTrue.run()
        Assert.assertEquals(timesAction, testList.size)
        Assert.assertEquals(timesComplete, 1)
        Assert.assertEquals(timesError, 0)
    }

    @Test
    fun testFalseQueueInvoke() {
        queueFalse.run()
        Assert.assertEquals(timesAction, 0)
        Assert.assertEquals(timesComplete, 0)
        Assert.assertEquals(timesError, 1)
    }

    @Test
    fun testEmptyQueue() {
        Assert.assertThrows(QueueEmptyException::class.java) {
            queueTrue.pop()
            queueTrue.pop()
            queueTrue.pop()
            queueTrue.pop()
            queueTrue.pop()
        }
        Assert.assertThrows(QueueEmptyException::class.java) {
            queueTrue.clear()
            queueTrue.pop()
        }
    }

}