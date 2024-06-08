package net.casual.database.stats

import net.casual.database.Minigame
import net.casual.database.MinigamePlayer
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.kotlin.datetime.duration
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.sum
import java.util.*

object UHCMinigameStats: MinigameStats() {
    val won = bool("won")
    val died = bool("died")
    val kills = integer("kills")
    val damageTaken = float("damage_taken")
    val damageHealed = float("damage_healed")
    val damageDealt = float("damage_dealt")
    val headsConsumed = integer("heads_consumed")
    val aliveTime = duration("alive_time")
    val crouchTime = duration("crouch_time")

    val jumps = integer("jumps")
    val relogs = integer("relogs")
    val blocksMined = integer("blocks_mined")
    val blocksPlaced = integer("blocks_placed")

    fun lifetimeWins(uuid: UUID) = lifetime(uuid, won.count(), won eq true).firstOrNull() ?: 0L
    fun lifetimeWinsScoreboard(limit: Int = 10) = lifetimeScoreboard(won.count(), won eq true, limit = limit)

    fun lifetimeDeaths(uuid: UUID) = lifetime(uuid, died.count(), died eq true).firstOrNull() ?: 0L
    fun lifetimeDeathsScoreboard(limit: Int = 10) = lifetimeScoreboard(died.count(), died eq true, limit = limit)

    fun lifetimeKills(uuid: UUID) = lifetime(uuid, kills.sum()).firstOrNull() ?: 0
    fun lifetimeKillsScoreboard(limit: Int = 10) = lifetimeScoreboard(kills.sum(), limit = limit)
    fun lifetimeMostKillsScoreboard(limit: Int = 10) = lifetimeScoreboard(kills.max(), limit = limit)
    fun killsScoreboard(minigame: Minigame, limit: Int = 10) = scoreboard(minigame, kills.max(), limit = limit)

    fun lifetimeDamageTaken(uuid: UUID) = lifetime(uuid, damageTaken.sum()).firstOrNull() ?: 0F
    fun lifetimeDamageTakenScoreboard(limit: Int = 10) = lifetimeScoreboard(damageTaken.sum(), limit = limit)
    fun lifetimeMostDamageTakenScoreboard(limit: Int = 10) = lifetimeScoreboard(damageTaken.max(), limit = limit)
    fun damageTakenScoreboard(minigame: Minigame, limit: Int = 10) = scoreboard(minigame, damageTaken.max(), limit = limit)

    fun lifetimeDamageHealed(uuid: UUID) = lifetime(uuid, damageHealed.sum()).firstOrNull() ?: 0F
    fun lifetimeDamageHealedScoreboard(limit: Int = 10) = lifetimeScoreboard(damageHealed.sum(), limit = limit)
    fun lifetimeMostDamageHealedScoreboard(limit: Int = 10) = lifetimeScoreboard(damageHealed.max(), limit = limit)
    fun damageHealedScoreboard(minigame: Minigame, limit: Int = 10) = scoreboard(minigame, damageHealed.max(), limit = limit)

    fun lifetimeDamageDealt(uuid: UUID) = lifetime(uuid, damageDealt.sum()).firstOrNull() ?: 0F
    fun lifetimeDamageDealtScoreboard(limit: Int = 10) = lifetimeScoreboard(damageDealt.sum(), limit = limit)
    fun lifetimeMostDamageDealtScoreboard(limit: Int = 10) = lifetimeScoreboard(damageDealt.max(), limit = limit)
    fun damageDealtScoreboard(minigame: Minigame, limit: Int = 10) = scoreboard(minigame, damageDealt.max(), limit = limit)

    fun lifetimeHeadsConsumed(uuid: UUID) = lifetime(uuid, headsConsumed.sum()).firstOrNull() ?: 0
    fun lifetimeHeadsConsumedScoreboard(limit: Int = 10) = lifetimeScoreboard(headsConsumed.sum(), limit = limit)
    fun lifetimeMostHeadsConsumedScoreboard(limit: Int = 10) = lifetimeScoreboard(headsConsumed.max(), limit = limit)
    fun headsConsumedScoreboard(minigame: Minigame, limit: Int = 10) = scoreboard(minigame, headsConsumed.max(), limit = limit)

    fun lifetimeAliveTime(uuid: UUID) = lifetime(uuid, aliveTime.sum()).firstOrNull() ?: 0L
    fun lifetimeAliveTimeScoreboard(limit: Int = 10) = lifetimeScoreboard(aliveTime.sum(), limit = limit)
    fun lifetimeMostAliveTimeScoreboard(limit: Int = 10) = lifetimeScoreboard(aliveTime.max(), limit = limit)
    fun aliveTimeScoreboard(minigame: Minigame, limit: Int = 10) = scoreboard(minigame, aliveTime.max(), limit = limit)

    fun lifetimeCrouchTime(uuid: UUID) = lifetime(uuid, crouchTime.sum()).firstOrNull() ?: 0L
    fun lifetimeCrouchTimeScoreboard(limit: Int = 10) = lifetimeScoreboard(crouchTime.sum(), limit = limit)
    fun lifetimeMostCrouchTimeScoreboard(limit: Int = 10) = lifetimeScoreboard(crouchTime.max(), limit = limit)
    fun crouchTimeScoreboard(minigame: Minigame, limit: Int = 10) = scoreboard(minigame, crouchTime.max(), limit = limit)

    fun lifetimeJumps(uuid: UUID) = lifetime(uuid, jumps.sum()).firstOrNull() ?: 0
    fun lifetimeJumpsScoreboard(limit: Int = 10) = lifetimeScoreboard(jumps.sum(), limit = limit)
    fun lifetimeMostJumpsScoreboard(limit: Int = 10) = lifetimeScoreboard(jumps.max(), limit = limit)
    fun jumpsScoreboard(minigame: Minigame, limit: Int = 10) = scoreboard(minigame, jumps.max(), limit = limit)

    fun lifetimeRelogs(uuid: UUID) = lifetime(uuid, relogs.sum()).firstOrNull() ?: 0
    fun lifetimeRelogsScoreboard(limit: Int = 10) = lifetimeScoreboard(relogs.sum(), limit = limit)
    fun lifetimeMostRelogsScoreboard(limit: Int = 10) = lifetimeScoreboard(relogs.max(), limit = limit)
    fun relogsScoreboard(minigame: Minigame, limit: Int = 10) = scoreboard(minigame, relogs.max(), limit = limit)

    fun lifetimeBlocksMined(uuid: UUID) = lifetime(uuid, blocksMined.sum()).firstOrNull() ?: 0
    fun lifetimeBlocksMinedScoreboard(limit: Int = 10) = lifetimeScoreboard(blocksMined.sum(), limit = limit)
    fun lifetimeMostBlocksMinedScoreboard(limit: Int = 10) = lifetimeScoreboard(blocksMined.max(), limit = limit)
    fun blocksMinedScoreboard(minigame: Minigame, limit: Int = 10) = scoreboard(minigame, blocksMined.max(), limit = limit)

    fun lifetimeBlocksPlaced(uuid: UUID) = lifetime(uuid, blocksPlaced.sum()).firstOrNull() ?: 0
    fun lifetimeBlocksPlacedScoreboard(limit: Int = 10) = lifetimeScoreboard(blocksPlaced.sum(), limit = limit)
    fun lifetimeMostBlocksPlacedScoreboard(limit: Int = 10) = lifetimeScoreboard(blocksPlaced.max(), limit = limit)
    fun blocksPlacedScoreboard(minigame: Minigame, limit: Int = 10) = scoreboard(minigame, blocksPlaced.max(), limit = limit)
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