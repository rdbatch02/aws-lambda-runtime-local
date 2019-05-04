package queue

import com.fasterxml.jackson.databind.JsonNode

object InvocationQueue {
    private val invocationRequests = mutableListOf<JsonNode>()

    fun enqueueNewInvocation(payload: JsonNode) {
        invocationRequests.add(payload)
        println("[ ${invocationRequests.size} PENDING EVENTS ] - Queued event for invocation: $payload ")
    }

    fun getInvocation(): JsonNode? {
        return if (invocationRequests.isNotEmpty()) {
            val request = invocationRequests.removeAt(0)
            println("[ ${invocationRequests.size} PENDING EVENTS ] - Sent event for invocation: $request")
            request
        } else null
    }
}