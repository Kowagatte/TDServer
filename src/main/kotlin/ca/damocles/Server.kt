package ca.damocles

import ca.damocles.communication.client.EstablishedConnection
import ca.damocles.storage.Database
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.*
import java.net.URLDecoder
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLServerSocketFactory
import javax.net.ssl.SSLSocket
import javax.xml.bind.JAXBElement

object Server{

    lateinit var serverSocket: SSLServerSocket
    val listOfEstablishedConnections: MutableList<EstablishedConnection> = ArrayList()
    private const val maxClientConnections: Int = 3500
    var isRunning: Boolean = true

    val serverPath: String
        get() {
            return try{
                URLDecoder.decode(Server::class.java.protectionDomain.codeSource.location.path, "UTF-8")
            }catch(e: UnsupportedEncodingException){
                e.printStackTrace()
                "NO PATH"
            }
        }

    fun start(){
        try{
            serverSocket = SSLServerSocketFactory.getDefault().createServerSocket(8989) as SSLServerSocket
        }catch(e: IOException){
            println("Server failed to start, perhaps there is already a server open on this port?")
            e.printStackTrace()
        }
    }

    fun isFull(): Boolean = listOfEstablishedConnections.size >= maxClientConnections

}

object ClientGate{
    var open = true
    fun start(){
        GlobalScope.launch {
            while(open){
                val clientSocket: SSLSocket
                try{
                    clientSocket = Server.serverSocket.accept() as SSLSocket
                    val connection = EstablishedConnection(clientSocket)
                    if(Server.isFull()){
                        connection.disconnect()
                    }else{
                        Server.listOfEstablishedConnections.add(connection)
                    }
                }catch(e: IOException){
                    break
                }
            }
        }
    }
}