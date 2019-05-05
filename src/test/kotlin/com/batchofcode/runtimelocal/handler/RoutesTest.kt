package com.batchofcode.runtimelocal.handler

import com.batchofcode.runtimelocal.TestUtils.testHandler
import com.natpryce.hamkrest.assertion.assertThat
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.hamkrest.hasStatus
import org.junit.After
import org.junit.Before
import org.junit.Test

class RoutesTest {
    @Before
    fun beforeTests() {
        mockkObject(EventHandler)
        every { EventHandler.addNextEvent() } returns testHandler()

        mockkObject(InitHandler)
        every { InitHandler.error() } returns testHandler()

        mockkObject(InvocationHandler)
        every { InvocationHandler.next() } returns testHandler()
        every { InvocationHandler.response() } returns testHandler()
        every { InvocationHandler.error() } returns testHandler()
    }

    @After
    fun afterTests() {
        unmockkAll()
    }

    @Test
    fun `should route to invocation handler next and respond OK`() {
        val response = Routes()(Request(Method.GET, "/2018-06-01/runtime/invocation/next"))
        assertThat(response, hasStatus(OK))
    }

    @Test
    fun `should route to invocation handler response and respond OK`() {
        val response = Routes()(Request(Method.POST, "/2018-06-01/runtime/invocation/test/response"))
        assertThat(response, hasStatus(OK))
    }

    @Test
    fun `should route to invocation handler error and respond OK`() {
        val response = Routes()(Request(Method.POST, "/2018-06-01/runtime/invocation/test/error"))
        assertThat(response, hasStatus(OK))
    }

    @Test
    fun `should route to init error handler and respond OK`() {
        val response = Routes()(Request(Method.POST, "/2018-06-01/runtime/init/error"))
        assertThat(response, hasStatus(OK))
    }

    @Test
    fun `should route to event handler add and respond OK`() {
        val response = Routes()(Request(Method.POST, "/event/next"))
        assertThat(response, hasStatus(OK))
    }
}