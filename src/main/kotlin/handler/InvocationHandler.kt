package handler

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
import queue.InvocationQueue
import queue.PendingRequestQueue
import java.util.*

object InvocationHandler {
    operator fun invoke(): RoutingHttpHandler {
        return routes(
            "/next" bind Method.GET to next(),
            "/{requestId}/response" bind Method.POST to response(),
            "/{requestId}/error" bind Method.POST to error()
        )
    }

    private fun next(): HttpHandler = handler@{
        val nextInvocation = InvocationQueue.getInvocation() ?: return@handler Response(OK)
        val requestId = UUID.randomUUID().toString()
        PendingRequestQueue.enqueueNewRequest(requestId)
        Response(OK).with(
            Header.required("Lambda-Runtime-Aws-Request-Id") of requestId,
            Header.required("Lambda-Runtime-Deadline-Ms") of (System.currentTimeMillis() + 30*1000L).toString(), // 30s default timeout, possibly configurable in the future
            Header.required("Lambda-Runtime-Invoked-Function-Arn") of "arn:aws:lambda:us-east-2:123456789012:function:custom-runtime",
            Header.required("Lambda-Runtime-Trace-Id") of "Root=1-5bef4de7-ad49b0e87f6ef6c87fc2e700;Parent=9a9197af755a6419;Sampled=1",
            Header.required("Lambda-Runtime-Client-Context") of "",
            Header.required("Lambda-Runtime-Cognito-Identity") of "",
            Body.json().toLens() of nextInvocation
        )
    }

    private fun response(): HttpHandler = handler@{
        val requestBody = Body.json().toLens()(it)
        val traceIdHeader = Header.optional("_X_AMZN_TRACE_ID")(it)
        val requestId = it.path("requestId") ?: return@handler Response(BAD_REQUEST).with(
            Body.string(ContentType.TEXT_PLAIN).toLens() of "Missing Request ID"
        )
        val startTime = PendingRequestQueue.completeRequest(requestId)
        if (startTime == null) {
            println("--------------------------------------")
            println("Runtime responded to $requestId but Lambda did not have an active request with that ID")
            println("--------------------------------------")
            Response(OK)
        }
        else {
            val totalTime = System.currentTimeMillis() - startTime
            println("--------------------------------------")
            println("Request $requestId completed in $totalTime ms")
            println("Body - $requestBody")
            println("Trace ID Header - $traceIdHeader")
            println("--------------------------------------")
            Response(OK)
        }
    }

    private fun error(): HttpHandler = handler@{
        val requestBody = Body.json().toLens()(it)
        val traceIdHeader = Header.optional("_X_AMZN_TRACE_ID")(it)
        val requestId = it.path("requestId") ?: return@handler Response(BAD_REQUEST).with(
            Body.string(ContentType.TEXT_PLAIN).toLens() of "Missing Request ID"
        )
        val startTime = PendingRequestQueue.completeRequest(requestId)
        if (startTime == null) {
            println("--------------------------------------")
            println("Runtime responded to $requestId but Lambda did not have an active request with that ID")
            println("--------------------------------------")
            Response(OK)
        }
        else {
            val totalTime = System.currentTimeMillis() - startTime
            println("--------------------------------------")
            println("REQUEST RETURNED AN ERROR!")
            println("Request $requestId completed in $totalTime ms")
            println("Error Body - $requestBody")
            println("Trace ID Header - $traceIdHeader")
            println("--------------------------------------")
            Response(OK)
        }

    }

}