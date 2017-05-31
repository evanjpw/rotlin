package us.cornus.rotlin

/**
 * Created by ejw on 5/28/17.
 */

private typealias DirectionalConstants = Array<Array<Int>>

object ROT {

    /**
     * @returns {bool} Is rot.js supported by this browser?
     */
    fun isSupported() : Boolean {
        //return !!(document.createElement("canvas").getContext && Function.prototype.bind)
        return true
    }

    /** Default with for display and map generators */
    val DEFAULT_WIDTH =  80
    /** Default height for display and map generators */
    val DEFAULT_HEIGHT =  25

    /** Directional constants. Ordering is important! */
    private val dirs_4 = arrayOf(
            arrayOf( 0, -1),
            arrayOf(1,  0),
            arrayOf(0,  1),
            arrayOf(-1,  0)
    )

    private val dirs_8 = arrayOf(
            arrayOf(0, -1),
            arrayOf(1, -1),
            arrayOf(1,  0),
            arrayOf(1,  1),
            arrayOf(0,  1),
            arrayOf(-1,  1),
            arrayOf(-1,  0),
            arrayOf(-1, -1)
    )

    private val dirs_6 = arrayOf(
            arrayOf(-1, -1),
            arrayOf(1, -1),
            arrayOf(2,  0),
            arrayOf(1,  1),
            arrayOf(-1,  1),
            arrayOf(-2,  0)
    )

    val DIRS = mapOf(
            "4" to dirs_4,
            "8" to dirs_8,
            "6" to dirs_6
    )

