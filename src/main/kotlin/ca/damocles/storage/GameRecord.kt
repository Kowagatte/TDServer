package ca.damocles.storage

import ca.damocles.storage.database.Storable
import ca.damocles.utilities.EloRating
import com.google.gson.Gson
import java.util.*

class GameRecord(map: MapRecord, playerOne: Account, playerTwo: Account, val outcome: Float): Storable {

    val date: Date = Date()
    val mapID: UUID = map.mapID
    val playerOneUsername: String = playerOne.username
    val playerOneUUID: UUID = playerOne.uuid
    val playerTwoUsername: String = playerTwo.username
    val playerTwoUUID: UUID = playerTwo.uuid
    val playerOneRating: Float = playerOne.rating
    val playerTwoRating: Float = playerTwo.rating
    val playerOneRatingDiff: Float = EloRating(playerOne, playerTwo).getDiffs(outcome).first
    val playerTwoRatingDiff: Float = EloRating(playerOne, playerTwo).getDiffs(outcome).second


    companion object{
        fun fromJson(gameRecordJson: String): GameRecord {
            return Gson().fromJson(gameRecordJson, GameRecord::class.java)
        }
    }
    override fun toString(): String = Gson().toJson(this)
}