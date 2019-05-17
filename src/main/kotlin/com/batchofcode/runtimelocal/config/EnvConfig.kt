package com.batchofcode.runtimelocal.config

object EnvConfig {
    val port = System.getProperty("runtime.port")?.toInt() ?: System.getenv("RUNTIME_PORT")?.toInt() ?: 9000
    val debug = System.getProperty("runtime.debug")?.toBoolean() ?: System.getenv("RUNTIME_DEBUG")?.toBoolean() ?: false
}