    /** Cancel key. */
    val VK_CANCEL = 3
    /** Help key. */
    val VK_HELP = 6
    /** Backspace key. */
    val VK_BACK_SPACE = 8
    /** Tab key. */
    val VK_TAB = 9
    /** 5 key on Numpad when NumLock is unlocked. Or on Mac, clear key which is positioned at NumLock key. */
    val VK_CLEAR = 12
    /** Return/enter key on the main keyboard. */
    val VK_RETURN = 13
    /** Reserved, but not used. */
    val VK_ENTER = 14
    /** Shift key. */
    val VK_SHIFT = 16
    /** Control key. */
    val VK_CONTROL = 17
    /** Alt (Option on Mac) key. */
    val VK_ALT = 18
    /** Pause key. */
    val VK_PAUSE = 19
    /** Caps lock. */
    val VK_CAPS_LOCK = 20
    /** Escape key. */
    val VK_ESCAPE = 27
    /** Space bar. */
    val VK_SPACE = 32
    /** Page Up key. */
    val VK_PAGE_UP = 33
    /** Page Down key. */
    val VK_PAGE_DOWN = 34
    /** End key. */
    val VK_END = 35
    /** Home key. */
    val VK_HOME = 36
    /** Left arrow. */
    val VK_LEFT = 37
    /** Up arrow. */
    val VK_UP = 38
    /** Right arrow. */
    val VK_RIGHT = 39
    /** Down arrow. */
    val VK_DOWN = 40
    /** Print Screen key. */
    val VK_PRINTSCREEN = 44
    /** Ins(ert) key. */
    val VK_INSERT = 45
    /** Del(ete) key. */
    val VK_DELETE = 46
    /***/
    val VK_0 = 48
    /***/
    val VK_1 = 49
    /***/
    val VK_2 = 50
    /***/
    val VK_3 = 51
    /***/
    val VK_4 = 52
    /***/
    val VK_5 = 53
    /***/
    val VK_6 = 54
    /***/
    val VK_7 = 55
    /***/
    val VK_8 = 56
    /***/
    val VK_9 = 57
    /** Colon (:) key. Requires Gecko 15.0 */
    val VK_COLON = 58
    /** Semicolon (;) key. */
    val VK_SEMICOLON = 59
    /** Less-than (<) key. Requires Gecko 15.0 */
    val VK_LESS_THAN = 60
    /** Equals (=) key. */
    val VK_EQUALS = 61
    /** Greater-than (>) key. Requires Gecko 15.0 */
    val VK_GREATER_THAN = 62
    /** Question mark (?) key. Requires Gecko 15.0 */
    val VK_QUESTION_MARK = 63
    /** Atmark (@) key. Requires Gecko 15.0 */
    val VK_AT = 64
    /***/
    val VK_A = 65
    /***/
    val VK_B = 66
    /***/
    val VK_C = 67
    /***/
    val VK_D = 68
    /***/
    val VK_E = 69
    /***/
    val VK_F = 70
    /***/
    val VK_G = 71
    /***/
    val VK_H = 72
    /***/
    val VK_I = 73
    /***/
    val VK_J = 74
    /***/
    val VK_K = 75
    /***/
    val VK_L = 76
    /***/
    val VK_M = 77
    /***/
    val VK_N = 78
    /***/
    val VK_O = 79
    /***/
    val VK_P = 80
    /***/
    val VK_Q = 81
    /***/
    val VK_R = 82
    /***/
    val VK_S = 83
    /***/
    val VK_T = 84
    /***/
    val VK_U = 85
    /***/
    val VK_V = 86
    /***/
    val VK_W = 87
    /***/
    val VK_X = 88
    /***/
    val VK_Y = 89
    /***/
    val VK_Z = 90
    /***/
    val VK_CONTEXT_MENU = 93
    /** 0 on the numeric keypad. */
    val VK_NUMPAD0 = 96
    /** 1 on the numeric keypad. */
    val VK_NUMPAD1 = 97
    /** 2 on the numeric keypad. */
    val VK_NUMPAD2 = 98
    /** 3 on the numeric keypad. */
    val VK_NUMPAD3 = 99
    /** 4 on the numeric keypad. */
    val VK_NUMPAD4 = 100
    /** 5 on the numeric keypad. */
    val VK_NUMPAD5 = 101
    /** 6 on the numeric keypad. */
    val VK_NUMPAD6 = 102
    /** 7 on the numeric keypad. */
    val VK_NUMPAD7 = 103
    /** 8 on the numeric keypad. */
    val VK_NUMPAD8 = 104
    /** 9 on the numeric keypad. */
    val VK_NUMPAD9 = 105
    /** * on the numeric keypad. */
    val VK_MULTIPLY = 106
    /** + on the numeric keypad. */
    val VK_ADD = 107
    /***/
    val VK_SEPARATOR = 108
    /** - on the numeric keypad. */
    val VK_SUBTRACT = 109
    /** Decimal point on the numeric keypad. */
    val VK_DECIMAL = 110
    /** / on the numeric keypad. */
    val VK_DIVIDE = 111
    /** F1 key. */
    val VK_F1 = 112
    /** F2 key. */
    val VK_F2 = 113
    /** F3 key. */
    val VK_F3 = 114
    /** F4 key. */
    val VK_F4 = 115
    /** F5 key. */
    val VK_F5 = 116
    /** F6 key. */
    val VK_F6 = 117
    /** F7 key. */
    val VK_F7 = 118
    /** F8 key. */
    val VK_F8 = 119
    /** F9 key. */
    val VK_F9 = 120
    /** F10 key. */
    val VK_F10 = 121
    /** F11 key. */
    val VK_F11 = 122
    /** F12 key. */
    val VK_F12 = 123
    /** F13 key. */
    val VK_F13 = 124
    /** F14 key. */
    val VK_F14 = 125
    /** F15 key. */
    val VK_F15 = 126
    /** F16 key. */
    val VK_F16 = 127
    /** F17 key. */
    val VK_F17 = 128
    /** F18 key. */
    val VK_F18 = 129
    /** F19 key. */
    val VK_F19 = 130
    /** F20 key. */
    val VK_F20 = 131
    /** F21 key. */
    val VK_F21 = 132
    /** F22 key. */
    val VK_F22 = 133
    /** F23 key. */
    val VK_F23 = 134
    /** F24 key. */
    val VK_F24 = 135
    /** Num Lock key. */
    val VK_NUM_LOCK = 144
    /** Scroll Lock key. */
    val VK_SCROLL_LOCK = 145
    /** Circumflex (^) key. Requires Gecko 15.0 */
    val VK_CIRCUMFLEX = 160
    /** Exclamation (!) key. Requires Gecko 15.0 */
    val VK_EXCLAMATION = 161
    /** Double quote () key. Requires Gecko 15.0 */
    val VK_DOUBLE_QUOTE = 162
    /** Hash (#) key. Requires Gecko 15.0 */
    val VK_HASH = 163
    /** Dollar sign ($) key. Requires Gecko 15.0 */
    val VK_DOLLAR = 164
    /** Percent (%) key. Requires Gecko 15.0 */
    val VK_PERCENT = 165
    /** Ampersand (&) key. Requires Gecko 15.0 */
    val VK_AMPERSAND = 166
    /** Underscore (_) key. Requires Gecko 15.0 */
    val VK_UNDERSCORE = 167
    /** Open parenthesis (() key. Requires Gecko 15.0 */
    val VK_OPEN_PAREN = 168
    /** Close parenthesis ()) key. Requires Gecko 15.0 */
    val VK_CLOSE_PAREN = 169
    /* Asterisk (*) key. Requires Gecko 15.0 */
    val VK_ASTERISK = 170
    /** Plus (+) key. Requires Gecko 15.0 */
    val VK_PLUS = 171
    /** Pipe (|) key. Requires Gecko 15.0 */
    val VK_PIPE = 172
    /** Hyphen-US/docs/Minus (-) key. Requires Gecko 15.0 */
    val VK_HYPHEN_MINUS = 173
    /** Open curly bracket ({) key. Requires Gecko 15.0 */
    val VK_OPEN_CURLY_BRACKET = 174
    /** Close curly bracket (}) key. Requires Gecko 15.0 */
    val VK_CLOSE_CURLY_BRACKET = 175
    /** Tilde (~) key. Requires Gecko 15.0 */
    val VK_TILDE = 176
    /** Comma (,) key. */
    val VK_COMMA = 188
    /** Period (.) key. */
    val VK_PERIOD = 190
    /** Slash (/) key. */
    val VK_SLASH = 191
    /** Back tick (`) key. */
    val VK_BACK_QUOTE = 192
    /** Open square bracket ([) key. */
    val VK_OPEN_BRACKET = 219
    /** Back slash (\) key. */
    val VK_BACK_SLASH = 220
    /** Close square bracket (]) key. */
    val VK_CLOSE_BRACKET = 221
    /** Quote (''') key. */
    val VK_QUOTE = 222
    /** Meta key on Linux, Command key on Mac. */
    val VK_META = 224
    /** AltGr key on Linux. Requires Gecko 15.0 */
    val VK_ALTGR = 225
    /** Windows logo key on Windows. Or Super or Hyper key on Linux. Requires Gecko 15.0 */
    val VK_WIN = 91
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_KANA = 21
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_HANGUL = 21
    /** 英数 key on Japanese Mac keyboard. Requires Gecko 15.0 */
    val VK_EISU = 22
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_JUNJA = 23
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_FINAL = 24
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_HANJA = 25
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_KANJI = 25
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_CONVERT = 28
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_NONCONVERT = 29
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_ACCEPT = 30
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_MODECHANGE = 31
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_SELECT = 41
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_PRINT = 42
    /** Linux support for this keycode was added in Gecko 4.0. */
    val VK_EXECUTE = 43
    /** Linux support for this keycode was added in Gecko 4.0.	 */
    val VK_SLEEP = 9
}

