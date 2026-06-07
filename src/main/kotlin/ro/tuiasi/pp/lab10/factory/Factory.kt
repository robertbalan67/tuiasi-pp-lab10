package ro.tuiasi.pp.lab10.factory

import ro.tuiasi.pp.lab10.chain.CEOHandler
import ro.tuiasi.pp.lab10.chain.Handler
import ro.tuiasi.pp.lab10.chain.HappyWorkerHandler

// ─── Abstract Factory ─────────────────────────────────────────────────────────

interface Factory {
    fun createHandler(): Handler
}

/** Produce handler-e de nivel C-suite (CEOHandler). */
class EliteFactory : Factory {
    override fun createHandler(): Handler = CEOHandler()
}

/** Produce handler-e de nivel muncitor (HappyWorkerHandler). */
class HappyWorkerFactory : Factory {
    override fun createHandler(): Handler = HappyWorkerHandler()
}

/** Factory Producer — selectează factory-ul potrivit după tip. */
object FactoryProducer {
    /**
     * @param type "elite" → EliteFactory, "happyworker" → HappyWorkerFactory
     * @throws IllegalArgumentException pentru tipuri necunoscute
     */
    fun getFactory(type: String): Factory = when (type.lowercase()) {
        "elite"       -> EliteFactory()
        "happyworker" -> HappyWorkerFactory()
        else          -> throw IllegalArgumentException("Tip de factory necunoscut: $type")
    }
}
