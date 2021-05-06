package ca.damocles.game

import ca.damocles.game.map.Level
import ca.damocles.storage.Account

class Game(val playerOne: Account, val playerTwo: Account){

    var outcome: GameOutcome = GameOutcome.NONE_YET
    val score: MutableList<Int> = mutableListOf(0, 0)
    val level: Level = Level()

    fun incrementScore(player: Int) = score[player-1]++
}

enum class GameOutcome(val value: Float){
    PLAYER_ONE(1f),
    PLAYER_TWO(0f),
    TIE(0.5f),
    NONE_YET(-1f);
}