package ca.damocles.storage

import com.google.gson.Gson
import java.util.*

data class Account(val uuid: UUID, var email: String, var username: String, var password: String): Storable{
    override fun toString(): String {
        return Gson().toJson(this)
    }
}