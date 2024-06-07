package net.casual.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class CasualDatabase(url: String, username: String, password: String) {
    private val source = source(url, username, password)
    private val database = Database.connect(source)

    fun initialize() {
        transaction {
            SchemaUtils.create(
                DiscordTeams,
                DiscordPlayers,
                Minigames,
                MinigameAdvancements,
                MinigameAdvancementAwards,
                MinigamePlayers,
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

    // TODO:
    // suspend fun getOrCreateDiscordPlayer(username: String): DiscordPlayer? {
    //     var player = getDiscordPlayer(username)
    //     if (player != null) {
    //         return player
    //     }
    //     val profile = Kojang.getMojangUserProfile(username) ?: return null
    //     val uuid = UUID.fromString(profile.uuid)
    //     player = getDiscordPlayer(uuid)
    //     if (player != null) {
    //         player.name = profile.username
    //         return player
    //     }
    //     return transaction {
    //         DiscordPlayer.new(uuid) {
    //             name = profile.username
    //             team = null
    //         }
    //     }
    // }

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

    fun <T> transaction(statement: Transaction.() -> T): T {
        return transaction(database) {
            addLogger(StdOutSqlLogger)
            statement.invoke(this)
        }
    }

    fun close() {
        source.close()
    }

    private fun source(url: String, username: String, password: String): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = url
        config.username = username
        config.password = password
        return HikariDataSource(config)
    }
}