package ca.damocles.utilities

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

/**
 * Object Mail
 *
 * Used to send automated emails from the given email address credentials.
 * Uses the privateemail smtp servers and SSL.
 */
object Mail {

    val fromEmail: String
    val password: String
    private val props = Properties()

    init{
        val passwordFile = JsonFile.openJsonFile("passwords.json", true)
        fromEmail = passwordFile.getJsonObject("email")["address"].asString
        password = passwordFile.getJsonObject("email")["password"].asString

        props["mail.smtp.host"] = "mail.privateemail.com"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.port"] = "465"
    }

    /**
     * Anonymous declaration of an Authenticator object.
     */
    private val auth: Authenticator = object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(fromEmail, password)
        }
    }

    /**
     * Converts an array of Strings to a single String
     * Concatenated with newlines between each array element.
     * @param content: the array to be concatenated together, must be an array of Strings.
     * @return: the generated string from the array.
     */
    private fun arrayToString(content: Array<String>): String{
        val stringBuilder = StringBuilder()
        for(line in content){
            stringBuilder.append(line)
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }

    /**
     * Sends an email from the configured email to the specified email,
     * with the given subject line and content.
     * @param toEmail: the email address the email will be sent to.
     * @param subject: the subject header of the email.
     * @param content: an array of String that are the content of the email.
     * @return: true if the message was sent correctly, false if sending failed.
     */
    fun send(toEmail: String, subject: String, content: Array<String> = arrayOf("")): Boolean{
        val session = Session.getDefaultInstance(props, auth)
        return try {
            val message: Message = MimeMessage(session)
            message.setFrom(InternetAddress(fromEmail))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
            message.subject = subject
            message.setText(arrayToString(content))
            Transport.send(message)
            true
        } catch (e: MessagingException) {
            false
        }
    }
}

/**
 * Test driver for automated email delivery.
 */
fun main(){
    val recipient = "nnryanp@gmail.com"
    val subject = "Testing automated emails"

    if(Mail.send(recipient, subject, arrayOf("Testing"))){
        println("Email sent.")
    }else{
        println("Error Occurred.")
    }
}