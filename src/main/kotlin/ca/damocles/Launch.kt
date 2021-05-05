package ca.damocles

import ca.damocles.utilities.JsonFile

fun main(args: Array<String>) {
    val passFile = JsonFile.openJsonFile("passwords.json")
    System.setProperty("javax.net.ssl.keyStore", "serverkeystore.pfx")
    System.setProperty("javax.net.ssl.keyStorePassword", passFile.getString("keystore"))
    Server.start()
}
