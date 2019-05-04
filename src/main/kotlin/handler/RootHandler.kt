package handler

import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

fun main() {
    val port = 9000
    val routes = routes(
        "/invocation" bind InvocationHandler(),
        "/events" bind EventHandler(),
        "/init" bind InitHandler()
    )

    val server = routes("/2018-06-01/runtime" bind routes).asServer(ApacheServer(port)).start()
    println("Started Lambda Runtime Local on Port $port")
    println("GET invocations - http://localhost:$port/2018-06-01/runtime/invocation/next")
    println("POST events - http://localhost:$port/2018-06-01/runtime/events/next")
    println("POST init error - http://localhost:$port/2018-06-01/runtime/init/error")
}