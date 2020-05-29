package ca.damocles.communication

import ca.damocles.utilities.generateAlphaString
import com.google.gson.Gson

open class Packet(open val identity: String, val side: Byte, val type: Byte, val body: HashMap<String, Any> = HashMap()){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class LoginPacket(override val identity: String, username: String, password: String): Packet(side=1, type=0, identity=identity) {
    init{
        this.body["username"] = username
        this.body["password"] = password
    }
}

fun fromJson(packet: String): Packet{
    return Gson().fromJson(packet, Packet::class.java)
}