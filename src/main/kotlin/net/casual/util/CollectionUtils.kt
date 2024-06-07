package net.casual.util

object CollectionUtils {
    fun <A: Any, B: Any> List<Pair<A, B?>>.replaceNullableWithDefault(value: B): List<Pair<A, B>> {
        return this.map { (a, b) -> a to (b ?: value) }
    }
}