package ca.damocles.storage

import ca.damocles.storage.database.Storable
import ca.damocles.game.EloRating
import ca.damocles.game.GameOutcome
import com.google.gson.Gson
import java.util.*

/**
 * TODO Document
 */
class GameRecord(
    map: MapRecord,
    playerOne: Account,
    playerTwo: Account,
    val outcome: Float
): Storable {

    val date: Date = Date()
    val map: UUID = map.mapID
    val players: List<PlayerRecord>

    init{

        val ratings = EloRating(playerOne, playerTwo)
        val diffs = ratings.getDiffs(outcome)

        players = listOf(
            PlayerRecord(
                1,
                playerOne.uuid,
                playerOne.username,
                playerOne.rating,
                diffs.first
            ),
            PlayerRecord(
                2,
                playerTwo.uuid,
                playerTwo.username,
                playerTwo.rating,
                diffs.second
            )
        )
    }

    companion object{
        fun fromJson(gameRecordJson: String): GameRecord {
            return Gson().fromJson(gameRecordJson, GameRecord::class.java)
        }
    }
    override fun toString(): String = Gson().toJson(this)
}

/**
 * TODO Document
 */
data class PlayerRecord(
    val player: Int,
    val id: UUID,
    val username: String,
    val rating: Float,
    val diff: Float
)