package net.casual.stat

import kotlin.time.Duration

interface FormattedStat {
    fun value(): Any

    fun formatted(): String

    companion object {
        fun of(value: Any): FormattedStat {
            return when (value) {
                is Boolean -> bool(value)
                is Int -> int32(value)
                is Long -> int64(value)
                is Float -> float32(value)
                is Double -> float64(value)
                else -> of(value, value.toString())
            }
        }

        fun of(value: Any, formatted: String): FormattedStat {
            return object: FormattedStat {
                override fun value() = value
                override fun formatted() = formatted
            }
        }

        fun bool(value: Boolean): FormattedStat {
            return of(value, if (value) "Yes" else "No" )
        }

        fun int32(value: Int): FormattedStat {
            return of(value, "%,d".format(value))
        }

        fun int64(value: Long): FormattedStat {
            return of(value, "%,d".format(value))
        }

        fun float32(value: Float): FormattedStat {
            return of(value, "%,.2f".format(value))
        }

        fun float64(value: Double): FormattedStat {
            return of(value, "%,.2f".format(value))
        }

        fun <A, B: Any> fromScoreboard(
            scoreboard: List<Pair<A, B>>,
            mapper: (B) -> FormattedStat = ::of
        ): List<Pair<A, FormattedStat>> {
            return scoreboard.map { it.first to mapper(it.second) }
        }
    }
}