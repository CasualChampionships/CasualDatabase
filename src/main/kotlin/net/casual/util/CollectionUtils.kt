package net.casual.util

fun <A: Any, B: Any> List<Pair<A, B?>>.replaceNullableWithDefault(value: B): List<Pair<A, B>> {
    return this.map { (a, b) -> a to (b ?: value) }
}

fun <A: Any, B: Any> List<Pair<A, B?>>.filterNotNull(): List<Pair<A, B>> {
    return this.mapNotNull { (a, b) -> if (b == null) null else a to b }
}