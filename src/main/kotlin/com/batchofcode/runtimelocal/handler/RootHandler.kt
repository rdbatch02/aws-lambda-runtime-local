package com.batchofcode.runtimelocal.handler

import com.batchofcode.runtimelocal.config.EnvConfig
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.server.ApacheServer
import org.http4k.server.asServer

fun main() {
    val port = EnvConfig.port

    ServerFilters.CatchAll()
        .then(Routes())
        .asServer(ApacheServer(port)).start()

    println("Started Lambda Runtime Local on Port $port")
    println("Available Lambda Runtime Endpoints ------------------------------------------------")
    println("GET invocations - http://localhost:$port/2018-06-01/runtime/invocation/next")
    println("POST invocation response - http://localhost:$port/2018-06-01/runtime/invocation/\$REQUEST_ID/response")
    println("POST invocation error - http://localhost:$port/2018-06-01/runtime/invocation/\$REQUEST_ID/error")
    println("POST init error - http://localhost:$port/2018-06-01/runtime/init/error")
    println("-----------------------------------------------------------------------------------")
    println("Helper Endpoints:")
    println("POST event - http://localhost:$port/event/next")
}