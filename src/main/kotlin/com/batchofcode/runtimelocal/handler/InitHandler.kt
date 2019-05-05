package com.batchofcode.runtimelocal.handler

import com.batchofcode.runtimelocal.logging.Logger
import com.batchofcode.runtimelocal.logging.PrintLogger
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

object InitHandler {
    private lateinit var logger: Logger

    operator fun invoke(logger: Logger = PrintLogger): RoutingHttpHandler {
        this.logger = logger
        return routes(
            "/error" bind Method.POST to error()
        )
    }

    fun error(): HttpHandler = handler@{
        logger.log("RUNTIME REPORTED INIT ERROR:")
        logger.log(it.bodyString())
        Response(OK)
    }
}