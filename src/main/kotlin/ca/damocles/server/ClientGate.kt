package ca.damocles.server

import ca.damocles.server.client.EstablishedConnection
import java.io.IOException
import java.net.Socket
import javax.net.ssl.SSLSocket
import kotlin.concurrent.thread

/**
 * Object ClientGate
 *
 * This is a object that handles the acceptance of connecting
 * sockets. The ClientGate will accept an incoming clientSocket,
 * attach it to a newly instantiated EstablishedConnection and
 * add it to the list of EstablishedConnections.
 */
object ClientGate{
    private var isOpen = false
    private lateinit var acceptanceThread: Thread

    /**
     * Starts the ClientGate thread, which allows clients to connect to the server.
     */
    fun start(){
        if(!isOpen){
            isOpen = true
            acceptanceThread = thread{
                while(isOpen){
                    val clientSocket: Socket
                    try{
                        clientSocket = Server.serverSocket.accept()
                        val connection = EstablishedConnection(clientSocket)
                        if(Server.isFull()){
                            connection.disconnect()
                        }else{
                            Server.listOfEstablishedConnections.add(connection)
                        }
                    }catch(e: IOException){
                        continue
                    }catch(e: SecurityException){
                        continue
                    }
                }
            }
        }
    }

    /**
     * Stops the ClientGate thread which allows clients to connect to the server.
     * After calling this method no more clients will be allowed to join the server.
     */
    fun stop(){
        if(isOpen)
            isOpen = false
    }

}