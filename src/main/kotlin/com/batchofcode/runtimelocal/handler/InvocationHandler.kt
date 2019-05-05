package com.batchofcode.runtimelocal.handler

import com.batchofcode.runtimelocal.logging.Logger
import com.batchofcode.runtimelocal.logging.PrintLogger
import com.batchofcode.runtimelocal.queue.InvocationQueue
import com.batchofcode.runtimelocal.queue.PendingRequestQueue
import org.http4k.core.*
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson.json
import org.http4k.lens.Header
import org.http4k.lens.string
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import java.util.*

object InvocationHandler {
    private lateinit var logger: Logger

    operator fun invoke(logger: Logger = PrintLogger): RoutingHttpHandler {
        this.logger = logger
        return routes(
            "/next" bind Method.GET to next(),
            "/{requestId}/response" bind Method.POST to response(),
            "/{requestId}/error" bind Method.POST to error()
        )
    }

    fun next(): HttpHandler = handler@{
        val nextInvocation = InvocationQueue.getInvocation() ?: return@handler Response(OK)
        val requestId = UUID.randomUUID().toString()
        PendingRequestQueue.enqueueNewRequest(requestId)
        Response(OK).with(
            Header.required("Lambda-Runtime-Aws-Request-Id") of requestId,
            Header.required("Lambda-Runtime-Deadline-Ms") of (System.currentTimeMillis() + 30 * 1000L).toString(), // 30s default timeout, possibly configurable in the future
            Header.required("Lambda-Runtime-Invoked-Function-Arn") of "arn:aws:lambda:us-east-2:123456789012:function:custom-runtime",
            Header.required("Lambda-Runtime-Trace-Id") of "Root=1-5bef4de7-ad49b0e87f6ef6c87fc2e700;Parent=9a9197af755a6419;Sampled=1",
            Header.required("Lambda-Runtime-Client-Context") of "",
            Header.required("Lambda-Runtime-Cognito-Identity") of "",
            Body.json().toLens() of nextInvocation
        )
    }

    fun response(): HttpHandler = handler@{
        val requestBody = Body.json().toLens()(it)
        val traceIdHeader = Header.optional("_X_AMZN_TRACE_ID")(it)
        val requestId = it.path("requestId") ?: return@handler Response(BAD_REQUEST).with(
            Body.string(ContentType.TEXT_PLAIN).toLens() of "Missing Request ID"
        )
        val startTime = PendingRequestQueue.completeRequest(requestId)
        if (startTime == null) {
            logger.log("--------------------------------------")
            logger.log("Runtime responded to $requestId but Lambda did not have an active request with that ID")
            logger.log("--------------------------------------")
            Response(OK)
        } else {
            val totalTime = System.currentTimeMillis() - startTime
            logger.log("--------------------------------------")
            logger.log("Request $requestId completed in $totalTime ms")
            logger.log("Body - $requestBody")
            logger.log("Trace ID Header - $traceIdHeader")
            logger.log("--------------------------------------")
            Response(OK)
        }
    }

    fun error(): HttpHandler = handler@{
        val requestBody = Body.json().toLens()(it)
        val traceIdHeader = Header.optional("_X_AMZN_TRACE_ID")(it)
        val requestId = it.path("requestId") ?: return@handler Response(BAD_REQUEST).with(
            Body.string(ContentType.TEXT_PLAIN).toLens() of "Missing Request ID"
        )
        val startTime = PendingRequestQueue.completeRequest(requestId)
        if (startTime == null) {
            logger.log("--------------------------------------")
            logger.log("Runtime responded to $requestId but Lambda did not have an active request with that ID")
            logger.log("--------------------------------------")
            Response(OK)
        } else {
            val totalTime = System.currentTimeMillis() - startTime
            logger.log("--------------------------------------")
            logger.log("REQUEST RETURNED AN ERROR!")
            logger.log("Request $requestId completed in $totalTime ms")
            logger.log("Error Body - $requestBody")
            logger.log("Trace ID Header - $traceIdHeader")
            logger.log("--------------------------------------")
            Response(OK)
        }

    }

}