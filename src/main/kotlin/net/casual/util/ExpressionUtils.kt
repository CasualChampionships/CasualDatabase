package net.casual.util

import org.jetbrains.exposed.v1.core.*

fun Expression<Boolean>.sum(): Sum<Int> {
    return Case().When(this, intLiteral(1)).Else(intLiteral(0)).sum()
}