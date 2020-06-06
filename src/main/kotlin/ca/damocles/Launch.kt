package ca.damocles

fun main(args: Array<String>) {
    System.setProperty("javax.net.ssl.keyStore", "serverkeystore.pfx")
    System.setProperty("javax.net.ssl.keyStorePassword", "oPkiCv43")
    Server.start()
}
