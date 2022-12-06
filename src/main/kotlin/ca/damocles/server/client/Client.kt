package ca.damocles.server.client

import ca.damocles.packets.ClientPackets
import ca.damocles.proto.Packets
import ca.damocles.server.Server
import ca.damocles.storage.Account
import ca.damocles.storage.database.Database
import ca.damocles.utilities.reverseBytes
import ca.damocles.utilities.toBytes
import com.google.protobuf.Any
import com.google.protobuf.GeneratedMessageV3
import kotlinx.coroutines.*
import java.io.IOException
import java.net.Socket
import java.net.SocketException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.coroutines.CoroutineContext

/**
 * Class EstablishedConnection
 *
 * This acts currently as a wrapper for the Clients Socket.
 * It also acts as the current storage of Client related information,
 * such as what account it is logged into and maybe in the future, where it should
 * be in the application process such as the main screen, in game or interacting with
 * specific components. Maybe this should be moved to a separate class? Probably.
 *
 * TODO It handles all packets sent to the server from the client, which should be moved to a packet handler class
 * as well as having the incoming packet's queued instead of having them all being resolved at once.
 */
class EstablishedConnection(private val connectionSocket: Socket){

    var account: Account = Database.accounts.emptyAccount()
    private val coroutine: CoroutineContext
    private val heartbeat: CoroutineContext
    private val inputStream = connectionSocket.inputStream
    private val outputStream = connectionSocket.outputStream
    private var beating = true
    private var isRunning = true

    /**
     * Starts a coroutine that accepts incoming packets from the
     * client this socket is connected too.
     */
    init{
        coroutine = GlobalScope.launch {
            loop@ while(isRunning){
                try{
                    val message: ByteArray = getMessageFromClient()
                    if(message.isNotEmpty()){
                        val incomingPacket: Any = Any.parseFrom(message)
                        when(ClientPackets.inferType(incomingPacket)){

                            ClientPackets.CREATE_ACCOUNT->{




                            }

                            ClientPackets.LOGIN->{
                                val packet: Packets.ClientPacket.LoginPacket = incomingPacket.unpack(ClientPackets.LOGIN.clazz) as Packets.ClientPacket.LoginPacket
                                println("Receiving Login packet")
                                println("\tEmail: ${packet.email}\n\tPassword: ${packet.password}")
                                val p = Packets.ServerPacket.ResponsePacket.newBuilder().setCode(200).setMessage("This is a response").build()
                                send(p)
                            }

                            ClientPackets.CLOSE->{

                            }

                            ClientPackets.PONG->{

                            }

                            else -> continue@loop
                        }
                    }
                }catch(e: IOException){
                    e.printStackTrace()
                    disconnect()
                }
            }
        }
        /*
        This is a coroutine that determines if this connection is
        still alive, if beating is false for 30 continuous seconds
        the server will terminate this connection.
         */
        heartbeat = GlobalScope.launch {
            while(true){
                beating = false
                //send(PingPacket(generateAlphaString()))
                delay(30000L)
                if(!beating){
                    //disconnect()
                }
            }
        }

    }

    /**
     * IO blocking call done in a separate context.
     */
    private suspend fun getMessageFromClient(): ByteArray =
        withContext(Dispatchers.IO){
            return@withContext if(inputStream.available() > 0){
                val ba = ByteArray(4)
                inputStream.read(ba)
                val size = ByteBuffer.wrap(ba).order(ByteOrder.LITTLE_ENDIAN).int
                println(size)
                val targetArray = ByteArray(size)
                inputStream.read(targetArray)
                targetArray
            }else{
                ByteArray(0)
            }
        }

    /**
     * Disconnects this EstablishedConnection from the server and sends
     * a disconnected packet so the client knows it was terminated.
     */
    fun disconnect(){
        println("killed socket")
        isRunning = false
        Server.listOfEstablishedConnections.remove(this)
        coroutine.cancel()
        heartbeat.cancel()
        //TODO("Send disconnect packet.")
        connectionSocket.close()
    }

    /**
     *
     */
    private fun send(unPacked: GeneratedMessageV3){
        val packet = Any.pack(unPacked)
        outputStream.write(packet.toByteArray().size.toBytes())
        outputStream.write(packet.toByteArray())
        outputStream.flush()
    }

}