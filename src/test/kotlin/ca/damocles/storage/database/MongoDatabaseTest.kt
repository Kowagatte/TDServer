package ca.damocles.storage.database

import ca.damocles.storage.Account
import org.junit.Test

import org.junit.Assert.*
import java.util.*

class MongoDatabaseTest {

    @Test
    fun query() {
        val db = MongoDatabase()
        val list = db.query(TableConstants.ACCOUNT.namespace, "rating", 1000, 1)
        list.forEach {
            println(it.toString())
        }
    }

    @Test
    fun find() {
        val db = MongoDatabase()
        val result = db.find(TableConstants.ACCOUNT.namespace, "rating", 1000)
        println(result.toString())
    }

    @Test
    fun remove(){
        val db = MongoDatabase()
        val response = db.remove(TableConstants.ACCOUNT.namespace, Pair("username", "changing"))
        assertEquals(1, response)
    }

    @Test
    fun update(){
        val db = MongoDatabase()
        val response = db.update(TableConstants.ACCOUNT.namespace, Pair("username", "testing"), listOf(Pair("username", "changing")))
        assertEquals(true, response)
    }

    @Test
    fun add(){
        val db = MongoDatabase()
        val account = Account(UUID.randomUUID(), "testing", "testing", "testing")
        db.insert(TableConstants.ACCOUNT.namespace, account.toDatabaseObject())
        println(db.find(TableConstants.ACCOUNT.namespace, "username", "testing"))
    }


}