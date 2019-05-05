package com.batchofcode.runtimelocal.handler

import com.batchofcode.runtimelocal.logging.Logger
import com.batchofcode.runtimelocal.logging.PrintLogger
import com.batchofcode.runtimelocal.queue.InvocationQueue
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson.json
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

object EventHandler {
    private lateinit var logger: Logger

    operator fun invoke(logger: Logger = PrintLogger): RoutingHttpHandler {
        this.logger = logger
        return routes(
            "/next" bind Method.POST to addNextEvent()
        )
    }

    fun addNextEvent(): HttpHandler = handler@{
        val requestbody = Body.json().toLens()(it)
        InvocationQueue.enqueueNewInvocation(requestbody)
        Response(OK)
    }
}