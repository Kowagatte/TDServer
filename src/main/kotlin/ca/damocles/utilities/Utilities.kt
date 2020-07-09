package ca.damocles.utilities

import java.util.*

fun generateAlphaString(length: Int = 4): String{
    val builder = StringBuilder()
    val random = Random()
    val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWYXZ0123456789".toCharArray()
    for(i: Int in 1..length){
        builder.append(characters[random.nextInt(characters.size)])
    }
    return builder.toString()
}