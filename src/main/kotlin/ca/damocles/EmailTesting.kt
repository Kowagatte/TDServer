package ca.damocles

import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.Properties
import javax.mail.*


fun main(){
    val recipient = "nnryanp@gmail.com"
    val subject = "Point proven"
    val args: Array<String> = arrayOf("")
    val fromEmail = "no-reply@damocles.ca"
    val password = ",lujnW}6`32a8Wr|"
    val toEmail: String = recipient

    val props = Properties()
    props["mail.smtp.host"] = "mail.privateemail.com"
    props["mail.smtp.socketFactory.port"] = "465"
    props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
    props["mail.smtp.auth"] = "true"
    props["mail.smtp.port"] = "465"

    val auth: Authenticator = object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication? {
            return PasswordAuthentication(fromEmail, password)
        }
    }

    val session = Session.getDefaultInstance(props, auth)
    try {
        val message: Message = MimeMessage(session)
        message.setFrom(InternetAddress("no-reply@damocles.ca"))
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
        message.subject = subject
        val text: String = args.joinToString { "\n" }
        message.setText(text)
        Transport.send(message)
        println("Email sent successfully")
    } catch (e: MessagingException) {
        println("Shit broke")
    }
}


