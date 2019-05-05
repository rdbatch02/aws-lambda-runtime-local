package com.batchofcode.runtimelocal.handler

import com.batchofcode.runtimelocal.TestLogger
import org.http4k.core.Method
import org.http4k.core.Request
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class InitHandlerTest {
    @Before
    fun setup() {
        TestLogger.clear()
    }

    @After
    fun tearDown() {
        TestLogger.clear()
    }

    @Test
    fun `should log init errors`() {
        InitHandler(TestLogger)(Request(Method.POST, "/error").body("test error"))
        assertTrue(TestLogger.loggedMessages.contains("test error"))
    }
}