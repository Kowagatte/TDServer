
package ca.damocles.storage


import com.google.gson.Gson
import com.sun.org.apache.xpath.internal.operations.Bool
import java.util.*
import org.mindrot.jbcrypt.BCrypt

/**
 * Data Class for storing Account Objects.
 */
data class Account(val uuid: UUID, var email: String, var username: String, var password: String, val rating: Float = 1000f, val gameRecords: List<GameRecord> = listOf<GameRecord>()): Storable{
    companion object{
        fun fromJson(accountJson: String): Account {
            return Gson().fromJson(accountJson, Account::class.java)
        }
    }
    override fun toString(): String = Gson().toJson(this)
}

/**
 * Take a String, email as a parameter
 * Take a String, username as a parameter
 * Take a String, password as a parameter
 * Check if the username is taken,
 * check if the email has already been used,
 * Create an account data object representing the data if both conditions are met (password must be encrypted)
 * encrypting password -> [BCrypt.hashpw(password, BCrypt.gensalt())]
 * Store to account database
 */
fun createAccount(email: String, username: String, password: String): Boolean{
    //check if username or email is taken.
    if(AccountDatabase.getAccountByUsername(username) != AccountDatabase.getEmptyAccount() && AccountDatabase.getAccountByEmail(email) != AccountDatabase.getEmptyAccount())
        return false

    AccountDatabase.addAccount(Account(UUID.randomUUID(), email, username, BCrypt.hashpw(password, BCrypt.gensalt())))
    return true
}

/**
 * Take a String, email as a parameter
 * Take a String, password as a parameter
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