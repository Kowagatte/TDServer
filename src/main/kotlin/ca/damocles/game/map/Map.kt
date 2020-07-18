package ca.damocles.game.map

import ca.damocles.storage.database.Storable
import com.google.gson.Gson
import java.util.*

class Level(){}

data class GameMap(val mapID: UUID, val name: String, val walls: List<List<Int>>, val spawns: List<Pair<Int, Int>>, val coins: List<Pair<Int, Int>>): Storable{
    companion object{
        fun fromJson(json: String): GameMap {
            return Gson().fromJson(json, GameMap::class.java)
        }
    }
    override fun toString(): String = Gson().toJson(this)
}