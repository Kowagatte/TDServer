package ca.damocles.server

import ca.damocles.packets.ClientPackets
import ca.damocles.proto.Packets
import com.google.protobuf.Any

fun main(){
    val t = Packets.ClientPacket.LoginPacket.newBuilder().setEmail("nnryanp@gmail.com").setPassword("12345qwesd").build()
    val ba = Any.pack(t).toByteArray()

    val any: Any = Any.parseFrom(ba)

    if (any.`is`(Packets.ClientPacket.LoginPacket::class.java)) {
        println("works")
        val specific = any.unpack(Packets.ClientPacket.LoginPacket::class.java)
        println(specific)
    }

    val spec = any.unpack(ClientPackets.inferType(any).clazz) as Packets.ClientPacket.LoginPacket
    println("spec: $spec")


}