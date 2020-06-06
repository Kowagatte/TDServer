package ca.damocles.storage

import com.mongodb.client.*
import com.mongodb.client.model.Filters.eq
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

object AccountDatabase{

    private val accountCollection: MongoCollection<Document> = Database.database.getCollection("account")

    /**
     * Used inside AccountDatabase instead of MongoCollection<Document>#find(Bson)
     * Returns a list of results with an empty account attached at the end.
     * This way empty lists (aka..no results found) will return a list with only an empty account instead of null.
     */
    fun findWithBackup(field: String, value: Any): List<Document>{
        val results: MutableList<Document> = accountCollection.find(eq(field, value)).toMutableList()
        results.add(getEmptyAccount().toDatabaseObject())
        return results
    }

    /**
     * Gets an Account that matches the corresponding field, partial matches are not returned.
     * If no match is found it returns an empty account of UUID: 00000000-0000-0000-0000-000000000000
     */
    private fun getAccountByField(field: String, value: Any): Account {
        return Account.fromJson(findWithBackup(field, value).first().toJson())
    }

    fun getEmptyAccount(): Account{
        return Account(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Not Found", "Not Found", "")
    }

    fun getAccountByUsername(username: String): Account = getAccountByField("username", username)

    fun getAccountByEmail(email: String): Account = getAccountByField("email", email)

    fun getAccountByUUID(uuid: UUID): Account = getAccountByField("uuid", uuid.toString())

    fun addAccount(account: Account) = accountCollection.insertOne(account.toDatabaseObject())

}

fun main(){
    //val newAccount: Account = Account(UUID.randomUUID(), "nnryanp@gmail.com", "Nick", BCrypt.hashpw("12345qwesd", BCrypt.gensalt()))
    //println(newAccount)
    //Database.add(Account(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Not Found", "Not Found", ""), "account")
    //println(Database.find(eq("username", "Nick"), "account").first()?.get("username"))
    //Database.find(eq("username", "Nick"), "account").first()?.toJson()
    //println(AccountDatabase.accountCollection.find(eq("username", "Nick")).first()?.email)
    //println(AccountDatabase.getAccountByUsername("Dalton").email)
    //AccountDatabase.addAccount(Account(UUID.randomUUID(), "craftartonline@gmail.com", "kowagatte", BCrypt.hashpw("password", BCrypt.gensalt())))
}