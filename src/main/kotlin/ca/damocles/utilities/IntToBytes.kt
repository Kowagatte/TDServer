package ca.damocles.utilities

/**
 * Converts an Integer to a byteArray
 */
fun Int.toBytes(): ByteArray{
    val buffer = ByteArray(4)
    for (i in 0..3) buffer[0 + i] = (this shr (i*8)).toByte()
    return buffer
}

/**
 * Reverses the bytes of an Integer according to
 * Two's Complement.
 */
fun Int.reverseBytes(): Int{
    return this shl 24 or
            (this and 0xff00 shl 8) or
            (this ushr 8 and 0xff00) or
            (this ushr 24)
}