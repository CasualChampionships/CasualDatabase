package net.casual.stat

import java.util.UUID

data class UnresolvedPlayerStat<T: Any>(
    val uuid: UUID,
    val value: T
)