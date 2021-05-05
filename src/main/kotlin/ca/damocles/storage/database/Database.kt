package ca.damocles.storage.database

import ca.damocles.utilities.JsonFile
import com.mongodb.client.*
import org.bson.Document
import org.bson.conversions.Bson

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

    private val mongoURI: String

    init{
        val passFile = JsonFile.openJsonFile("passwords.json")
        val username = passFile.getJsonObject("mongodb").getAsJsonPrimitive("username").asString
        val password = passFile.getJsonObject("mongodb").getAsJsonPrimitive("password").asString
        mongoURI = "mongodb+srv://$username:$password@damocles-fcf0g.mongodb.net/test?retryWrites=true&w=majority"
    }

    private val mongoClient: MongoClient = MongoClients.create(mongoURI)
    val database: MongoDatabase = mongoClient.getDatabase("damocles")

    fun add(element: Storable, collection: String) =
        database.getCollection(collection).insertOne(element.toDatabaseObject())

    fun find(bson: Bson, collection: String): FindIterable<Document> =
        database.getCollection(collection).find(bson)

    fun list(){
        TODO("Return a list of Json elements in the database")
    }
}