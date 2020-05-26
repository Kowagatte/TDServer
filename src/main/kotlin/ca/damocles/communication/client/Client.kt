package ca.damocles.communication.client

import ca.damocles.communication.Packet
import java.io.*
import javax.net.ssl.SSLSocket

class EstablishedConnection(val connectionSocket: SSLSocket){
    val account: Account
    val bufferedWriter: BufferedWriter = BufferedWriter(OutputStreamWriter(connectionSocket.outputStream))
    val bufferedReader: BufferedReader = BufferedReader(InputStreamReader(connectionSocket.inputStream))

    init{
    }

    fun send(packet: Packet){
        bufferedWriter.write(packet.toString())
        bufferedWriter.newLine()
        bufferedWriter.flush()
    }
}