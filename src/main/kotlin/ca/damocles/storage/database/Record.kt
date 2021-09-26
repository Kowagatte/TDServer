package ca.damocles.storage.database

/**
 * Interface Record
 *
 * Used as an interface to have multiple common methods
 * for different database records.
 */
interface Record {

    /**
     * Returns the key of the record
     * @return: String representing the key of the record
     */
    fun key(): String

    /**
     * Returns the value of the record
     * @return: An object stored by the given record
     */
    fun value(): Any

}