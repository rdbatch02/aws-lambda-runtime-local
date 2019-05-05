package com.batchofcode.runtimelocal.handler

import com.batchofcode.runtimelocal.TestLogger
import com.batchofcode.runtimelocal.queue.InvocationQueue
import com.batchofcode.runtimelocal.queue.PendingRequestQueue
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson
import org.http4k.hamkrest.hasBody
import org.http4k.hamkrest.hasHeader
import org.http4k.hamkrest.hasStatus
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class InvocationHandlerTest {
    @Before
    fun setup() {
        InvocationQueue.clear()
        PendingRequestQueue.clear()
    }

    @After
    fun tearDown() {
        InvocationQueue.clear()
        PendingRequestQueue.clear()
    }

    @Test
    fun `should respond OK when getting next invocation without a pending event`() {
        val response = InvocationHandler()(Request(Method.GET, "/next"))
        assertThat(response, hasStatus(OK))
    }

    @Test
    fun `should respond with to invocation next request with pending event`() {
        InvocationQueue.enqueueNewInvocation(Jackson.asJsonObject("test"))
        val response = InvocationHandler()(Request(Method.GET, "/next"))
        assertThat(response, hasStatus(OK))
        assertThat(response, hasBody("\"test\""))
        assertThat(response, hasHeader("Lambda-Runtime-Aws-Request-Id"))
        assertThat(response, hasHeader("Lambda-Runtime-Deadline-Ms"))
        assertThat(response, hasHeader("Lambda-Runtime-Invoked-Function-Arn"))
        assertThat(response, hasHeader("Lambda-Runtime-Trace-Id"))
        assertThat(response, hasHeader("Lambda-Runtime-Client-Context"))
        assertThat(response, hasHeader("Lambda-Runtime-Cognito-Identity"))
    }

    @Test
    fun `should enqueue request ID when repsonding to invocation next request`() {
        InvocationQueue.enqueueNewInvocation(Jackson.asJsonObject("test"))
        InvocationHandler()(Request(Method.GET, "/next"))
        assertEquals(1, PendingRequestQueue.pendingRequestCount())
    }

    @Test
    fun `should accept response and terminate pending request`() {
        InvocationQueue.enqueueNewInvocation(Jackson.asJsonObject("test"))
        val nextRequest = InvocationHandler()(Request(Method.GET, "/next"))
        val requestId = nextRequest.header("Lambda-Runtime-Aws-Request-Id")!!

        val response = InvocationHandler(TestLogger)(Request(Method.POST, "/$requestId/response").body("{\"test\":\"test\"}"))
        assertThat(response, hasStatus(OK))
        assertEquals(0, PendingRequestQueue.pendingRequestCount())
    }

    @Test
    fun `should accept error and terminate pending request`() {
        InvocationQueue.enqueueNewInvocation(Jackson.asJsonObject("test"))
        val nextRequest = InvocationHandler()(Request(Method.GET, "/next"))
        val requestId = nextRequest.header("Lambda-Runtime-Aws-Request-Id")!!

        val response = InvocationHandler(TestLogger)(Request(Method.POST, "/$requestId/error").body("{\"error\":\"err message\"}"))
        assertThat(response, hasStatus(OK))
        assertEquals(0, PendingRequestQueue.pendingRequestCount())
    }
}