package ro.tuiasi.pp.lab10.chain

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

// ─── Interfață ────────────────────────────────────────────────────────────────

interface Handler {
    var next: Handler?
    var previous: Handler?
    suspend fun handleRequest(message: String): String
}

// ─── Handler-e ────────────────────────────────────────────────────────────────

/**
 * Inițiază cererea cu prefix "Request - " și primește răspunsul final.
 */
class CEOHandler : Handler {
    override var next: Handler? = null
    override var previous: Handler? = null

    override suspend fun handleRequest(message: String): String = coroutineScope {
        val request = "Request - $message"
        val result = async {
            delay(10)
            next?.handleRequest(request) ?: request
        }
        result.await()
    }
}

/**
 * Handler intermediar — procesează și pasează mai departe.
 */
class ExecutiveHandler : Handler {
    override var next: Handler? = null
    override var previous: Handler? = null

    override suspend fun handleRequest(message: String): String = coroutineScope {
        val result = async {
            delay(10)
            next?.handleRequest(message) ?: message
        }
        result.await()
    }
}

/**
 * Handler intermediar — procesează și pasează mai departe.
 */
class ManagerHandler : Handler {
    override var next: Handler? = null
    override var previous: Handler? = null

    override suspend fun handleRequest(message: String): String = coroutineScope {
        val result = async {
            delay(10)
            next?.handleRequest(message) ?: message
        }
        result.await()
    }
}

/**
 * Handler final — procesează cererea și returnează răspunsul cu prefix "Response - ".
 */
class HappyWorkerHandler : Handler {
    override var next: Handler? = null
    override var previous: Handler? = null

    override suspend fun handleRequest(message: String): String = coroutineScope {
        val result = async {
            delay(10)
            "Response - $message"
        }
        result.await()
    }
}

// ─── Utilitar: construiește lanțul complet ────────────────────────────────────

/**
 * Asamblează CEO → Executive → Manager → HappyWorker și returnează CEO-ul.
 */
fun buildChain(): Handler {
    val ceo      = CEOHandler()
    val exec     = ExecutiveHandler()
    val manager  = ManagerHandler()
    val worker   = HappyWorkerHandler()

    ceo.next     = exec;    exec.previous    = ceo
    exec.next    = manager; manager.previous  = exec
    manager.next = worker;  worker.previous   = manager

    return ceo
}
