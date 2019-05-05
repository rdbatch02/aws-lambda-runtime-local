package com.batchofcode.runtimelocal.handler

import com.batchofcode.runtimelocal.queue.InvocationQueue
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.format.Jackson
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class EventHandlerTest {
    @Before
    fun setup() {
        InvocationQueue.clear()
    }

    @After
    fun tearDown() {
        InvocationQueue.clear()
    }

    @Test
    fun `should add new invocation request to queue`() {
        val testInvocation = "{\"test\":\"test\"}"
        EventHandler()(Request(Method.POST, "/next").body(testInvocation))
        assertEquals(1, InvocationQueue.invocationCount())
        assertEquals(testInvocation, Jackson.asJsonString(InvocationQueue.getInvocation()!!))
    }
}