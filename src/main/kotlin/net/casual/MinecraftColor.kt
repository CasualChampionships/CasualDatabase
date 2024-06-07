package net.casual

enum class MinecraftColor(val formatted: String, val code: Int) {
    BLACK("Black", 0x000000),
    DARK_BLUE("Dark Blue", 0x0000AA),
    DARK_GREEN("Dark Green", 0x00AA00),
    DARK_AQUA("Dark Aqua", 0x00AAAA),
    DARK_RED("Dark Red", 0xAA0000),
    DARK_PURPLE("Dark Purple", 0xAA00AA),
    GOLD("Gold", 0xFFAA00),
    GRAY("Gray", 0xAAAAAA),
    DARK_GRAY("Dark Gray", 0x555555),
    BLUE("Blue", 0x5555FF),
    GREEN("Green", 0x55FF55),
    AQUA("Aqua", 0x55FFFF),
    RED("Red", 0xFF5555),
    LIGHT_PURPLE("Light Purple", 0xFF55FF),
    YELLOW("Yellow", 0xFFFF55),
    WHITE("White", 0xFFFFFF);
}