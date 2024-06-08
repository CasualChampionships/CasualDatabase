package net.casual.database.stats

import net.casual.database.Minigame
import net.casual.database.MinigamePlayer
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.max
import org.jetbrains.exposed.sql.sum
import java.util.*

object DuelMinigameStats: MinigameStats() {
    val won = bool("won")
    val kills = integer("kills")
    val damageTaken = float("damage_taken")
    val damageHealed = float("damage_healed")
    val damageDealt = float("damage_dealt")

    fun lifetimeWins(player: UUID) = lifetime(player, won.count(), won eq true).firstOrNull() ?: 0L
    fun lifetimeWinsScoreboard(limit: Int = 10) = lifetimeScoreboard(won.count(), won eq true, limit = limit)

    fun lifetimeKills(player: UUID) = lifetime(player, kills.sum()).firstOrNull() ?: 0
    fun lifetimeKillsScoreboard(limit: Int = 10) = lifetimeScoreboard(kills.sum(), limit = limit)
    fun lifetimeMostKillsScoreboard(limit: Int = 10) = lifetimeScoreboard(kills.max(), limit = limit)
    fun killsScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, kills.sum(), limit = limit)

    fun lifetimeDamageTaken(uuid: UUID) = lifetime(uuid, damageTaken.sum()).firstOrNull() ?: 0F
    fun lifetimeDamageTakenScoreboard(limit: Int = 10) = lifetimeScoreboard(damageTaken.sum(), limit = limit)
    fun lifetimeMostDamageTakenScoreboard(limit: Int = 10) = lifetimeScoreboard(damageTaken.max(), limit = limit)
    fun damageTakenScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, damageTaken.sum(), limit = limit)

    fun lifetimeDamageHealed(uuid: UUID) = lifetime(uuid, damageHealed.sum()).firstOrNull() ?: 0F
    fun lifetimeDamageHealedScoreboard(limit: Int = 10) = lifetimeScoreboard(damageHealed.sum(), limit = limit)
    fun lifetimeMostDamageHealedScoreboard(limit: Int = 10) = lifetimeScoreboard(damageHealed.max(), limit = limit)
    fun damageHealedScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, damageHealed.sum(), limit = limit)

    fun lifetimeDamageDealt(uuid: UUID) = lifetime(uuid, damageDealt.sum()).firstOrNull() ?: 0F
    fun lifetimeDamageDealtScoreboard(limit: Int = 10) = lifetimeScoreboard(damageDealt.sum(), limit = limit)
    fun lifetimeMostDamageDealtScoreboard(limit: Int = 10) = lifetimeScoreboard(damageDealt.max(), limit = limit)
    fun damageDealtScoreboard(minigames: Iterable<Minigame>, limit: Int = 10) = scoreboard(minigames, damageDealt.sum(), limit = limit)
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