package com.batchofcode.runtimelocal.handler

import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

fun main() {
    val port = 9000

    ServerFilters.CatchAll()
        .then(Routes())
        .asServer(ApacheServer(port)).start()

    println("Started Lambda Runtime Local on Port $port")
    println("GET invocations - http://localhost:$port/2018-06-01/runtime/invocation/next")
    println("POST events - http://localhost:$port/2018-06-01/runtime/events/next")
    println("POST init error - http://localhost:$port/2018-06-01/runtime/init/error")
}