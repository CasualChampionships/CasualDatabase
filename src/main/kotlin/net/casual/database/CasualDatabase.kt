package net.casual.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import net.casual.database.stats.DuelMinigameStats
import net.casual.database.stats.UHCMinigameStats
import org.jetbrains.exposed.v1.core.DatabaseConfig
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.*

class CasualDatabase(
    url: String,
    username: String? = null,
    password: String? = null,
    config: DatabaseConfig? = null
) {
    private val source = source(url, username, password)
    private val database = Database.connect(source, databaseConfig = config)

    fun initialize() {
        transaction {
            SchemaUtils.create(
                DiscordTeams,
                DiscordPlayers,
                Minigames,
                MinigameAdvancements,
                MinigameAdvancementAwards,
                MinigamePlayers,
                Events,
                EventPlayers,
                EventTeams,
                UHCMinigameStats,
                DuelMinigameStats
            )
        }
    }

    fun getDiscordPlayer(username: String): DiscordPlayer? {
        return transaction {
            DiscordPlayer.find { DiscordPlayers.name eq username }.singleOrNull()
        }
    }

    fun getDiscordPlayer(uuid: UUID): DiscordPlayer? {
        return transaction {
            DiscordPlayer.findById(uuid)
        }
    }

    fun getDiscordTeam(name: String): DiscordTeam? {
        return transaction {
            DiscordTeam.find { DiscordTeams.name eq name }.singleOrNull()
        }
    }

    fun getDiscordTeams(): List<DiscordTeam> {
        return transaction {
            DiscordTeam.all().toList()
        }
    }

    fun getDiscordPlayers(): List<DiscordPlayer> {
        return transaction {
            DiscordPlayer.all().toList()
        }
    }

    fun getEvents(): List<Event> {
        return transaction {
            Event.all().toList()
        }
    }

    fun <T> transaction(statement: Transaction.() -> T): T {
        return transaction(database) {
            statement.invoke(this)
        }
    }

    fun close() {
        source.close()
    }

    private fun source(url: String, username: String?, password: String?): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = url
        config.username = username
        config.password = password
        return HikariDataSource(config)
    }
}