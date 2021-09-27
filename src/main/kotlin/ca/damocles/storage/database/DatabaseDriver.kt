package ca.damocles.storage.database

import com.google.gson.JsonObject
import org.jetbrains.annotations.Nullable

/**
 * Interface DatabaseDriver
 *
 * An interface that database specific implementations will
 * interact with the rest of the application through, this way
 * switching to different database implementation will just require
 * adding a class that implements DatabaseDriver instead of replacing
 * all the hardcoded database code.
 *
 * This class contains top-level methods for retrieving/manipulating
 * data in a database implementation.
 */
interface DatabaseDriver {

    /**
     * Query is used to search for multiple records of a specific parameter.
     * @param key: the key being compared.
     * @param value: the value being checked for in the key.
     * @param size: the size of the response we want to cap it at, defaults at 20, a value of 0 defaults to 1.
     * @return: a list of related records, empty if none exist.
     */
    fun query(table: String, key: String, value: Any, size: Int = 20): List<JsonObject>

    /**
     * Query is used to search for multiple records of a specific parameter.
     * @param condition: a pair of values where the left side is the parameter
     * and the right side is the value being checked.
     * @param size: the size of the response we want to cap it at, defaults at 20, a value of 0 defaults to 1.
     * @return: a list of related records, empty if none exist.
     */
    fun query(table: String, condition: Pair<String, Any>, size: Int = 20): List<JsonObject> =
        query(table, condition.first, condition.second , size)

    /**
     * Finds the most relevant record of a specified key and value.
     * @param key: the key being searched for.
     * @param value: the value of the key being checked.
     * @return: the most relevant record, null if one does not exist
     */
    @Nullable
    fun find(table: String, key: String, value: Any): JsonObject?

    /**
     * Updates a list of values in a record where a condition is met.
     * @param condition: a pair of values where the left side is the parameter
     * and the right side is the value being checked.
     * @param values: a list of parameters being changed and their corresponding new values.
     * @return: if the operation was successful or not.
     */
    fun update(table: String, condition: Pair<String, Any>, values: List<Pair<String, Any>>): Boolean

    fun insert()

    /**
     * Drops entries matching the given condition.
     * @param condition: a pair of values where the left side is the parameter
     * and the right side is the value being checked.
     * @return: the amount of dropped entries.
     */
    fun remove(table: String, condition: Pair<String, Any>): Int

}