package ca.damocles.storage.database

import ca.damocles.utilities.Files
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mongodb.client.*
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set
import com.mongodb.client.result.UpdateResult
import org.bson.Document
import org.bson.conversions.Bson
import org.jetbrains.annotations.Nullable

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
     * @param key: the key being compared.
     * @param value: the value being checked for in the key.
     * @param size: the size of the response we want to cap it at, defaults at 20, a value of 0 defaults to 1.
     * @return: a list of related records, empty if none exist.
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
     * Finds the most relevant record of a specified key and value.
     * @param key: the key being searched for.
     * @param value: the value of the key being checked.
     * @return: the most relevant record, null if one does not exist
     */
    @Nullable
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
        val collection = database.getCollection(table)
        val filter = eq(condition.first, condition.second)
        val updates: MutableList<Bson> = mutableListOf()
        for(value in values){
            updates.add(set(value.first, value.second))
        }
        return collection.updateMany(filter, combine(updates)).wasAcknowledged()
    }

    /**
     * Inserts a jsonObject into the given mongodb collection,
     * ***Currently no conflict checking***
     * ***Always returns true***
     *
     * @param table: the collection the object is being inserted into.
     * @param jsonObject: the object being inserted into the database.
     * @return: if the operation was successful or not.
     */
    override fun insert(table: String, jsonObject: JsonObject): Boolean {
        database.getCollection(table).insertOne(Document.parse(jsonObject.toString()))
        return true
    }

    /**
     * Drops entries matching the given condition.
     * @param condition: a pair of values where the left side is the parameter
     * and the right side is the value being checked.
     * @return: the amount of dropped entries.
     */
    override fun remove(table: String, condition: Pair<String, Any>): Int {
        val filter = eq(condition.first, condition.second)
        return database.getCollection(table).deleteMany(filter).deletedCount.toInt()
    }
}