package handler

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

object InitHandler {
    operator fun invoke(): RoutingHttpHandler {
        return routes(
            "/error" bind Method.POST to error()
        )
    }

    private fun error(): HttpHandler = handler@{
        println("RUNTIME REPORTED INIT ERROR:")
        println(it.bodyString())
        Response(OK)
    }
}