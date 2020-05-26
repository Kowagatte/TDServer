package ca.damocles.communication

import com.google.gson.Gson

data class Packet(val identity: String, val side: Byte, val type: Byte, val body: HashMap<String, Any> = HashMap()){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

fun fromJson(packet: String): Packet{
    return Gson().fromJson(packet, Packet::class.java)
}