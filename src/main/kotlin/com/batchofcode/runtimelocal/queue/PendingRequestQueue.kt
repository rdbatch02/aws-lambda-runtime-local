package com.batchofcode.runtimelocal.queue

object PendingRequestQueue {
    private val pendingRequests = mutableMapOf<String, Long>()

    fun pendingRequestCount(): Int = pendingRequests.size

    fun clear() = pendingRequests.clear()

    fun enqueueNewRequest(requestId: String) {
        pendingRequests[requestId] = System.currentTimeMillis()
    }

    fun completeRequest(requestId: String): Long? {
        return pendingRequests.remove(requestId)
    }
}