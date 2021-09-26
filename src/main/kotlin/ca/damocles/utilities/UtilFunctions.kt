package ca.damocles.utilities

import java.util.*

/**
 * Generates a pseudo-random alphanumeric string of a
 * predetermined size.
 * @param length: the length of the randomized string, defaults to 4 if no length is given.
 * @return: a pseudo-randomly generated alphanumeric string.
 */
fun generateAlphaString(length: Int = 4): String{
    val builder = StringBuilder()
    val random = Random()
    val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWYXZ0123456789".toCharArray()
    for(i: Int in 1..length){
        builder.append(characters[random.nextInt(characters.size)])
    }
    return builder.toString()
}