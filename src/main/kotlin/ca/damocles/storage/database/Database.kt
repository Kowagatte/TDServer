package ca.damocles.storage.database

import ca.damocles.storage.Account
import com.mongodb.client.*
import com.mongodb.client.model.Filters.eq
import org.bson.Document
import org.bson.conversions.Bson
import java.util.*

interface Storable{
    fun toDatabaseObject(): Document{
        return Document.parse(this.toString())
    }
}

object Database{
    /**
     * MongoClient
     * ***Contains the login credentials for the read/write MongoDB account, do not leak.***
     */
    private val mongoClient: MongoClient = MongoClients.create("mongodb+srv://accountdb_connection:ZLFAfSFyvfF9VbZ2@damocles-fcf0g.mongodb.net/test?retryWrites=true&w=majority")
    val database: MongoDatabase = mongoClient.getDatabase("damocles")

    fun add(element: Storable, collection: String) =
        database.getCollection(collection).insertOne(element.toDatabaseObject())

    fun find(bson: Bson, collection: String): FindIterable<Document> =
        database.getCollection(collection).find(bson)

    fun list(){
        TODO("Return a list of Json elements in the database")
    }
}