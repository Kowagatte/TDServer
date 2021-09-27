package ca.damocles.storage.database

import com.google.gson.JsonObject

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
     * @param key: the parameter being compared.
     * @param value: the value being checked for in the parameter.
     * @param size: the size of the response we want to cap it at.
     * @return: a list of related records.
     */
    fun query(table: String, key: String, value: Any, size: Int = 20): List<JsonObject>

    /**
     * Query is used to search for multiple records of a specific parameter.
     * @param condition: a pair of values where the left side is the parameter
     * and the right side is the value being checked.
     * @param size: the size of the response we want to cap it at.
     * @return: a list of related records.
     */
    fun query(table: String, condition: Pair<String, Any>, size: Int = 20): List<JsonObject> =
        query(table, condition.first, condition.second , size)

    /**
     * Finds the most relevant record of a specific parameter.
     * @param key: the parameter being searched for.
     * @param value: the value of the param being checked.
     * @return: the most relevant record.
     */
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