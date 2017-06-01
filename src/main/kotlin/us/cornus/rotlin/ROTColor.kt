package us.cornus.rotlin

import java.awt.Color

/**
 * Created by ejw on 5/31/17.
 */

data class ROTRGB(val r: Int, val g: Int, val b: Int) {

    constructor(values: List<Int>) : this(values[0], values[1], values[2])

    constructor(triple: Triple<Int, Int, Int>) : this(triple.first, triple.second, triple.third)

    fun toJavaColor(): Color = Color(r, g, b)

    fun toList() = listOf(r, g, b)

    fun toMutableList() = mutableListOf(r, g, b)

    fun toTriple() = Triple(r, g, b)

    fun toHSL() = ROTColor.rgb2hsl(this)

    fun toIntArray() = this.toList().toIntArray()

    fun toTypedArray() = this.toList().toTypedArray()
}

fun Color.toROTRGB(): ROTRGB = ROTRGB(this.red, this.green, this.blue)

data class ROTHSL(val h: Float, val s: Float, val l: Float) {

    constructor(values: List<Float>) : this(values[0], values[1], values[2])

    constructor(triple: Triple<Float, Float, Float>) : this(triple.first, triple.second, triple.third)

    fun toJavaColor() = Color.getHSBColor(h, s, l)

    fun toList() = listOf(h, s, l)

    fun toMutableList() = mutableListOf(h, s, l)

    fun toTriple() = Triple(h, s, l)

    fun toRGB() = ROTColor.hsl2rgb(this)

    fun toFloatArray() = this.toList().toFloatArray()

    fun toTypedArry() = this.toList().toTypedArray()
}

fun Color.toROTHSL(): ROTHSL {
    val hsbVals = Color.RGBtoHSB(this.red, this.green, this.blue, null)
    return ROTHSL(hsbVals[0], hsbVals[1], hsbVals[2])
}

/**
 * @namespace Color operations
 */
object ROTColor {

    private val HEX_DIGIT = Regex("[0-9a-f]", RegexOption.IGNORE_CASE) //g flag
    private val RGB_STRING = Regex("rgb\\(([0-9, ]+)\\)", RegexOption.IGNORE_CASE)
    private val SPACE_COMMA_SPACE = Regex("\\s*,\\s*")

    fun fromString(str: String): ROTRGB {
        val cached: List<Int>
        if (str in _cache) {
            cached = _cache[str]!!
        } else {
            if (str[0] == '#') { /* hex rgb */

                val values = str.jsMatch(HEX_DIGIT, global = true).map{ x: String -> Integer.parseInt(x, 16) }.toTypedArray()
                if (values.size == 3) {
                    cached = values.map({ x: Int -> x * 17 })
                } else {
                    val mvalues = ArrayList<Int>()
                    (0 until 6 step 2).mapTo(mvalues) { values[it + 1] + 16 * values[it] }
                    cached = mvalues
                }

            } else {
                val r: Array<String> = str.jsMatch(RGB_STRING)
                if ((r.isNotEmpty())) { /* decimal rgb */
                    cached = r[1].split(SPACE_COMMA_SPACE).map{ x: String -> Integer.parseInt(x) }
                } else { /* html name */
                    cached = listOf(0, 0, 0)
                }
            }

            _cache[str] = cached
        }

        return ROTRGB(cached)
    }

    /**
     * Add two or more colors
     * @param {number[]} color1
     * @param {number[]} color2
     * @returns {number[]}
     */
    fun add(color1: List<Int>, vararg colors: List<Int>): ROTRGB {
        return _add(color1, colors)
    }

    fun add(color1: ROTRGB, vararg colors: ROTRGB): ROTRGB {
        val c1 = color1.toList()
        val cs: Array<out List<Int>> = colors.map { it.toList() }.toTypedArray()
        return _add(c1, cs)
    }

    private fun _add(color1: List<Int>, colors: Array<out List<Int>>): ROTRGB {
        val result = color1.toMutableList()
        for (i in 0 until 3) {
            for (j in 0 until colors.size) {
                result[i] += colors[j][i]
            }
        }
        return ROTRGB(result)
    }

