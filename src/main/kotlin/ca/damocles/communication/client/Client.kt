package ca.damocles.communication.client

import ca.damocles.Server
import ca.damocles.communication.Packet
import ca.damocles.storage.Account
import ca.damocles.storage.AccountDatabase
import kotlinx.coroutines.*
import java.io.*
import java.util.*
import javax.net.ssl.SSLSocket
import kotlin.coroutines.CoroutineContext

class EstablishedConnection(private val connectionSocket: SSLSocket){

    val account: Account = AccountDatabase.getAccountByUUID(UUID.fromString("00000000-0000-0000-0000-000000000000"))
    private val coroutine: CoroutineContext
    private val bufferedWriter: BufferedWriter = BufferedWriter(OutputStreamWriter(connectionSocket.outputStream))
    private val bufferedReader: BufferedReader = BufferedReader(InputStreamReader(connectionSocket.inputStream))

    init{
        coroutine = GlobalScope.launch {
            while(true){
                try{
                    var line: String = getMessageFromClient()
                    TODO("Process( Line -> Packet ) : Then -> Handle the packet.")
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
        TODO("Send disconnect packet.")
    }

    fun send(packet: Packet){
        bufferedWriter.write(packet.toString())
        bufferedWriter.newLine()
        bufferedWriter.flush()
    }

}