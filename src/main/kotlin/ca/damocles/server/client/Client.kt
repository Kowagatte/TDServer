package ca.damocles.server.client

import ca.damocles.server.Server
import ca.damocles.server.Packet
import ca.damocles.server.PingPacket
import ca.damocles.server.ResponsePacket
import ca.damocles.storage.Account
import ca.damocles.storage.database.AccountDatabase
import ca.damocles.storage.authenticateLogin
import ca.damocles.storage.createAccount
import ca.damocles.utilities.generateAlphaString
import kotlinx.coroutines.*
import java.io.*
import javax.net.ssl.SSLSocket
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
class EstablishedConnection(private val connectionSocket: SSLSocket){

    var account: Account = AccountDatabase.getEmptyAccount()
    private val coroutine: CoroutineContext
    private val heartbeat: CoroutineContext
    private val bufferedWriter: BufferedWriter = BufferedWriter(OutputStreamWriter(connectionSocket.outputStream))
    private val bufferedReader: BufferedReader = BufferedReader(InputStreamReader(connectionSocket.inputStream))
    private var beating = true
    private var isRunning = true

    /**
     * Starts a coroutine that accepts incoming packets from the
     * client this socket is connected too.
     */
    init{
        coroutine = GlobalScope.launch {
            while(isRunning){
                try{
                    val line: String = getMessageFromClient()
                    if(line != ""){
                        println(line)
                        val incomingPacket: Packet = Packet.fromJson(line)
                        when(incomingPacket.type){
                            //TODO Move this to a packetHandler class
                            0.toByte() -> {
                                val response = authenticateLogin(incomingPacket.body["email"].toString(), incomingPacket.body["password"].toString())
                                if(response.first){
                                    account = response.second
                                    send(ResponsePacket(incomingPacket.identity, "Login Successful!", 200))
                                }else{
                                    send(ResponsePacket(incomingPacket.identity, "Login Not Successful!", 401))
                                }
                            }
                            1.toByte() -> {
                                disconnect()
                            }
                            2.toByte() ->{
                                val response = createAccount(incomingPacket.body["email"].toString(), incomingPacket.body["username"].toString(), incomingPacket.body["password"].toString())
                                if(response){
                                    send(ResponsePacket(incomingPacket.identity, "Created account!", 200))
                                }else{
                                    send(ResponsePacket(incomingPacket.identity, "Could not create account!", 400))
                                }
                            }
                            3.toByte() ->{
                                beating = true
                            }
                        }
                        //TODO("Process( Line -> Packet ) : Then -> Handle the packet.")
                    }
                }catch(e: IOException){
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
                send(PingPacket(generateAlphaString()))
                delay(30000L)
                if(!beating){
                    disconnect()
                }
            }
        }

    }

    /**
     * IO blocking call done in a separate context.
     */
    private suspend fun getMessageFromClient(): String =
        withContext(Dispatchers.IO){
            bufferedReader.readLine()
        }

    /**
     * Disconnects this EstablishedConnection from the server and sends
     * a disconnected packet so the client knows it was terminated.
     */
    fun disconnect(){
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
    fun send(packet: Packet){
        bufferedWriter.write(packet.toString())
        bufferedWriter.newLine()
        bufferedWriter.flush()
    }

}