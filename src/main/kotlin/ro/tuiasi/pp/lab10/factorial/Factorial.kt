package ro.tuiasi.pp.lab10.factorial

import kotlinx.coroutines.Channel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Calculează factorialul pentru fiecare valoare din [values] în paralel,
 * folosind 4 corutine consumatoare și un Channel ca coadă de lucru.
 *
 * Exemplu: computeFactorials(listOf(5, 6, 7, 8)) → {5=120, 6=720, 7=5040, 8=40320}
 *
 * @return Map<valoare, factorial>
 */
suspend fun computeFactorials(values: List<Int>): Map<Int, Long> = coroutineScope {
    val channel  = Channel<Int>(Channel.BUFFERED)
    val results  = mutableMapOf<Int, Long>()
    val mutex    = Mutex()

    // Producer: trimite toate valorile pe canal, apoi îl închide
    launch {
        for (v in values) {
            channel.send(v)
        }
        channel.close()
    }

    // 4 corutine consumatoare (async ca să așteptăm finalizarea tuturor)
    val workers = List(4) {
        async {
            for (n in channel) {
                val fact = factorial(n)
                mutex.withLock { results[n] = fact }
            }
        }
    }
    workers.awaitAll()

    results.toMap()
}

/** Calculează n! iterativ. Returnează 1 pentru n ≤ 1. */
private fun factorial(n: Int): Long {
    if (n <= 1) return 1L
    var result = 1L
    for (i in 2..n) result *= i
    return result
}
