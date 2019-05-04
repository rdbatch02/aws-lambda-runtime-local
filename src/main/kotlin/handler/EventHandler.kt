package handler

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.format.Jackson.json
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import queue.InvocationQueue

object EventHandler {
    operator fun invoke(): RoutingHttpHandler {
        return routes(
            "/next" bind Method.POST to addNextEvent()
        )
    }

    private fun addNextEvent(): HttpHandler = handler@{
        val requestbody = Body.json().toLens()(it)
        InvocationQueue.enqueueNewInvocation(requestbody)
        Response(OK)
    }
}