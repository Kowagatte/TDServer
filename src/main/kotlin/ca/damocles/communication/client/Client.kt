package ca.damocles.communication.client

import ca.damocles.Server
import ca.damocles.communication.Packet
import ca.damocles.communication.ResponsePacket
import ca.damocles.storage.Account
import ca.damocles.storage.AccountDatabase
import ca.damocles.storage.authenticateLogin
import kotlinx.coroutines.*
import java.io.*
import java.util.*
import javax.net.ssl.SSLSocket
import kotlin.coroutines.CoroutineContext

class EstablishedConnection(val connectionSocket: SSLSocket){

    var account: Account = AccountDatabase.getEmptyAccount()
    private val coroutine: CoroutineContext
    private val bufferedWriter: BufferedWriter = BufferedWriter(OutputStreamWriter(connectionSocket.outputStream))
    private val bufferedReader: BufferedReader = BufferedReader(InputStreamReader(connectionSocket.inputStream))

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
                                    send(ResponsePacket(incomingPacket.identity, "Login Successful!"))
                                }else{
                                    send(ResponsePacket(incomingPacket.identity, "Login Not Successful!"))
                                }
                            }
                            1.toByte() -> {
                                disconnect()
                            }
                            2.toByte() ->{
                                TODO("CLIENT WANTS TO CREATE ACCOUNT")
                            }
                        }
                        //TODO("Process( Line -> Packet ) : Then -> Handle the packet.")
                    }
                }catch(e: IOException){
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
        //TODO("Send disconnect packet.")
    }

    fun send(packet: Packet){
        bufferedWriter.write(packet.toString())
        bufferedWriter.newLine()
        bufferedWriter.flush()
    }

}