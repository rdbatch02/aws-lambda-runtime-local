package com.batchofcode.runtimelocal

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK

object TestUtils {
    fun testHandler(): HttpHandler = { Response(OK).body(it.bodyString()) }
}