package queue

object PendingRequestQueue {
    private val pendingRequests = mutableMapOf<String, Long>()

    fun enqueueNewRequest(requestId: String) {
        pendingRequests[requestId] = System.currentTimeMillis()
    }

    fun completeRequest(requestId: String): Long? {
        return pendingRequests.remove(requestId)
    }
}