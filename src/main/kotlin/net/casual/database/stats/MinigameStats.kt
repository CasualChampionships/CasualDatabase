package net.casual.database.stats

import net.casual.database.EventPlayers
import net.casual.database.Minigame
import net.casual.database.MinigamePlayer
import net.casual.database.MinigamePlayers
import net.casual.stat.UnresolvedPlayerStat
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
    ): List<UnresolvedPlayerStat<T & Any>> {
        return joinedWithEventPlayers()
            .select(EventPlayers.uuid, column)
            .where { condition }
            .groupBy(EventPlayers.uuid)
            .limit(limit)
            .orderBy(column, sort)
            .mapNotNull {
                UnresolvedPlayerStat(it[EventPlayers.uuid], it[column] ?: return@mapNotNull null)
            }
    }

    fun <T> scoreboard(
        minigame: Minigame,
        column: Expression<T>,
        condition: Op<Boolean> = Op.TRUE,
        sort: SortOrder = SortOrder.DESC,
        limit: Int = 0
    ): List<UnresolvedPlayerStat<T & Any>> {
        return joinedWithEventPlayers()
            .select(EventPlayers.uuid, column)
            .where { (MinigamePlayers.minigame eq minigame.id) and condition }
            .groupBy(EventPlayers.uuid)
            .limit(limit)
            .orderBy(column, sort)
            .mapNotNull {
                UnresolvedPlayerStat(it[EventPlayers.uuid], it[column] ?: return@mapNotNull null)
            }
    }

    private fun joinedWithMinigamePlayers(): Join {
        return join(
            MinigamePlayers,
            JoinType.INNER,
            additionalConstraint = { id eq MinigamePlayers.id }
        )
    }

    private fun joinedWithEventPlayers(): Join {
        return joinedWithMinigamePlayers()
            .join(EventPlayers, JoinType.INNER, additionalConstraint = { MinigamePlayers.player eq EventPlayers.id })
    }
}

abstract class PlayerStats(id: EntityID<Int>): IntEntity(id)


