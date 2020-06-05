package ca.damocles.storage

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

object AccountDatabase{

    private val accountCollection: MongoCollection<Document> = Database.database.getCollection("account")

    private fun getAccountByField(field: String, value: String): Account {
        val foundAccount: Document? = accountCollection.find(eq(field, value)).first()
        return if (foundAccount != null){
            Account.fromJson(foundAccount.toJson())
        }else{
            Account(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Not Found", "Not Found", "")
        }
    }

    fun getAccountByUsername(username: String): Account = getAccountByField("username", username)

    fun getAccountByEmail(email: String): Account = getAccountByField("email", email)

    fun getAccountByUUID(uuid: UUID): Account = getAccountByField("uuid", uuid.toString())

}

fun main(){
    //val newAccount: Account = Account(UUID.randomUUID(), "nnryanp@gmail.com", "Nick", BCrypt.hashpw("12345qwesd", BCrypt.gensalt()))
    //println(newAccount)
    //Database.add(newAccount, "account")
    //println(Database.find(eq("username", "Nick"), "account").first()?.get("username"))
    //Database.find(eq("username", "Nick"), "account").first()?.toJson()
    //println(AccountDatabase.accountCollection.find(eq("username", "Nick")).first()?.email)
    //println(AccountDatabase.getAccountByUsername("Dalton").email)
}