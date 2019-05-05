package com.batchofcode.runtimelocal.queue

import org.http4k.format.Jackson
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals

class InvocationQueueTest {
    @After
    fun cleanUp() {
        InvocationQueue.clear()
    }

    @Test
    fun `should add a new invocation to the queue`() {
        InvocationQueue.enqueueNewInvocation(Jackson.asJsonObject(""))
        assertEquals(1, InvocationQueue.invocationCount())
    }

    @Test
    fun `should respond and remove event from invocation queue`() {
        InvocationQueue.enqueueNewInvocation(Jackson.asJsonObject("{\"test\": \"test\"}"))
        val invocationEvent = InvocationQueue.getInvocation()
        assertEquals(0, InvocationQueue.invocationCount())
        assertEquals("{\"test\": \"test\"}", invocationEvent!!.asText())
    }
}