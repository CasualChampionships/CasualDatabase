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

    fun lifetimeWinsScoreboard(limit: Int = 10) = lifetimeScoreboard(won.count(), won eq true, limit = limit)

    fun lifetimeDeathsScoreboard(limit: Int = 10) = lifetimeScoreboard(died.count(), died eq true, limit = limit)

    fun lifetimeKillsScoreboard(limit: Int = 10) = lifetimeScoreboard(kills.sum(), limit = limit)
    fun lifetimeMostKillsScoreboard(limit: Int = 10) = lifetimeScoreboard(kills.max(), limit = limit)
    fun killsScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, kills.sum(), limit = limit)

    fun lifetimeDamageTakenScoreboard(limit: Int = 10) = lifetimeScoreboard(damageTaken.sum(), limit = limit)
    fun lifetimeMostDamageTakenScoreboard(limit: Int = 10) = lifetimeScoreboard(damageTaken.max(), limit = limit)
    fun damageTakenScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, damageTaken.sum(), limit = limit)

    fun lifetimeDamageHealedScoreboard(limit: Int = 10) = lifetimeScoreboard(damageHealed.sum(), limit = limit)
    fun lifetimeMostDamageHealedScoreboard(limit: Int = 10) = lifetimeScoreboard(damageHealed.max(), limit = limit)
    fun damageHealedScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, damageHealed.sum(), limit = limit)

    fun lifetimeDamageDealtScoreboard(limit: Int = 10) = lifetimeScoreboard(damageDealt.sum(), limit = limit)
    fun lifetimeMostDamageDealtScoreboard(limit: Int = 10) = lifetimeScoreboard(damageDealt.max(), limit = limit)
    fun damageDealtScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, damageDealt.sum(), limit = limit)

    fun lifetimeHeadsConsumedScoreboard(limit: Int = 10) = lifetimeScoreboard(headsConsumed.sum(), limit = limit)
    fun lifetimeMostHeadsConsumedScoreboard(limit: Int = 10) = lifetimeScoreboard(headsConsumed.max(), limit = limit)
    fun headsConsumedScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, headsConsumed.sum(), limit = limit)

    fun lifetimeAliveTimeScoreboard(limit: Int = 10) = lifetimeScoreboard(aliveTime.sum(), limit = limit)
    fun lifetimeMostAliveTimeScoreboard(limit: Int = 10) = lifetimeScoreboard(aliveTime.max(), limit = limit)
    fun aliveTimeScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, aliveTime.sum(), limit = limit)

    fun lifetimeCrouchTimeScoreboard(limit: Int = 10) = lifetimeScoreboard(crouchTime.sum(), limit = limit)
    fun lifetimeMostCrouchTimeScoreboard(limit: Int = 10) = lifetimeScoreboard(crouchTime.max(), limit = limit)
    fun crouchTimeScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, crouchTime.sum(), limit = limit)

    fun lifetimeJumpsScoreboard(limit: Int = 10) = lifetimeScoreboard(jumps.sum(), limit = limit)
    fun lifetimeMostJumpsScoreboard(limit: Int = 10) = lifetimeScoreboard(jumps.max(), limit = limit)
    fun jumpsScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, jumps.sum(), limit = limit)

    fun lifetimeRelogsScoreboard(limit: Int = 10) = lifetimeScoreboard(relogs.sum(), limit = limit)
    fun lifetimeMostRelogsScoreboard(limit: Int = 10) = lifetimeScoreboard(relogs.max(), limit = limit)
    fun relogsScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, relogs.sum(), limit = limit)

    fun lifetimeBlocksMinedScoreboard(limit: Int = 10) = lifetimeScoreboard(blocksMined.sum(), limit = limit)
    fun lifetimeMostBlocksMinedScoreboard(limit: Int = 10) = lifetimeScoreboard(blocksMined.max(), limit = limit)
    fun blocksMinedScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, blocksMined.sum(), limit = limit)

    fun lifetimeBlocksPlacedScoreboard(limit: Int = 10) = lifetimeScoreboard(blocksPlaced.sum(), limit = limit)
    fun lifetimeMostBlocksPlacedScoreboard(limit: Int = 10) = lifetimeScoreboard(blocksPlaced.max(), limit = limit)
    fun blocksPlacedScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, blocksPlaced.sum(), limit = limit)
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