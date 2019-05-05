package com.batchofcode.runtimelocal.config

object EnvConfig {
    val port = System.getProperty("port")?.toInt() ?: 9000
}