package us.cornus.rotlin


/**
 * Created by ejw on 5/28/17.
 */

fun randomIndex(size: Int) = Math.floor(RNG.getUniform() * size).toInt()

fun <T> Array<T>.random() : T? = if (this.isEmpty()) { null } else { this[randomIndex(this.size)] }

inline fun <reified T> Array<T>.randomize() : Array<T> {
    if (this.isEmpty()) return this
    val result = ArrayList<T>()
    val indices = ArrayList<Int>(this.size)
    indices += IntRange(0, this.size)
    while(indices.isNotEmpty()) {
        val index = indices[randomIndex(indices.size)]
        result.add(this[index])
        indices.remove(index)
    }
    return result.toTypedArray()
}

fun Byte.rotmod(n : Number) = ((this.rem(n.toByte())) + n.toByte()) % n.toByte()
fun Short.rotmod(n : Number) = ((this.rem(n.toShort())) + n.toShort()) % n.toShort()
fun Int.rotmod(n : Number) = ((this.rem(n.toInt())) + n.toInt()) % n.toInt()
fun Long.rotmod(n : Number) = ((this.rem(n.toLong())) + n.toLong()) % n.toLong()
fun Double.rotmod(n : Number) = ((this.rem(n.toDouble())) + n.toDouble()) % n.toDouble()

fun CharSequence.rpad(character : Char = '0', count : Int = 2) = this.padEnd(length = count, padChar = character).toString()
fun CharSequence.lpad(character : Char = '0', count : Int = 2) = this.padStart(length = count, padChar = character).toString()

fun <T> ArrayList<T>.pop() : T? {
    if (this.isEmpty()) return null
    val value = this.get(0)
    this.removeAt(0)
    return value
}