    /**
     * Add two or more colors, MODIFIES FIRST ARGUMENT
     * @param {number[]} color1
     * @param {number[]} color2
     * @returns {number[]}
     */
    fun add_(color1: MutableList<Int>, vararg colors: List<Int>): MutableList<Int> {
        for (i in 0 until 3) {
            for (j in 0 until colors.size) {
                color1[i] = color1[i] + colors[j][i]
            }
        }
        return color1
    }

    /**
     * Multiply (mix) two or more colors
     * @param {number[]} color1
     * @param {number[]} color2
     * @returns {number[]}
     */
    fun multiply(color1: List<Int>, vararg colors: List<Int>): ROTRGB {
        return _multiply(color1, colors)
    }

    fun multiply(color1: ROTRGB, vararg colors: ROTRGB): ROTRGB {
        val c1 = color1.toList()
        val cs: Array<out List<Int>> = colors.map { it.toList() }.toTypedArray()
        return _multiply(c1, cs)
    }

    private fun _multiply(color1: List<Int>, colors: Array<out List<Int>>): ROTRGB {
        val result = color1.map { it.toDouble() }.toMutableList()
        for (i in 0 until 3) {
            for (j in 0 until colors.size) {
                result[i] *= colors[j][i] / 255.0
            }
            result[i] = Math.round(result[i]).toDouble()
        }
        return ROTRGB(result.map { it.toInt() })
    }

    /**
     * Multiply (mix) two or more colors, MODIFIES FIRST ARGUMENT
     * @param {number[]} color1
     * @param {number[]} color2
     * @returns {number[]}
     */
    fun multiply_(color1: MutableList<Int>, vararg colors: List<Int>): MutableList<Int> {
        val result = _multiply(color1, colors)
        if (color1.size > 0) color1[0] = result.r
        if (color1.size > 1) color1[1] = result.g
        if (color1.size > 2) color1[2] = result.b
        return color1
    }

    /**
     * Interpolate (blend) two colors with a given factor
     * @param {number[]} color1
     * @param {number[]} color2
     * @param {float} [factor=0.5] 0..1
     * @returns {number[]}
     */
    fun interpolate(color1: List<Int>, color2: List<Int>, factor: Float = 0.5f): ROTRGB {
        val result = color1.toMutableList()
        for (i in 0 until 3) {
            result[i] = Math.round(result[i] + factor * (color2[i] - color1[i]))
        }
        return ROTRGB(result)
    }

    fun interpolate(color1: ROTRGB, color2: ROTRGB, factor: Float = 0.5f): ROTRGB = interpolate(color1.toList(), color2.toList(), factor)

    /**
     * Interpolate (blend) two colors with a given factor in HSL mode
     * @param {number[]} color1
     * @param {number[]} color2
     * @param {float} [factor=0.5] 0..1
     * @returns {number[]}
     */
    fun interpolateHSL(color1: List<Int>, color2: List<Int>, factor: Float = 0.5f): ROTRGB {
        val hsl1 = rgb2hsl(color1).toMutableList()
        val hsl2 = rgb2hsl(color2).toList()
        for (i in 0 until 3) {
            hsl1[i] = hsl1[i] + factor * (hsl2[i] - hsl1[i])
        }
        return hsl2rgb(hsl1)
    }

    fun interpolateHSL(color1: ROTRGB, color2: ROTRGB, factor: Float = 0.5f): ROTRGB = interpolateHSL(color1.toList(), color2.toList(), factor)

    fun randomize(color: ROTRGB, diff: Double) = randomize(color.toList(), diff)

    /**
     * Create a new random color based on this one
     * @param {number[]} color
     * @param {number[]} diff Set of standard deviations
     * @returns {number[]}
     */
    fun randomize(color: List<Int>, idiff: Double): ROTRGB {
        val diff = Math.round(RNG.getNormal(0.0, idiff)).toInt()
        val result = color.toMutableList()
        for (i in 0 until 3) {
            result[i] += diff
        }
        return ROTRGB(result)
    }

