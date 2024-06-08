package net.casual.database

import net.casual.MinecraftColor
import net.casual.database.MinigameAdvancement.Companion.referrersOn
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.mapLazy
import java.util.*

object Events: IntIdTable() {
    val name = varchar("name", 64)
}

object Minigames: UUIDTable() {
    val type = varchar("type", 64)
    val startTime = timestamp("start_time")
    val endTime = timestamp("end_time")

    val event = reference("event", Events)
}

object MinigameAdvancements: IntIdTable() {
    val minigameType = varchar("type", 64)
    val advancementId = varchar("advancement_id", 64)
    val title = varchar("title", 64)
    val displayItem = varchar("display_item", 64)
}

object MinigameAdvancementAwards: IntIdTable() {
    val player = reference("player", MinigamePlayers)
    val advancement = reference("advancement", MinigameAdvancements)
}

object MinigamePlayers: IntIdTable() {
    val player = reference("player", EventPlayers)
    val minigame = reference("minigame", Minigames)
}

object EventPlayers: IntIdTable() {
    val uuid = uuid("uuid")
    val team = reference("team", EventTeams)
}

object EventTeams: IntIdTable() {
    val name = varchar("name", 32)
    val prefix = varchar("prefix", 16)
    val color = enumeration("color", MinecraftColor::class)

    val event = reference("event", Events)
}

class Event(id: EntityID<Int>): IntEntity(id) {
    var name by Events.name

    val minigames by Minigame referrersOn Minigames.event
    val teams by EventTeam referrersOn EventTeams.event

    companion object: IntEntityClass<Event>(Events)
}

class Minigame(id: EntityID<UUID>): UUIDEntity(id) {
    var type by Minigames.type
    var startTime by Minigames.startTime
    var endTime by Minigames.endTime

    var event by Event referencedOn Minigames.event

    val players by MinigamePlayer referrersOn MinigamePlayers.minigame

    companion object: UUIDEntityClass<Minigame>(Minigames)
}

class MinigameAdvancement(id: EntityID<Int>): IntEntity(id) {
    var minigameType by MinigameAdvancements.advancementId
    var advancementId by MinigameAdvancements.advancementId
    var title by MinigameAdvancements.title
    var displayItem by MinigameAdvancements.displayItem

    val awardees by MinigamePlayer via MinigameAdvancementAwards

    companion object: IntEntityClass<MinigameAdvancement>(MinigameAdvancements)
}

class MinigameAdvancementAward(id: EntityID<Int>): IntEntity(id) {
    var player by MinigamePlayer referencedOn MinigameAdvancementAwards.player
    var advancement by MinigameAdvancement referencedOn MinigameAdvancementAwards.advancement

    companion object: IntEntityClass<MinigameAdvancementAward>(MinigameAdvancementAwards)
}

class MinigamePlayer(id: EntityID<Int>): IntEntity(id) {
    var player by EventPlayer referencedOn MinigamePlayers.player
    var minigame by Minigame referencedOn MinigamePlayers.minigame

    val advancements by MinigameAdvancement via MinigameAdvancementAwards

    val uuid: UUID get() = this.player.uuid
    val team: EventTeam get() = this.player.team

    companion object: IntEntityClass<MinigamePlayer>(MinigamePlayers)
}

class EventPlayer(id: EntityID<Int>): IntEntity(id) {
    var uuid by EventPlayers.uuid
    var team by EventTeam referencedOn EventPlayers.team

    val minigames by Minigame via MinigamePlayers
    val minigamePlayers by MinigamePlayer referrersOn MinigamePlayers.player

    val event get() = team.event

    companion object: IntEntityClass<EventPlayer>(EventPlayers)
}

class EventTeam(id: EntityID<Int>): IntEntity(id) {
    var name by EventTeams.name
    var prefix by EventTeams.prefix
    var color by EventTeams.color
    var event by Event referencedOn EventTeams.event

    val players by EventPlayer referrersOn EventPlayers.team

    companion object: IntEntityClass<EventTeam>(EventTeams)
}
