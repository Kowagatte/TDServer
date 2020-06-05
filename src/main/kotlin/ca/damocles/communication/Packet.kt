package ca.damocles.communication

import ca.damocles.communication.client.EstablishedConnection
import ca.damocles.storage.authenticateLogin
import com.google.gson.Gson

abstract class Packet(open val identity: String, val side: Byte, val type: Byte, val body: HashMap<String, Any> = HashMap()){
    companion object{
        fun fromJson(packet: String): Packet{
            return Gson().fromJson(packet, Packet::class.java)
        }
    }
    abstract fun handle(connection: EstablishedConnection)
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

class LoginPacket(override val identity: String, email: String, password: String): Packet(side=1, type=0, identity=identity) {
    init{
        this.body["email"] = email
        this.body["password"] = password
    }

    override fun handle(connection: EstablishedConnection) {
        val response = authenticateLogin(body["email"].toString(), body["password"].toString())
        if(response.first){
            connection.account = response.second
            connection.send(ResponsePacket(identity, "Login Successful!"))
        }else{
            connection.send(ResponsePacket(identity, "Login Not Successful!"))
        }
        TODO("Not yet implemented")
    }
}

class ResponsePacket(override val identity: String, info: String): Packet(side=0, type=0, identity=identity) {
    override fun handle(connection: EstablishedConnection) {}
    init{
        this.body["info"] = info
    }
}