    fun randomize(color: ROTRGB, diff: Array<Double>) = randomize(color.toList(), diff)

    /**
     * Create a new random color based on this one
     * @param {number[]} color
     * @param {number[]} diff Set of standard deviations
     * @returns {number[]}
     */
    fun randomize(color: List<Int>, diff: Array<Double>): ROTRGB {
        val result = color.toMutableList()
        for (i in 0 until 3) {
            result[i] += Math.round(RNG.getNormal(0.0, diff[i])).toInt()
        }
        return ROTRGB(result)
    }

    fun rgb2hsl(color: List<Int>) = rgb2hsl(ROTRGB(color))

    /**
     * Converts an RGB color value to HSL. Expects 0..255 inputs, produces 0..1 outputs.
     * @param {number[]} color
     * @returns {number[]}
     */
    fun rgb2hsl(color: ROTRGB): ROTHSL {
        val r: Float = color.r / 255.0f
        val g: Float = color.g / 255.0f
        val b: Float = color.b / 255.0f

        val max = Math.max(r, Math.max(g, b))
        val min = Math.min(r, Math.min(g, b))
        var h: Float = 0.0f
        val s: Float
        val l = (max + min) / 2.0f

        if (max == min) {
            s = 0.0f // achromatic
        } else {
            val d = max - min
            s = (if (l > 0.5f) d / (2.0f - max - min) else d / (max + min))
            when (max) {
                r -> h = (g - b) / d + (if (g < b) 6.0f else 0.0f)
                g -> h = (b - r) / d + 2.0f
                b -> h = (r - g) / d + 4.0f
            }
            h /= 6.0f
        }

        return ROTHSL(h, s, l)
    }

    fun hsl2rgb(color: List<Float>) = hsl2rgb(ROTHSL(color))

    /**
     * Converts an HSL color value to RGB. Expects 0..1 inputs, produces 0..255 outputs.
     * @param {number[]} color
     * @returns {number[]}
     */
    fun hsl2rgb(color: ROTHSL): ROTRGB {
        val l: Float = color.l

        if (color.s == 0.0f) {
            val il = Math.round(l * 255.0f)
            return ROTRGB(il, il, il)
        } else {
            fun hue2rgb(p: Float, q: Float, tin: Float): Float {
                var t = tin
                if (t < 0.0f) t += 1.0f
                if (t > 1.0f) t -= 1.0f
                if (t < 1.0f / 6.0f) return p + (q - p) * 6.0f * t
                if (t < 1.0f / 2.0f) return q
                if (t < 2.0f / 3.0f) return p + (q - p) * (2.0f / 3.0f - t) * 6.0f
                return p
            }

            val s = color.s
            val q = (if (l < 0.5f) l * (1.0f + s) else l + s - l * s)
            val p = 2.0f * l - q
            val r = hue2rgb(p, q, color.h + 1.0f / 3.0f)
            val g = hue2rgb(p, q, color.h)
            val b = hue2rgb(p, q, color.h - 1.0f / 3.0f)
            return ROTRGB(Math.round(r * 255.0f), Math.round(g * 255.0f), Math.round(b * 255.0f))
        }
    }

    fun toRGB(color: List<Int>) = toRGB(ROTRGB(color))

    fun toRGB(color: ROTRGB): String {
        return "rgb(" + _clamp(color.r) + "," + _clamp(color.g) + "," + _clamp(color.b) + ")"
    }

    fun toHex(color: ROTRGB) = toHex(color.toList())

    fun toHex(color: List<Int>): String {
        val parts = ArrayList<String>()
        (0 until 3).mapTo(parts) { _clamp(color[it]).toString(16).lpad('0', 2) }
        return "#${parts.joinToString("")}"
    }

    private fun _clamp(num: Int): Int {
        if (num < 0) {
            return 0
        } else if (num > 255) {
            return 255
        } else {
            return num
        }
    }

