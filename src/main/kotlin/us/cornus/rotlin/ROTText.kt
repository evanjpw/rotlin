package us.cornus.rotlin

/**
 * Created by ejw on 5/29/17.
 */

data class Dimension(val width : Int, val height : Int)

enum class TokenType {
    TEXT,       //0
    NEWLINE,    //1
    FG,         //2
    BG          //3
}

data class Token(val type : TokenType, var value : String = "")

/**
 * @namespace
 * Contains text tokenization and breaking routines
 */

object ROTText {
    val RE_COLORS = Regex("%([bc])\\{([^}]*)}") // was written with the 'g' flag following

    /* token types */
    val TYPE_TEXT = TokenType.TEXT
    val TYPE_NEWLINE = TokenType.NEWLINE
    val TYPE_FG = TokenType.FG
    val TYPE_BG = TokenType.BG

    init {
        assert(TYPE_TEXT.ordinal == 0)
        assert(TYPE_NEWLINE.ordinal == 1)
        assert(TYPE_FG.ordinal == 2)
        assert(TYPE_BG.ordinal == 3)
    }

    /**
     * Measure size of a resulting text block
     */
    fun measure(str: String, maxWidth: Int = Int.MAX_VALUE): Dimension {
        val tokens = tokenize(str, maxWidth)
        var width = 0
        var height = 1
        var lineWidth = 0

        tokens.indices
                .asSequence()
                .map { tokens[it] }
                .forEach {
                    when (it.type) {
                        TYPE_TEXT -> lineWidth += it.value.length

                        TYPE_NEWLINE -> {
                            height++
                            width = Math.max(width, lineWidth)
                            lineWidth = 0
                        }
                        else -> {
                            //Don't do anything
                        }
                    }
                }
        width = Math.max(width, lineWidth)

        return Dimension(width = width, height = height) //result
    }

    /**
     * Convert string to a series of a formatting commands
     */
    fun tokenize(istr: String, maxWidth: Int): ArrayList<Token> {
        val result = ArrayList<Token>()

        /* first tokenization pass - split texts and color formatting commands */
        var offset = 0
        val nstr = istr.replace(RE_COLORS) { m ->
            //val match = m.value
            val type = m.groupValues[1]
            val name = m.groupValues[2]
            val index = m.range.start

            //System.out.println("tokenize() -> replace lambda: istr = \"$istr\"\nmatch = \"$match\", type = '$type'\nname = \"$name\", index = $index, offset = $offset")

            /* string before */
            val part = istr.substring(offset, index)
            if (part.isNotEmpty()) {
                result.add(Token(
                        type = TYPE_TEXT,
                        value = part
                ))
            }

            /* color command */
            result.add(Token(
                    type = (if (type == "c") TYPE_FG else TYPE_BG),
                    value = name.trim()
            ))

            offset = index //+ match.length
            ""
        }

        //System.out.println("tokenize(): nstr = \"$nstr\"\noffset = $offset")
        /* last remaining part */
        val part = nstr.substring(offset)
        if (part.isNotEmpty()) {
            result.add(Token(
                    type = TYPE_TEXT,
                    value = part
            ))
        }

        return _breakLines(result, maxWidth)
    }

    /* insert line breaks into first-pass tokenized data */
    private fun _breakLines(tokens: ArrayList<Token>, iMaxWidth: Int): ArrayList<Token> {
        val maxWidth = if (iMaxWidth == 0) {
            Int.MAX_VALUE
        } else iMaxWidth

        var i = 0
        var lineLength = 0
        var lastTokenWithSpace = -1

        while (i < tokens.size) { /* take all text tokens, remove space, apply linebreaks */
            val token = tokens[i]
            if (token.type == TYPE_NEWLINE) { /* reset */
                lineLength = 0
                lastTokenWithSpace = -1
            }
            if (token.type != TYPE_TEXT) { /* skip non-text tokens */
                i++
                continue
            }

            /* remove spaces at the beginning of line */
            while (lineLength == 0 && token.value[0] == ' ') {
                token.value = token.value.substring(1)
            }

            /* forced newline? insert two new tokens after this one */
            val index = token.value.indexOf("\n")
            if (index != -1) {
                token.value = _breakInsideToken(tokens, i, index, true)

                /* if there are spaces at the end, we must remove them (we do not want the line too long) */
                val sb: StringBuilder = StringBuilder(token.value)
                while (sb.isNotEmpty() && sb[sb.length - 1] == ' ') {
                    sb.deleteCharAt(sb.length - 1)
                }
                token.value = sb.toString()
            }

            /* token degenerated? */
            if (token.value.isEmpty()) {
                tokens.removeAt(i)
                continue
            }

            if (lineLength + token.value.length > maxWidth) { /* line too long, find a suitable breaking spot */

                /* is it possible to break within this token? */
                var index1 = -1
                while (true) {
                    val nextIndex = token.value.indexOf(" ", index1 + 1)
                    if (nextIndex == -1) {
                        break; }
                    if (lineLength + nextIndex > maxWidth) {
                        break
                    }
                    index1 = nextIndex
                }

                if (index1 != -1) { /* break at space within this one */
                    token.value = this._breakInsideToken(tokens, i, index1, true)
                } else if (lastTokenWithSpace != -1) { /* is there a previous token where a break can occur? */
                    val token1 = tokens[lastTokenWithSpace]
                    val breakIndex = token1.value.lastIndexOf(" ")
                    token1.value = this._breakInsideToken(tokens, lastTokenWithSpace, breakIndex, true)
                    i = lastTokenWithSpace
                } else { /* force break in this token */
                    token.value = this._breakInsideToken(tokens, i, maxWidth - lineLength, false)
                }

            } else { /* line not long, continue */
                lineLength += token.value.length
                if (token.value.indexOf(" ") != -1) {
                    lastTokenWithSpace = i
                }
            }

            i++ /* advance to next token */
        }


        tokens.add(Token(type = TYPE_NEWLINE)) /* insert fake newline to fix the last text line */

        /* remove trailing space from text tokens before newlines */
        var lastTextToken: Token? = null
        for (j in tokens.indices) {
            val token = tokens[j]
            when (token.type) {
                TYPE_TEXT -> lastTextToken = token
                TYPE_NEWLINE -> {
                    if (lastTextToken != null) { /* remove trailing space */
                        val sb = StringBuilder(lastTextToken.value)
                        while (sb.isNotEmpty() && sb[sb.length - 1] == ' ') {
                            sb.deleteCharAt(sb.length - 1)
                        }
                        lastTextToken.value = sb.toString()
                    }
                    lastTextToken = null
                }
                else -> {
                    // Don't do anything
                }
            }
        }

        tokens.removeAt(tokens.size - 1) /* remove fake token */

        return tokens
    }

    /**
     * Create new tokens and insert them into the stream
     * @param {object[]} tokens
     * @param {int} tokenIndex Token being processed
     * @param {int} breakIndex Index within current token's value
     * @param {bool} removeBreakChar Do we want to remove the breaking character?
     * @returns {string} remaining unbroken token value
     */
    private fun _breakInsideToken(tokens: ArrayList<Token>, tokenIndex: Int, breakIndex: Int, removeBreakChar: Boolean): String {
        val newBreakToken = Token(
                type = TYPE_NEWLINE
        )
        val newTextToken = Token(
                type = TYPE_TEXT,
                value = tokens[tokenIndex].value.substring(breakIndex + (if (removeBreakChar) 1 else 0))
        )
        tokens.addAll(tokenIndex + 1, listOf(newBreakToken, newTextToken))
        return tokens[tokenIndex].value.substring(0, breakIndex)
    }
}