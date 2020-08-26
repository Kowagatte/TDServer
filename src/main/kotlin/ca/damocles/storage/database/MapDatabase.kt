package ca.damocles.storage.database

import ca.damocles.storage.MapRecord
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import org.bson.Document

object MapDatabase {
    private val mapCollection: MongoCollection<Document> = Database.database.getCollection("maps")

    fun getTopMaps(value: Int): MutableList<MapRecord> {
        val topMaps = mutableListOf<MapRecord>()
        mapCollection.find().sort(BasicDBObject("votes", -1)).limit(value).forEach {
            topMaps.add(MapRecord.fromJson(it.toJson()))
        }
        return topMaps
    }
}