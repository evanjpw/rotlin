/**
 * Created by ejw on 5/27/17.
 */

package us.cornus.rotlin

private typealias Datum = MutableMap<String, Double?>

data class StringGeneratorOptions(val words : Boolean = false, val order : Int = 3, val prior : Double = 0.001)

/**
 * @class (Markov process)-based string generator.
 * Copied from a <a href="http://www.roguebasin.roguelikedevelopment.org/index.php?title=Names_from_a_high_order_Markov_Process_and_a_simplified_Katz_back-off_scheme">RogueBasin article</a>.
 * Offers configurable order and prior.
 * @param {bool} [words=false] Use word mode?
 * @param {int} [order=3]
 * @param {float} [prior=0.001]
 */
class StringGenerator (private val words: Boolean = false, private val order: Int = 3, private val prior: Double = 0.001) {

    constructor(options : StringGeneratorOptions) : this(options.words, options.order, options.prior)

    private val boundaryChar = '\u0000'
    private val boundary = boundaryChar.toString()
    private val suffix = boundary
    private val data : MutableMap<String, Datum> = HashMap()
    private val priorValues : MutableMap<String, Double> = HashMap()
    private val prefix = MutableList(order) { boundary }

    init {
        priorValues[boundary] = prior
    }

    /**
     * Remove all learning data
     */
    fun clear() : Unit {
        data.clear()
        priorValues.clear()
        priorValues[boundary] = prior
    }

    /**
     * @returns {string} Generated string
     */
    fun generate() : String {
        val result : ArrayList<String> = ArrayList()
        result.add(sample(prefix.toTypedArray()))
        while (result[result.size-1] != boundary) {
            val sampledResult = sample(result.toTypedArray())
            //System.out.println("generate(): sample = \"$sampledResult\"")
            result.add(sampledResult)
        }
/*
 */
	return result.slice(IntRange(0, result.size -1)).joinToString()

    }

    /**
     * Observe (learn) a string from a training set
     */
    fun observe(string : String) : Unit {
        val tokens: MutableList<String> = split(string).toMutableList()

        for (token in tokens) priorValues[token] = prior

        tokens.addAll(0, prefix)
        tokens.add(suffix) /* add boundary symbols */

        IntRange(order, tokens.size-1).forEach { i ->
            val context = tokens.slice(IntRange(i-order, i))
            val event = tokens[i]
            context.indices
                    .map { context.slice(IntRange(it, context.size-1)) }
                    .forEach { observeEvent(it.toTypedArray(), event) }
        }
    }

    fun getStats() : String {
        val parts = StringBuilder()

        var priorCount = 0
        priorValues.forEach { _ -> priorCount++ }
        priorCount-- /* boundary */
        parts.append("distinct samples: $priorCount, ")

        var dataCount = 0
        var eventCount = 0
        data.keys.forEach { p : String ->
            dataCount++
            data[p]?.keys?.forEach { _ -> eventCount++ }
        }
        parts.append("dictionary size (contexts): $dataCount, ")
        parts.append("dictionary size (events): $eventCount")

        return parts.toString()
    }


    /**
     * @param {string[]} context
     * @param {string} event
     */
    private fun observeEvent(context : Array<String>, event : String) : Unit {
        val key = join(context)
        if (key !in data) { data[key] = HashMap() }
        val data: Datum? = data[key]

        data?.let {
            if (event !in data) { data[event] = 0.0 }
            data[event]?.inc()
        }
    }

    /**
     * @param {string}
     * @returns {string[]}
     */
    private fun split(str : String) : Array<String> {
        return str.split(Regex(if (words) { "\\s+" } else { "" })).toTypedArray()
    }

    /**
     * @param {string[]}
     * @returns {string}
     */
    private fun join(arr : Array<String>) : String {
        return arr.joinToString(if (words) { " " } else { "" })
    }

    /**
     * @param {string[]}
     * @returns {string}
     */
    private fun sample(context : Array<String>) : String {
        //System.out.println("sample(): in")
        val ncontext = backoff(context)
        val key = join(ncontext)
        val ndata = data[key]

        var available: Datum = HashMap()

        if (prior != 0.0) {
            for (event in priorValues.keys) { available[event] = priorValues[event] }
            //System.out.println("sample(): got priorValues")
            for (event in ndata?.keys!!) {
                available[event] = ndata[event]!! + (available[event] ?: 0.0)
            }
        } else {
            available = ndata ?: available
        }
        //System.out.println("sample(): About to getWeightValue")
        return RNG.getWeightedValue(available)
    }

    /**
     * @param {string[]}
     * @returns {string[]}
     */
    private fun backoff(context: Array<String>): Array<String> {
        var joinableReturnValue = if (context.size > order) {
            context.slice(IntRange(context.size - order - 1, context.size-1)).toTypedArray()
        } else if (context.size < order) {
            (prefix.slice(IntRange(0, order - context.size)) + context).toTypedArray()
        } else {
            context
        }

        while (join(joinableReturnValue) !in data && joinableReturnValue.isNotEmpty()) {
            //System.out.println("backoff(): joinableReturnValue.size = ${joinableReturnValue.size}")
            joinableReturnValue = joinableReturnValue.slice(IntRange(1, joinableReturnValue.size-1)).toTypedArray()
        }

        //System.out.println("backoff(): returning $joinableReturnValue")
        return joinableReturnValue
    }

}
