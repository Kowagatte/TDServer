package ca.damocles.game.community

import ca.damocles.storage.MapRecord
import ca.damocles.storage.database.MapDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object MapRotation {
    var mapsInRotation: MutableList<MapRecord> = MapDatabase.getTopMaps(4)

    init{
        GlobalScope.launch {
            while(isActive){
                delay(86400000)
                mapsInRotation = MapDatabase.getTopMaps(4)
            }
        }
    }
}