    private val _cache = mutableMapOf(
            "black" to listOf(0, 0, 0),
            "navy" to listOf(0, 0, 128),
            "darkblue" to listOf(0, 0, 139),
            "mediumblue" to listOf(0, 0, 205),
            "blue" to listOf(0, 0, 255),
            "darkgreen" to listOf(0, 100, 0),
            "green" to listOf(0, 128, 0),
            "teal" to listOf(0, 128, 128),
            "darkcyan" to listOf(0, 139, 139),
            "deepskyblue" to listOf(0, 191, 255),
            "darkturquoise" to listOf(0, 206, 209),
            "mediumspringgreen" to listOf(0, 250, 154),
            "lime" to listOf(0, 255, 0),
            "springgreen" to listOf(0, 255, 127),
            "aqua" to listOf(0, 255, 255),
            "cyan" to listOf(0, 255, 255),
            "midnightblue" to listOf(25, 25, 112),
            "dodgerblue" to listOf(30, 144, 255),
            "forestgreen" to listOf(34, 139, 34),
            "seagreen" to listOf(46, 139, 87),
            "darkslategray" to listOf(47, 79, 79),
            "darkslategrey" to listOf(47, 79, 79),
            "limegreen" to listOf(50, 205, 50),
            "mediumseagreen" to listOf(60, 179, 113),
            "turquoise" to listOf(64, 224, 208),
            "royalblue" to listOf(65, 105, 225),
            "steelblue" to listOf(70, 130, 180),
            "darkslateblue" to listOf(72, 61, 139),
            "mediumturquoise" to listOf(72, 209, 204),
            "indigo" to listOf(75, 0, 130),
            "darkolivegreen" to listOf(85, 107, 47),
            "cadetblue" to listOf(95, 158, 160),
            "cornflowerblue" to listOf(100, 149, 237),
            "mediumaquamarine" to listOf(102, 205, 170),
            "dimgray" to listOf(105, 105, 105),
            "dimgrey" to listOf(105, 105, 105),
            "slateblue" to listOf(106, 90, 205),
            "olivedrab" to listOf(107, 142, 35),
            "slategray" to listOf(112, 128, 144),
            "slategrey" to listOf(112, 128, 144),
            "lightslategray" to listOf(119, 136, 153),
            "lightslategrey" to listOf(119, 136, 153),
            "mediumslateblue" to listOf(123, 104, 238),
            "lawngreen" to listOf(124, 252, 0),
            "chartreuse" to listOf(127, 255, 0),
            "aquamarine" to listOf(127, 255, 212),
            "maroon" to listOf(128, 0, 0),
            "purple" to listOf(128, 0, 128),
            "olive" to listOf(128, 128, 0),
            "gray" to listOf(128, 128, 128),
            "grey" to listOf(128, 128, 128),
            "skyblue" to listOf(135, 206, 235),
            "lightskyblue" to listOf(135, 206, 250),
            "blueviolet" to listOf(138, 43, 226),
            "darkred" to listOf(139, 0, 0),
            "darkmagenta" to listOf(139, 0, 139),
            "saddlebrown" to listOf(139, 69, 19),
            "darkseagreen" to listOf(143, 188, 143),
            "lightgreen" to listOf(144, 238, 144),
            "mediumpurple" to listOf(147, 112, 216),
            "darkviolet" to listOf(148, 0, 211),
            "palegreen" to listOf(152, 251, 152),
            "darkorchid" to listOf(153, 50, 204),
            "yellowgreen" to listOf(154, 205, 50),
            "sienna" to listOf(160, 82, 45),
            "brown" to listOf(165, 42, 42),
            "darkgray" to listOf(169, 169, 169),
            "darkgrey" to listOf(169, 169, 169),
            "lightblue" to listOf(173, 216, 230),
            "greenyellow" to listOf(173, 255, 47),
            "paleturquoise" to listOf(175, 238, 238),
            "lightsteelblue" to listOf(176, 196, 222),
            "powderblue" to listOf(176, 224, 230),
            "firebrick" to listOf(178, 34, 34),
            "darkgoldenrod" to listOf(184, 134, 11),
            "mediumorchid" to listOf(186, 85, 211),
            "rosybrown" to listOf(188, 143, 143),
            "darkkhaki" to listOf(189, 183, 107),
            "silver" to listOf(192, 192, 192),
            "mediumvioletred" to listOf(199, 21, 133),
            "indianred" to listOf(205, 92, 92),
            "peru" to listOf(205, 133, 63),
            "chocolate" to listOf(210, 105, 30),
            "tan" to listOf(210, 180, 140),
            "lightgray" to listOf(211, 211, 211),
            "lightgrey" to listOf(211, 211, 211),
            "palevioletred" to listOf(216, 112, 147),
            "thistle" to listOf(216, 191, 216),
            "orchid" to listOf(218, 112, 214),
            "goldenrod" to listOf(218, 165, 32),
            "crimson" to listOf(220, 20, 60),
            "gainsboro" to listOf(220, 220, 220),
            "plum" to listOf(221, 160, 221),
            "burlywood" to listOf(222, 184, 135),
            "lightcyan" to listOf(224, 255, 255),
            "lavender" to listOf(230, 230, 250),
            "darksalmon" to listOf(233, 150, 122),
            "violet" to listOf(238, 130, 238),
            "palegoldenrod" to listOf(238, 232, 170),
            "lightcoral" to listOf(240, 128, 128),
            "khaki" to listOf(240, 230, 140),
            "aliceblue" to listOf(240, 248, 255),
            "honeydew" to listOf(240, 255, 240),
            "azure" to listOf(240, 255, 255),
            "sandybrown" to listOf(244, 164, 96),
            "wheat" to listOf(245, 222, 179),
            "beige" to listOf(245, 245, 220),
            "whitesmoke" to listOf(245, 245, 245),
            "mintcream" to listOf(245, 255, 250),
            "ghostwhite" to listOf(248, 248, 255),
            "salmon" to listOf(250, 128, 114),
            "antiquewhite" to listOf(250, 235, 215),
            "linen" to listOf(250, 240, 230),
            "lightgoldenrodyellow" to listOf(250, 250, 210),
            "oldlace" to listOf(253, 245, 230),
            "red" to listOf(255, 0, 0),
            "fuchsia" to listOf(255, 0, 255),
            "magenta" to listOf(255, 0, 255),
            "deeppink" to listOf(255, 20, 147),
            "orangered" to listOf(255, 69, 0),
            "tomato" to listOf(255, 99, 71),
            "hotpink" to listOf(255, 105, 180),
            "coral" to listOf(255, 127, 80),
            "darkorange" to listOf(255, 140, 0),
            "lightsalmon" to listOf(255, 160, 122),
            "orange" to listOf(255, 165, 0),
            "lightpink" to listOf(255, 182, 193),
            "pink" to listOf(255, 192, 203),
            "gold" to listOf(255, 215, 0),
            "peachpuff" to listOf(255, 218, 185),
            "navajowhite" to listOf(255, 222, 173),
            "moccasin" to listOf(255, 228, 181),
            "bisque" to listOf(255, 228, 196),
            "mistyrose" to listOf(255, 228, 225),
            "blanchedalmond" to listOf(255, 235, 205),
            "papayawhip" to listOf(255, 239, 213),
            "lavenderblush" to listOf(255, 240, 245),
            "seashell" to listOf(255, 245, 238),
            "cornsilk" to listOf(255, 248, 220),
            "lemonchiffon" to listOf(255, 250, 205),
            "floralwhite" to listOf(255, 250, 240),
            "snow" to listOf(255, 250, 250),
            "yellow" to listOf(255, 255, 0),
            "lightyellow" to listOf(255, 255, 224),
            "ivory" to listOf(255, 255, 240),
            "white" to listOf(255, 255, 255)
    )
}