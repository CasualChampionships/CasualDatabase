package net.casual.stat

import kotlin.time.Duration

interface FormattedStat {
    val name: String

    val value: Any
    val formattedValue: String

    companion object {
        fun bool(name: String, value: Boolean): FormattedStat = BooleanStat(name, value)
        fun int32(name: String, value: Int): FormattedStat = IntStat(name, value)
        fun int64(name: String, value: Long): FormattedStat = LongStat(name, value)
        fun float32(name: String, value: Float): FormattedStat = FloatStat(name, value)
        fun float64(name: String, value: Double): FormattedStat = DoubleStat(name, value)
        fun duration(name: String, value: Duration): FormattedStat = DurationStat(name, value)
    }

    private class BooleanStat(override val name: String, override val value: Boolean): FormattedStat {
        override val formattedValue: String
            get() = if (value) "Yes" else "No"
    }

    private class IntStat(override val name: String, override val value: Int): FormattedStat {
        override val formattedValue: String
            get() = "%,d".format(value)
    }

    private class LongStat(override val name: String, override val value: Long): FormattedStat {
        override val formattedValue: String
            get() = "%,d".format(value)
    }

    private class FloatStat(override val name: String, override val value: Float): FormattedStat {
        override val formattedValue: String
            get() = "%,.2f".format(value)
    }

    private class DoubleStat(override val name: String, override val value: Double): FormattedStat {
        override val formattedValue: String
            get() = "%,.2f".format(value)
    }

    private class DurationStat(override val name: String, override val value: Duration): FormattedStat {
        override val formattedValue: String
            get() = value.toString()

    }
}