package net.casual.stat

import java.util.*

data class ResolvedPlayerStat(
    val uuid: UUID,
    val name: String,
    val stat: FormattedStat
)