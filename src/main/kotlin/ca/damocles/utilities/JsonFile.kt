package ca.damocles.utilities

import com.google.gson.*
import java.io.*

/**
 * Class JsonFile
 *
 * Saves and loads a JsonFile from the filesystem.
 * Utilizes Gson to parse the json data.
 */
class JsonFile(path: String){

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val file: File = File(path)
    private val reader: BufferedReader
    private val jsonObject: JsonObject

    /**
     * Instantiates the file on the filesystem
     * and loads any json if it is there.
     */
    init{
        file.createNewFile()
        reader = BufferedReader(FileReader(file))
        jsonObject = try {
            gson.fromJson(reader, JsonObject::class.java)
        }catch(e: JsonIOException){
            println("Could not parse")
            JsonObject()
        }
    }

    /**
     * @return: a boolean representing if the file exists in the filesystem or not.
     */
    fun exists(): Boolean = file.exists()

    /**
     * Sets the json key to the given value.
     * @param key: the key to set the value of
     * @param value: the value to be set, must be a JsonElement
     */
    fun setValue(key: String, value: JsonPrimitive){
        jsonObject.add(key, value)
    }

    /**
     * Retrieves a value from the corresponding key within the json object
     * @param key: the key used to retrieve the corresponding value
     * @return: a value from the JsonObject using the given key
     */
    fun getValue(key: String): Any{
        return jsonObject.get(key)
    }

    /**
     * Gets if the given value exists in the JsonObject
     * @param key: the key to be searched for
     * @return: true if it exists, false otherwise.
     */
    fun hasValue(key: String): Boolean = jsonObject.has(key)

    /**
     * Writes the JsonObject to the FileSystem,
     * @return: whether the operation was successful or not.
     */
    fun save(): Boolean{
        val writer = BufferedWriter(FileWriter(file))
        return try {
            gson.toJson(jsonObject, writer)
            writer.close()
            true
        }catch (e: Exception){
            false
        }
    }

    /**
     * @return: the string representation of the json file.
     */
    override fun toString(): String = jsonObject.toString()

}

fun main(){
    //TODO turn into test script.
    var file = JsonFile("C:\\Users\\craft\\Desktop\\ThisIsATest.json")
    file.setValue("Test", JsonPrimitive("Testing"))
    file.save()

    file = JsonFile("C:\\Users\\craft\\Desktop\\ThisIsATest.json")
    println(file)

}