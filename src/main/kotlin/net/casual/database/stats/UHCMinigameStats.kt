package net.casual.database.stats

import net.casual.database.MinigamePlayer
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.kotlin.datetime.duration
import kotlin.time.Duration.Companion.seconds

object UHCMinigameStats: MinigameStats() {
    val won = bool("won").default(false)
    val died = bool("died").default(false)
    val kills = integer("kills").default(0)
    val damageTaken = float("damage_taken").default(0.0F)
    val damageHealed = float("damage_healed").default(0.0F)
    val damageDealt = float("damage_dealt").default(0.0F)
    val headsConsumed = integer("heads_consumed").default(0)
    val aliveTime = duration("alive_time").default(0.seconds)
    val crouchTime = duration("crouch_time").default(0.seconds)

    val jumps = integer("jumps").default(0)
    val relogs = integer("relogs").default(0)
    val blocksMined = integer("blocks_mined").default(0)
    val blocksPlaced = integer("blocks_placed").default(0)
}

class UHCPlayerStats(id: EntityID<Int>): PlayerStats(id) {
    var player by MinigamePlayer referencedOn UHCMinigameStats.id

    var won by UHCMinigameStats.won
    var died by UHCMinigameStats.died
    var kills by UHCMinigameStats.kills
    var damageTaken by UHCMinigameStats.damageTaken
    var damageHealed by UHCMinigameStats.damageHealed
    var damageDealt by UHCMinigameStats.damageDealt
    var headsConsumed by UHCMinigameStats.headsConsumed
    var aliveTime by UHCMinigameStats.aliveTime
    var crouchTime by UHCMinigameStats.crouchTime

    var jumps by UHCMinigameStats.jumps
    var relogs by UHCMinigameStats.relogs
    var blocksMined by UHCMinigameStats.blocksMined
    var blocksPlaced by UHCMinigameStats.blocksPlaced

    companion object: IntEntityClass<UHCPlayerStats>(UHCMinigameStats)
}