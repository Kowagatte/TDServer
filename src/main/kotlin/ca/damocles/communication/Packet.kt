package ca.damocles.communication

import com.google.gson.Gson

open class Packet(val identity: String, val side: Byte, val type: Byte, val body: HashMap<String, Any> = HashMap()){
    companion object{
        fun fromJson(packet: String): Packet{
            return Gson().fromJson(packet, Packet::class.java)
        }
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class LoginPacket(identity: String, email: String, password: String): Packet(side=1, type=0, identity=identity) {
    init{
        this.body["email"] = email
        this.body["password"] = password
    }
}

class ClosePacket(identity: String): Packet(side=1, type=1, identity = identity){}

class ResponsePacket(identity: String, info: String): Packet(side=0, type=0, identity=identity) {
    init{
        this.body["info"] = info
    }
}

class CreateAccountPacket(identity: String, email: String, username: String, password: String): Packet(side=1, type=2, identity = identity){
    init{
        this.body["email"] = email
        this.body["username"] = username
        this.body["password"] = password
    }
}