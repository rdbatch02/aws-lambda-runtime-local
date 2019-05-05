package com.batchofcode.runtimelocal.queue

import org.junit.After
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PendingRequestQueueTest {
    @After
    fun cleanUp() {
        PendingRequestQueue.clear()
    }

    @Test
    fun `should add a new request to the queue`() {
        val requestGuid = UUID.randomUUID().toString()
        PendingRequestQueue.enqueueNewRequest(requestGuid)
        assertEquals(1, PendingRequestQueue.pendingRequestCount())
    }

    @Test
    fun `should respond and remove request from request queue`() {
        val requestGuid = UUID.randomUUID().toString()
        PendingRequestQueue.enqueueNewRequest(requestGuid)
        val requestTime = PendingRequestQueue.completeRequest(requestGuid)
        assertEquals(0, PendingRequestQueue.pendingRequestCount())
        assertNotNull(requestTime)
    }
}