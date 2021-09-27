package ca.damocles.storage.database

import ca.damocles.utilities.Files
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mongodb.client.*
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import org.bson.Document
import org.jongo.Jongo

/**
 * MongoDB Database Implementation
 *
 * This should be a connection point between
 * MongoDB specific database calls and our application
 * database CRUD methods.
 */
class MongoDatabase : DatabaseDriver {

    private val gson: Gson = Gson()
    private val mongoURI: String
    private val DATABASE_NAME: String = "damocles"

    init{
        val username = Files.passFile.getJsonObject("mongodb").getAsJsonPrimitive("username").asString
        val password = Files.passFile.getJsonObject("mongodb").getAsJsonPrimitive("password").asString
        mongoURI = "mongodb+srv://$username:$password@damocles-fcf0g.mongodb.net/test?retryWrites=true&w=majority"
    }

    private val mongoClient: MongoClient = MongoClients.create(mongoURI)
    private val database: MongoDatabase = mongoClient.getDatabase(DATABASE_NAME)

    /**
     * Query is used to search for multiple records of a specific parameter.
     * @param key: the parameter being compared.
     * @param value: the key being checked for in the parameter.
     * @param size: the size of the response we want to cap it at.
     * @return: a list of related records.
     */
    override fun query(table: String, key: String, value: Any, size: Int): List<JsonObject> {
        val list: MutableList<JsonObject> = mutableListOf()
        val documents: MongoCursor<Document> = database.getCollection(table).find(eq(key, value)).limit(size).iterator()
        while(documents.hasNext()){
            list.add(gson.fromJson(documents.next().toJson(), JsonObject::class.java))
        }
        return list.toList()
    }

    /**
     * Finds the most relevant record of a specific parameter.
     * @param key: the parameter being searched for.
     * @param value: the key of the param being checked.
     * @return: the most relevant record.
     */
    override fun find(table: String, key: String, value: Any): JsonObject? {
        val document = database.getCollection(table).find(eq(key, value)).limit(1).first()
        return if(document != null){
            gson.fromJson(document.toJson(), JsonObject::class.java)
        }else{
            null
        }
    }

    /**
     * Updates a list of values in a record where a condition is met.
     * @param condition: a pair of values where the left side is the parameter
     * and the right side is the value being checked.
     * @param values: a list of parameters being changed and their corresponding new values.
     * @return: if the operation was successful or not.
     */
    override fun update(table: String, condition: Pair<String, Any>, values: List<Pair<String, Any>>): Boolean {
        TODO("Not yet implemented")
    }

    override fun insert() {
        TODO("Not yet implemented")
    }

    /**
     * Drops entries matching the given condition.
     * @param condition: a pair of values where the left side is the parameter
     * and the right side is the value being checked.
     * @return: the amount of dropped entries.
     */
    override fun remove(table: String, condition: Pair<String, Any>): Int {
        TODO("Not yet implemented")
    }
}