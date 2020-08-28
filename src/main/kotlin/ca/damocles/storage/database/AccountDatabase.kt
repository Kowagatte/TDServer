package ca.damocles.storage.database

import ca.damocles.storage.Account
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import org.bson.Document
import org.mindrot.jbcrypt.BCrypt
import java.util.*
import javax.management.Query.eq

object AccountDatabase {

    private val accountCollection: MongoCollection<Document> = Database.database.getCollection("account")

    /**
     * Used inside AccountDatabase instead of MongoCollection<Document>#find(Bson)
     * Returns a list of results with an empty account attached at the end.
     * This way empty lists (aka..no results found) will return a list with only an empty account instead of null.
     */
    private fun findWithBackup(field: String, value: Any): List<Document> {
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

    /**
     * Returns an empty account, this is a placeholder account for when searches yield no result or are the result of
     * an unauthorized search.
     */
    fun getEmptyAccount(): Account {
        return Account(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Not Found", "Not Found", "")
    }

    fun getAccountByUsername(username: String): Account = getAccountByField("username", username)

    fun getAccountByEmail(email: String): Account = getAccountByField("email", email)

    fun getAccountByUUID(uuid: UUID): Account = getAccountByField("uuid", uuid.toString())

    fun addAccount(account: Account) = accountCollection.insertOne(account.toDatabaseObject())

    /**
     * Changes the given accounts password, you MUST pass through a hash & salted password.
     */
    fun changePassword(account: Account, password: String) {
        accountCollection.updateOne(eq("uuid", account.uuid.toString()), Document("\$set", Document("password", password)))
    }

    private fun changeRatingByField(field: String, value: Any, newRating: Float) {
        val old = getAccountByField(field, value).toDatabaseObject()
        old["rating"] = newRating
        accountCollection.findOneAndReplace(eq(field, value), old)
    }

    fun changeRating(username: String, newRating: Float) = changeRatingByField("username", username, newRating)

    fun changeRating(uuid: UUID, newRating: Float) = changeRatingByField("uuid", uuid.toString(), newRating)

}