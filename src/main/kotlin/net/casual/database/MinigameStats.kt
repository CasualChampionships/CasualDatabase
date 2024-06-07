package net.casual.database

import net.casual.stat.FormattedStat
import net.casual.stat.UnresolvedPlayerStat
import net.casual.stat.UnresolvedPlayerStat.Companion.filterNotNull
import net.casual.util.CollectionUtils.filterNotNull
import net.casual.util.Named
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

    fun <T> lifetime(
        player: UUID,
        column: Expression<T>,
        condition: Expression<Boolean> = Op.TRUE,
    ): List<T> {
        return joinedWithEventPlayers()
            .select(column)
            .where { (EventPlayers.uuid eq player) and condition }
            .map { it[column] }
    }

    fun <T> lifetimeScoreboard(
        column: Expression<T>,
        condition: Op<Boolean> = Op.TRUE,
        sort: SortOrder = SortOrder.DESC,
        limit: Int = 0
    ): List<UnresolvedPlayerStat<T>> {
        return joinedWithEventPlayers()
            .select(EventPlayers.uuid, column)
            .where { condition }
            .groupBy(EventPlayers.uuid)
            .limit(limit)
            .orderBy(column, sort)
            .map { UnresolvedPlayerStat(it[EventPlayers.uuid], it[column]) }
    }

    fun <T> scoreboard(
        minigame: Minigame,
        column: Expression<T>,
        condition: Op<Boolean> = Op.TRUE,
        sort: SortOrder = SortOrder.DESC,
        limit: Int = 0
    ): List<UnresolvedPlayerStat<T>> {
        return joinedWithEventPlayers()
            .select(EventPlayers.uuid, column)
            .where { (MinigamePlayers.minigame eq minigame.id) and condition }
            .groupBy(EventPlayers.uuid)
            .limit(limit)
            .orderBy(column, sort)
            .map { UnresolvedPlayerStat(it[EventPlayers.uuid], it[column]) }
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

    abstract fun getFormattedStats(): List<Named<FormattedStat>>
}

object DuelMinigameStats: MinigameStats() {
    val won = bool("won")
    val kills = integer("kills")

    fun lifetimeWins(player: UUID): Long {
        return lifetime(player, won.count(), won eq true).firstOrNull() ?: 0L
    }

    fun lifetimeWinsScoreboard(limit: Int = 10): List<UnresolvedPlayerStat<Long>> {
        return lifetimeScoreboard(won.count(), won eq true, limit = limit)
    }

    fun lifetimeKills(player: UUID): Int {
        return lifetime(player, kills.sum()).firstOrNull() ?: 0
    }

    fun lifetimeKillsScoreboard(limit: Int = 10): List<UnresolvedPlayerStat<Int>> {
        return lifetimeScoreboard(kills.sum(), limit = limit).filterNotNull()
    }

    fun lifetimeMostKillsScoreboard(limit: Int = 10): List<UnresolvedPlayerStat<Int>> {
        return lifetimeScoreboard(kills.max(), limit = limit).filterNotNull()
    }

    fun killsScoreboard(minigame: Minigame, limit: Int = 10): List<UnresolvedPlayerStat<Int>> {
        return scoreboard(minigame, kills.max(), limit = limit).filterNotNull()
    }
}

class DuelPlayerStats(id: EntityID<Int>): PlayerStats(id) {
    var player by MinigamePlayer referencedOn DuelMinigameStats.id

    var won by DuelMinigameStats.won
    var kills by DuelMinigameStats.kills

    override val minigameName: String = "Duel"

    override fun getFormattedStats(): List<Named<FormattedStat>> {
        return listOf(
            Named("Won", FormattedStat.bool(won)),
            Named("Kills",  FormattedStat.int32(kills))
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

    override fun getFormattedStats(): List<Named<FormattedStat>> {
        return listOf(
            Named("Kills", FormattedStat.int32(kills)),
            Named("Damage Dealt", FormattedStat.float32(damageDealt)),
            Named("Damage Taken", FormattedStat.float32(damageTaken)),
            Named("Damage Healed", FormattedStat.float32(damageHealed))
        )
    }

    companion object: IntEntityClass<UHCPlayerStats>(UHCMinigameStats)
}