package net.casual.stat

import java.util.UUID

data class UnresolvedPlayerStat<T>(
    val uuid: UUID,
    val value: T
) {
    companion object {
        fun <T: Any> Iterable<UnresolvedPlayerStat<T?>>.filterNotNull(): List<UnresolvedPlayerStat<T>> {
            @Suppress("UNCHECKED_CAST")
            return this.filter { it.value != null } as List<UnresolvedPlayerStat<T>>
        }
    }
}