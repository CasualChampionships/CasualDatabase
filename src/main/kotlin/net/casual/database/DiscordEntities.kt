package net.casual.database

import net.casual.MinecraftColor
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.emptySized
import java.util.*

object DiscordTeams: IntIdTable() {
    val name = varchar("name", 32).uniqueIndex()
    val prefix = varchar("prefix", 16)
    val logo = varchar("logo_url", 2083).nullable()
    val color = enumeration("color", MinecraftColor::class)
    val wins = integer("wins")
    val roleId = long("role_id").nullable()
    val channelId = long("channel_id").nullable()
}

object DiscordPlayers: UUIDTable() {
    val name = varchar("name", 16).index()
    val team = optReference("team", DiscordTeams)
}

class DiscordPlayer(id: EntityID<UUID>): UUIDEntity(id) {
    var name by DiscordPlayers.name
    var team by DiscordTeam optionalReferencedOn DiscordPlayers.team

    companion object: UUIDEntityClass<DiscordPlayer>(DiscordPlayers)
}

class DiscordTeam(id: EntityID<Int>): IntEntity(id) {
    var name by DiscordTeams.name
    var prefix by DiscordTeams.prefix
    var logo by DiscordTeams.logo
    var color by DiscordTeams.color
    var wins by DiscordTeams.wins
    var roleId by DiscordTeams.roleId
    var channelId by DiscordTeams.channelId

    val players: SizedIterable<DiscordPlayer>
        get() = optionalPlayers ?: emptySized()

    private val optionalPlayers: SizedIterable<DiscordPlayer>? by DiscordPlayer optionalReferrersOn DiscordPlayers.team

    companion object: IntEntityClass<DiscordTeam>(DiscordTeams)
}