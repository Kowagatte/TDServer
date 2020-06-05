package ca.damocles.storage

import com.google.gson.Gson
import java.util.*

/**
 * Skeleton Account Objects used for authentication.
 */
data class AuthAccount(val username: String, val password: String){
    override fun toString(): String = Gson().toJson(this)
}

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
 * return a pair, (boolean, Account) (true if it passes and the corrosponding account), ( false if it doesnt pass and null for the account)
 *
 * Testing purposes:
 * username: Nick
 * password: 12345qwesd
 */
fun authenticateLogin(){
    TODO("")
}