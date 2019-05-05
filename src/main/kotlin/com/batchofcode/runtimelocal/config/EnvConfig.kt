package com.batchofcode.runtimelocal.config

object EnvConfig {
    val port = System.getProperty("runtime.port")?.toInt() ?: System.getenv("RUNTIME_PORT")?.toInt() ?: 9000
}