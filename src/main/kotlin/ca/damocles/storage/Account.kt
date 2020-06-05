
package ca.damocles.storage

import com.google.gson.Gson
import com.sun.org.apache.xpath.internal.operations.Bool
import java.util.*
import org.mindrot.jbcrypt.BCrypt

/**
 * Data Class for storing Account Objects.
 */
data class Account(val uuid: UUID, var email: String, var username: String, var password: String): Storable{
    companion object{
        fun fromJson(accountJson: String): Account {
            return Gson().fromJson(accountJson, Account::class.java)
        }
    }
    override fun toString(): String = Gson().toJson(this)
}

/**
 * Take an AuthAccount as a parameter
 * Check if an account exists with the username of the auth account
 * if it does, compare passwords (BCrypt.checkpw)
 * return a pair, (boolean, Account) (true if it passes and the corresponding account), ( false if it doesnt pass and null for the account)
 */
fun authenticateLogin(email: String, password: String): Pair<Boolean, Account> {
    val databaseAccount = AccountDatabase.getAccountByEmail(email)
    //if there's an account with this email in the database.
    if (email == databaseAccount.email)
        //and if the password provided matches the one stored.
        if (BCrypt.checkpw(password, databaseAccount.password))
            return Pair(true, databaseAccount)
    return Pair(false,  AccountDatabase.getEmptyAccount())
}