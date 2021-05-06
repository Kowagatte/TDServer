package ca.damocles.networking

import ca.damocles.networking.client.EstablishedConnection
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.*
import java.net.URLDecoder
import javax.net.ssl.SSLServerSocket
import javax.net.ssl.SSLServerSocketFactory
import javax.net.ssl.SSLSocket
import kotlin.concurrent.thread
import kotlin.system.exitProcess

/**
 * Object Server
 *
 * Controls the general process of the server and stores some
 * configuration settings such as port, limits on connections
 * and the sockets type.
 */
object Server{

    lateinit var serverSocket: SSLServerSocket
    val listOfEstablishedConnections: MutableList<EstablishedConnection> = ArrayList()
    private const val maxClientConnections: Int = 3500
    var isRunning: Boolean = true
    private val amountOfPlayers: Int
        get() = listOfEstablishedConnections.size

    val serverPath: String
        get() {
            return try{
                URLDecoder.decode(Server::class.java.protectionDomain.codeSource.location.path, "UTF-8")
            }catch(e: UnsupportedEncodingException){
                e.printStackTrace()
                "NO PATH"
            }
        }

    /**
     * Starts the server socket, the main thread and the ClientGate.
     */
    fun start(){
        try{
            serverSocket = SSLServerSocketFactory.getDefault().createServerSocket(8888) as SSLServerSocket
            ClientGate.start()
            println("Started")
        }catch(e: IOException){
            println("Server failed to start, perhaps there is already a server open on this port?")
            e.printStackTrace()
        }

        thread(start = true) {
            while(isRunning){
                //TODO Move to a proper command pattern and move this handler to a seperate class.
                val input = readLine()
                when(input){
                    "stop"->{
                        println("Closing the server.")
                        stop()
                    }
                    "stop connections"->{
                        ClientGate.stop()
                        println("Stopped incoming connections")
                    }
                    "players"->{
                        listOfEstablishedConnections.forEach {
                            println(it.account.username)
                        }
                    }
                    "amount"->{
                        println(amountOfPlayers)
                    }
                }
            }
        }
    }

    /**
     * Stops the server, this should cause the process to end,
     * if you only want to stop clients from connecting stop the ClientGate.
     */
    fun stop(){
        isRunning = false
        exitProcess(0)
    }

    fun isFull(): Boolean = amountOfPlayers >= maxClientConnections

}