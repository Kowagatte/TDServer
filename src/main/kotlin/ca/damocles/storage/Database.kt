package ca.damocles.storage

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.bson.conversions.Bson
import org.mindrot.jbcrypt.BCrypt
import java.util.*

interface Storable{
    fun toDatabaseObject(): Document{
        return Document.parse(this.toString())
    }
}

object Database{
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

fun main(){
    val newAccount: Account = Account(UUID.randomUUID(), "nnryanp@gmail.com", "Nick", BCrypt.hashpw("12345qwesd", BCrypt.gensalt()))
    println(newAccount)
    //Database.add(newAccount, "account")
    //println(Database.find(eq("username", "Nick"), "account").first())
}