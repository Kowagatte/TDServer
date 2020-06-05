
package ca.damocles.storage

import com.google.gson.Gson
import com.sun.org.apache.xpath.internal.operations.Bool
import java.util.*
import org.mindrot.jbcrypt.BCrypt

data class AuthAccount(val username: String, val password: String){
    override fun toString(): String = Gson().toJson(this)
}

data class Account(val uuid: UUID, var email: String, var username: String, var password: String): Storable{
    override fun toString(): String = Gson().toJson(this)
}

fun fromJson(accountJson: String): Account {
    return Gson().fromJson(accountJson, Account::class.java)
}

/**
 * Take AuthAccount
 * Check if an account exists with the username of the auth account
 * if it does, compare passwords (BCrypt.checkpw)
 * return a pair, (boolean, Account) (true if it passes and the corrosponding account), ( false if it doesnt pass and null for the account)
 *
 *
 * Testing purposes:
 * username: Nick
 * password: 12345qwesd
 *
 */

/*fun authenticateLogin(): Pair<Boolean,Account> {
    TODO("Fix This.")
    val loggingAccount = AuthAccount(username, password)

    if(loggingAccount.username == AccountDatabase.getAccountByUsername(username).username)
        if(BCrypt.checkpw(loggingAccount.password, AccountDatabase.getAccountByUsername(loggingAccount.username).password))
            return Pair<True,>
}
*/



fun authenticateLogin(account: AuthAccount): Pair<Boolean, Account> {

    var databaseAccount = AccountDatabase.getAccountByUsername(account.username)
    //if there's an account with this username is the database.
    if (account.username == databaseAccount.username) {
        //and if the password provided matches the one stored.
        if (BCrypt.checkpw(account.password, databaseAccount.password))
            return Pair(true, databaseAccount)
    }
    return Pair(false,  Account(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Not Found", "Not Found", ""))
}
