package ca.damocles.storage

import ca.damocles.storage.database.Storable
import com.google.gson.Gson
import java.util.*

data class MapRecord(
    val mapID: UUID,
    val name: String,
    val walls: List<List<Int>>,
    val spawns: List<List<Int>>,
    val coins: List<List<Int>>,
    val votes: Int = 0
): Storable {

    companion object{
        fun fromJson(json: String): MapRecord {
            return Gson().fromJson(json, MapRecord::class.java)
        }
    }
    override fun toString(): String = Gson().toJson(this)
}