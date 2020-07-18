package ca.damocles.game

import ca.damocles.game.map.Level
import ca.damocles.storage.Account
import ca.damocles.utilities.EloRating
import java.util.*

class Game(val playerOne: Account, val playerTwo: Account){
    var outcome: Float = -1f
    val score: MutableList<Int> = mutableListOf(0, 0)
    val level: Level = Level()

    fun incrementScore(player: Int){
        score[player-1]++
    }
}