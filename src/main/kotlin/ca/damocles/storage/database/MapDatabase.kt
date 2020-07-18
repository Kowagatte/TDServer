package ca.damocles.storage.database

import com.mongodb.client.MongoCollection
import org.bson.Document

object MapDatabase {
    private val mapCollection: MongoCollection<Document> = Database.database.getCollection("maps")
}