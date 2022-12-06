package ca.damocles.server

/**
 * Class CommandHandler
 *
 * Thread in which CommandLine IO and Commands are processed.
 *
 * @param isRunning: if this thread is still running.
 * Setting this to false would close the console input.
 */
class CommandHandler(var isRunning: Boolean): Thread() {
    override fun run() {
        while(isRunning){
            val input: String = readLine().toString()
            Commands.values().forEach { command ->
                if(command.alias.any { it.equals(input, true) }){
                    command.execute()
                }
            }
        }
    }
}

/**
 * Enum Commands
 *
 * All commands a server administrator can input into the server.
 */
enum class Commands(val alias: List<String>){
    EXIT(listOf("exit")){
        override fun execute() {
            println("Closing the server.")
            Server.stop()
        }
    },
    CLOSE(listOf("close")){
        override fun execute() {
            ClientGate.stop()
            println("Stopped incoming connections")
        }
    },
    PLAYERS(listOf("players")){
        override fun execute() {
            Server.listOfEstablishedConnections.forEach {
                println(it.account.username)
            }
        }
    },
    AMOUNT(listOf("amount")){
        override fun execute() {
            println(Server.amountOfPlayers)
        }
    };

    /**
     * Executes code attached to the command
     */
    abstract fun execute()
}