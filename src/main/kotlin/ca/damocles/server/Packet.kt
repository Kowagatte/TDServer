package ca.damocles.server

import com.google.gson.Gson

/**
 * PACKETS:
 * SIDE: 1=To be received by SERVER, 0=To be received by CLIENT
 * TYPE: Packets ID
 */
open class Packet(val identity: String, val side: Byte, val type: Byte, val body: HashMap<String, Any> = HashMap()){
    companion object{
        fun fromJson(packet: String): Packet {
            return Gson().fromJson(packet, Packet::class.java)
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}

/* CLIENT PACKETS */

/**
 * Sends login credentials to be validated and signed into.
 */
class LoginPacket(identity: String, email: String, password: String): Packet(side=1, type=0, identity=identity) {
    init{
        this.body["email"] = email
        this.body["password"] = password
    }
}

/**
 * Sent to tell the server the client was disconnected.
 */
class ClosePacket(identity: String): Packet(side=1, type=1, identity = identity){}

/**
 * Requests the creation of an account.
 */
class CreateAccountPacket(identity: String, email: String, username: String, password: String): Packet(side=1, type=2, identity = identity){
    init{
        this.body["email"] = email
        this.body["username"] = username
        this.body["password"] = password
    }
}

/**
 * Sent to tell the server the client is still responding.
 */
class PongPacket(identity: String): Packet(side=1, type=3, identity = identity){}

/* SERVER PACKETS */

/**
 * Sent to client in response to other packets.
 * Includes information and a code based on the response.
 * Should be sent with the identity of the packet being responded too.
 */
class ResponsePacket(identity: String, info: String, code: Int): Packet(side=0, type=0, identity=identity) {
    init{
        this.body["code"] = code
        this.body["info"] = info
    }
}

/**
 * Sent to check if the client is still there.
 */
class PingPacket(identity: String): Packet(side=0, type=1, identity = identity){}