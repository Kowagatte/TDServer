package ca.damocles.storage.database

import ca.damocles.storage.Account
import com.google.gson.Gson
import java.util.*

/**
 * AccountDatabase Class
 *
 * This is the entrypoint for the application to modify
 * the storage of Account objects in the database.
 *
 * ***There should be no calls to specific database implementations here.***
 *
 */
class AccountDatabase(val db: DatabaseDriver) {

    //The namespace of the tables storing the accounts
    private val accountTable: String = TableConstants.ACCOUNT.name

    /**
     * Get's a reference of the default empty account.
     * Used to be returned on calls to the database where nothing is found.
     * @return: a reference to an empty account.
     */
    fun emptyAccount(): Account =
        Account(
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            "Not Found",
            "Not Found",
            ""
        )

    /**
     * Gets the account with the closest username to the supplied one.
     * Returns an empty account if one can not be found.
     * @param username: the username you are searching for.
     * @return: An account in the database with the closest username to the one supplied,
     * returns an empty account if one cannot be found.
     */
    fun byUsername(username: String): Account{
        val json = db.find(accountTable, "username", username)
        return if (json != null) Account.fromJson(json.toString()) else emptyAccount()
    }

    /**
     * Gets an account with the exact username supplied.
     * If the given username cannot be found or does not exist it returns an empty account.
     * @param username: The username being searched for.
     * @return: the Account with the given username if found, or an empty account if it does not exist.
     */
    fun byExactUsername(username: String): Account{
        val json = db.find(accountTable, "username", username)
        if(json != null)
            if(json.getAsJsonPrimitive("username").asString.equals(username, true))
                return Account.fromJson(json.toString())
        return emptyAccount()
    }

    /**
     * Gets an account with the exact email supplied.
     * If the given email cannot be found or does not exist it returns an empty account.
     * @param email: The email being searched for.
     * @return: the Account with the given email if found, or an empty account if it does not exist.
     */
    fun byEmail(email: String): Account{
        val json = db.find(accountTable, "email", email)
        if(json != null)
            if(json.getAsJsonPrimitive("email").asString.equals(email, true))
                return Account.fromJson(json.toString())
        return emptyAccount()
    }

    /**
     * Gets an account with the exact uuid supplied.
     * If the given uuid cannot be found or does not exist it returns an empty account.
     * @param uuid: The uuid being searched for.
     * @return: the Account with the given uuid if found, or an empty account if it does not exist.
     */
    fun byUUID(uuid: String): Account = byUUID(UUID.fromString(uuid))

    /**
     * Gets an account with the exact uuid supplied.
     * If the given uuid cannot be found or does not exist it returns an empty account.
     * @param uuid: The uuid being searched for.
     * @return: the Account with the given uuid if found, or an empty account if it does not exist.
     */
    fun byUUID(uuid: UUID): Account{
        val json = db.find(accountTable, "uuid", uuid)
        if(json != null)
            if(json.getAsJsonPrimitive("uuid").asString.equals(uuid.toString(), true))
                return Account.fromJson(json.toString())
        return emptyAccount()
    }

    fun add(account: Account): Boolean{
        TODO("Write this method")
    }


    fun main(args: Array<String>){
        val gson = Gson()
        val account = Account(UUID.randomUUID(), "test@damocles.ca", "87pen", "---")

        val s = account.toString()
        val restruct = gson.fromJson(s, account::class.java)
        println(restruct)

    }


}