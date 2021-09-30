package ca.damocles.storage.database

import org.bson.Document

/**
 * TODO is this still relevant?
 */
interface Storable{
    fun toDatabaseObject(): Document{
        return Document.parse(this.toString())
    }
}

/**
 * Database Object
 *
 * Entry-point for the application to access the databases.
 * should only use methods in the DatabaseDriver directly,
 * this way if we need to move to a new database implementation we
 * can simply switch out another class that implements Database driver,
 * which is essentially an interface with all the CRUD calls.
 */
object Database{
    val database: DatabaseDriver = MongoDatabase()
}

/**
 * TODO document
 */
enum class TableConstants(namespace: String){
    ACCOUNT("account"),
    MAP("map"),
    MATCH_RECORDS("matches");
}