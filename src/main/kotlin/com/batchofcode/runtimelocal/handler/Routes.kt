package com.batchofcode.runtimelocal.handler

import org.http4k.routing.bind
import org.http4k.routing.routes

object Routes {
    operator fun invoke() = routes(
        "/2018-06-01/runtime" bind
                routes(
                    "/invocation" bind InvocationHandler(),
                    "/init" bind InitHandler()
                ),
        "" bind routes("/event" bind EventHandler())
    )
}