package ca.damocles.utilities

import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import java.util.Properties
import javax.mail.*

object Mail{

    const val fromEmail = "no-reply@damocles.ca"
    const val password = ",lujnW}6`32a8Wr|"
    private val props = Properties()

    init{
        props["mail.smtp.host"] = "mail.privateemail.com"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
    }

    private val auth: Authenticator = object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(fromEmail, password)
        }
    }

    /**
     * Sends an email from the configured email to the specified email,
     * with the given subject line.
     */
    fun send(toEmail: String, subject: String, content: Array<String> = arrayOf("")): Boolean{
        val session = Session.getDefaultInstance(props, auth)
        return try {
            val message: Message = MimeMessage(session)
            message.setFrom(InternetAddress(fromEmail))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
            message.subject = subject
            val text: String = content.joinToString { "\n" }
            message.setText(text)
            Transport.send(message)
            true
        } catch (e: MessagingException) {
            false
        }
    }
}

fun main(){
    val recipient = "nnryanp@gmail.com"
    val subject = "Point proven"

    if(Mail.send(recipient, subject)){
        println("Email sent.")
    }else{
        println("Error Occurred.")
    }
}


