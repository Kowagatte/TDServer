package ca.damocles.storage.database

import org.junit.Test

import org.junit.Assert.*

class MongoDatabaseTest {

    @Test
    fun query() {
        val db = MongoDatabase()
        val list = db.query("account", "rating", 1000, 1)
        list.forEach {
            println(it.toString())
        }
    }

    @Test
    fun find() {
        val db = MongoDatabase()
        val result = db.find("account", "rating", 1000)
        println(result.toString())
    }
}