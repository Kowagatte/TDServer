package ca.damocles.server

import ca.damocles.server.client.EstablishedConnection
import ca.damocles.utilities.Files
import java.io.*
import java.net.ServerSocket
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLServerSocketFactory
import kotlin.system.exitProcess

/**
 * Object Server
 *
 * Controls the general process of the server and stores some
 * configuration settings such as port, limits on connections
 * and the sockets type.
 */
object Server{

    lateinit var serverSocket: ServerSocket
    private lateinit var ServerThread: CommandHandler
    //TODO change to a Hashed List with an identifier key attached to an established connection.
    val listOfEstablishedConnections: MutableList<EstablishedConnection> = ArrayList()
    private val maxClientConnections: Int
    private val port: Int
    val isRunning: Boolean
        get() = ServerThread.isRunning
    val amountOfPlayers: Int
        get() = listOfEstablishedConnections.size

    init{
        maxClientConnections = Files.properties.getNumber("max_connections").toInt()
        port = Files.properties.getNumber("port").toInt()
    }

    /**
     * Starts the server socket, the main thread and the ClientGate.
     */
    fun start(){
        try{
            serverSocket = ServerSocket(port)
            ClientGate.start()
            println("Started")
        }catch(e: IOException){
            println("Server failed to start, perhaps there is already a server open on this port?")
            e.printStackTrace()
        }
        ServerThread = CommandHandler(true)
        ServerThread.start()
    }

    /**
     * Stops the server, this should cause the process to end,
     * if you only want to stop clients from connecting stop the ClientGate.
     */
    fun stop(){
        ServerThread.isRunning = false
        exitProcess(0)
    }

    /**
     * Returns if the server is currently full of connections.
     * @return: true if the amountOfPlayers connected is greater than
     * the max amount of connections, false otherwise.
     */
    fun isFull(): Boolean = amountOfPlayers >= maxClientConnections

}