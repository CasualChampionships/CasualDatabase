package net.casual.database

import net.casual.stat.FormattedStat
import net.casual.util.CollectionUtils.replaceNullableWithDefault
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.kotlin.datetime.duration
import java.util.*

abstract class MinigameStats: IdTable<Int>() {
    final override val id = reference("player", MinigamePlayers)

    final override val primaryKey: PrimaryKey = PrimaryKey(id)

    protected fun <T> lifetime(player: UUID, column: Expression<T>, condition: Expression<Boolean> = Op.TRUE): List<T> {
        return joinedWithEventPlayers()
            .select(column)
            .where { (EventPlayers.uuid eq player) and condition }
            .map { it[column] }
    }

    protected fun <T> lifetimeScoreboard(column: Expression<T>, condition: Op<Boolean> = Op.TRUE): List<Pair<UUID, T>> {
        return joinedWithEventPlayers()
            .select(EventPlayers.uuid, column)
            .where { condition }
            .groupBy(EventPlayers.uuid)
            .orderBy(column)
            .map { it[EventPlayers.uuid] to it[column] }
    }

    protected fun <T> scoreboard(minigame: Minigame, column: Expression<T>, condition: Op<Boolean> = Op.TRUE): List<Pair<UUID, T>> {
        return joinedWithEventPlayers()
            .select(EventPlayers.uuid, column)
            .where { (MinigamePlayers.minigame eq minigame.id) and condition }
            .groupBy(EventPlayers.uuid)
            .orderBy(column)
            .map { it[EventPlayers.uuid] to it[column] }
    }

    private fun joinedWithMinigamePlayers(): Join {
        return join(
            MinigamePlayers,
            JoinType.INNER,
            additionalConstraint = { DuelMinigameStats.id eq MinigamePlayers.id }
        )
    }

    private fun joinedWithEventPlayers(): Join {
        return joinedWithMinigamePlayers()
            .join(EventPlayers, JoinType.INNER, additionalConstraint = { MinigamePlayers.player eq EventPlayers.id })
    }
}

abstract class PlayerStats(id: EntityID<Int>): IntEntity(id) {
    abstract val minigameName: String

    abstract fun getFormattedStats(): List<FormattedStat>
}

object DuelMinigameStats: MinigameStats() {
    val won = bool("won")
    val kills = integer("kills")

    fun lifetimeWins(player: UUID) = lifetime(player, won.count(), won eq true).firstOrNull() ?: 0L

    fun lifetimeWinsScoreboard() = lifetimeScoreboard(won.count(), won eq true)

    fun lifetimeKills(player: UUID) = lifetime(player, kills.sum()).firstOrNull() ?: 0

    fun lifetimeKillsScoreboard() = lifetimeScoreboard(kills.sum()).replaceNullableWithDefault(0)

    fun lifetimeMostKillsScoreboard() = lifetimeScoreboard(kills.max()).replaceNullableWithDefault(0)

    fun killsScoreboard(minigame: Minigame) = scoreboard(minigame, kills.max())
}

class DuelPlayerStats(id: EntityID<Int>): PlayerStats(id) {
    var player by MinigamePlayer referencedOn DuelMinigameStats.id

    var won by DuelMinigameStats.won
    var kills by DuelMinigameStats.kills

    override val minigameName: String = "Duel"

    override fun getFormattedStats(): List<FormattedStat> {
        return listOf(
            FormattedStat.bool("Won", won),
            FormattedStat.int32("Kills", kills)
        )
    }

    companion object: IntEntityClass<DuelPlayerStats>(DuelMinigameStats)
}

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

    fun lifetimeWinsScoreboard() = lifetimeScoreboard(won.count(), won eq true)
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

    override val minigameName: String = "UHC"

    override fun getFormattedStats(): List<FormattedStat> {
        return listOf(
            // FormattedStat.bool("Won", won),
            // FormattedStat.bool("Died", died),
            FormattedStat.int32("Kills", kills),
            FormattedStat.float32("Damage Dealt", damageDealt),
            FormattedStat.float32("Damage Taken", damageTaken),
            FormattedStat.float32("Damage Healed", damageHealed),
            // FormattedStat.duration("Time Alive", aliveTime),
            // FormattedStat.duration("Time Crouching", crouchTime)
        )
    }

    companion object: IntEntityClass<UHCPlayerStats>(UHCMinigameStats)
}