package ca.damocles

fun main(args: Array<String>) {
    System.setProperty("javax.net.ssl.keyStore", "keystore.pfx")
    System.setProperty("javax.net.ssl.keyStorePassword", "-~uE8@-A3Zr6Dy~%")
    Server.start()
}
