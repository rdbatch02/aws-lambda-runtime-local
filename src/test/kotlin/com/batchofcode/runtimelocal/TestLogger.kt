package com.batchofcode.runtimelocal

import com.batchofcode.runtimelocal.logging.Logger

object TestLogger : Logger {
    val loggedMessages = mutableListOf<String>()

    override fun log(message: String) {
        println(message)
        loggedMessages.add(message)
    }

    fun clear() = loggedMessages.clear()
}