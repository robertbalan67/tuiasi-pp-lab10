package ro.tuiasi.pp.lab10.pipeline

import kotlinx.coroutines.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Procesare pipeline a unui vector de întregi prin 3 etape:
 *
 *   Input → [× alpha] → [sortare] → Output
 *
 * Fiecare etapă rulează într-o corutină separată și comunică prin Channel<Int>.
 */
class PipelineProcessor {

    /**
     * Procesează lista [input] prin pipeline și returnează rezultatul sortat.
     *
     * @param input  lista de intrare
     * @param alpha  factorul de înmulțire (Etapa 1)
     */
    fun process(input: List<Int>, alpha: Int): List<Int> = runBlocking {

        // Etapa 1 → Etapa 2: canal cu elemente înmulțite
        val stage1Channel = Channel<Int>(Channel.BUFFERED)

        // Etapa 2 → Etapa 3: canal cu elemente sortate
        val stage2Channel = Channel<Int>(Channel.BUFFERED)

        // ── Etapa 1: înmulțire × alpha ─────────────────────────────────────
        launch {
            for (item in input) {
                stage1Channel.send(item * alpha)
            }
            stage1Channel.close()
        }

        // ── Etapa 2: colectare + sortare ────────────────────────────────────
        launch {
            val buffer = mutableListOf<Int>()
            for (item in stage1Channel) {
                buffer.add(item)
            }
            for (item in buffer.sorted()) {
                stage2Channel.send(item)
            }
            stage2Channel.close()
        }

        // ── Etapa 3: colectare output ───────────────────────────────────────
        val result = mutableListOf<Int>()
        for (item in stage2Channel) {
            result.add(item)
        }
        result
    }
}
