package net.casual.database.stats

import net.casual.database.MinigamePlayer
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

object DuelMinigameStats: MinigameStats() {
    val won = bool("won").default(false)
    val kills = integer("kills").default(0)
    val damageTaken = float("damage_taken").default(0.0F)
    val damageHealed = float("damage_healed").default(0.0F)
    val damageDealt = float("damage_dealt").default(0.0F)
}

class DuelPlayerStats(id: EntityID<Int>): PlayerStats(id) {
    var player by MinigamePlayer referencedOn DuelMinigameStats.id

    var won by DuelMinigameStats.won
    var kills by DuelMinigameStats.kills
    var damageTaken by DuelMinigameStats.damageTaken
    var damageHealed by DuelMinigameStats.damageHealed
    var damageDealt by DuelMinigameStats.damageDealt

    companion object: IntEntityClass<DuelPlayerStats>(DuelMinigameStats)
}