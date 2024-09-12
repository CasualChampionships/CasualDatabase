package net.casual

import java.awt.Color

enum class MinecraftColor(val formatted: String, val color: Color) {
    BLACK("Black",  Color(0x000000)),
    DARK_BLUE("Dark Blue",  Color(0x0000AA)),
    DARK_GREEN("Dark Green",  Color(0x00AA00)),
    DARK_AQUA("Dark Aqua",  Color(0x00AAAA)),
    DARK_RED("Dark Red",  Color(0xAA0000)),
    DARK_PURPLE("Dark Purple",  Color(0xAA00AA)),
    GOLD("Gold",  Color(0xFFAA00)),
    GRAY("Gray",  Color(0xAAAAAA)),
    DARK_GRAY("Dark Gray",  Color(0x555555)),
    BLUE("Blue",  Color(0x5555FF)),
    GREEN("Green",  Color(0x55FF55)),
    AQUA("Aqua",  Color(0x55FFFF)),
    RED("Red",  Color(0xFF5555)),
    LIGHT_PURPLE("Light Purple",  Color(0xFF55FF)),
    YELLOW("Yellow",  Color(0xFFFF55)),
    WHITE("White",  Color(0xFFFFFF));
}