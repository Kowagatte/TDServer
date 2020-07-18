package ca.damocles.communication.client

import ca.damocles.Server
import ca.damocles.communication.Packet
import ca.damocles.communication.PingPacket
import ca.damocles.communication.ResponsePacket
import ca.damocles.storage.Account
import ca.damocles.storage.database.AccountDatabase
import ca.damocles.storage.authenticateLogin
import ca.damocles.storage.createAccount
import ca.damocles.utilities.generateAlphaString
import kotlinx.coroutines.*
import java.io.*
import javax.net.ssl.SSLSocket
import kotlin.coroutines.CoroutineContext

class EstablishedConnection(val connectionSocket: SSLSocket){

    var account: Account = AccountDatabase.getEmptyAccount()
    private val coroutine: CoroutineContext
    private val heartbeat: CoroutineContext
    private val bufferedWriter: BufferedWriter = BufferedWriter(OutputStreamWriter(connectionSocket.outputStream))
    private val bufferedReader: BufferedReader = BufferedReader(InputStreamReader(connectionSocket.inputStream))
    private var beating = true

    init{
        coroutine = GlobalScope.launch {
            while(connectionSocket.isConnected){
                try{
                    val line: String = getMessageFromClient()
                    if(line != ""){
                        println(line)
                        val incomingPacket: Packet = Packet.fromJson(line)
                        when(incomingPacket.type){
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

    private suspend fun getMessageFromClient(): String =
            withContext(Dispatchers.Default){
                bufferedReader.readLine()
            }

    fun disconnect(){
        Server.listOfEstablishedConnections.remove(this)
        coroutine.cancel()
        heartbeat.cancel()
        //TODO("Send disconnect packet.")
    }

    fun send(packet: Packet){
        bufferedWriter.write(packet.toString())
        bufferedWriter.newLine()
        bufferedWriter.flush()
    }

}