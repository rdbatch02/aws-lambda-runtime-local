package com.batchofcode.runtimelocal.logging

object PrintLogger : Logger {
    override fun log(message: String) = println(message)
}