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

    /**
     * Static methods belonging to JsonFile.
     */
    companion object{

        /**
         * Opens a json file that may or may not exist, to open a file in the resource folder
         * it MUST exist, we do not support the creation of resource files during runtime.
         *
         * If you are accessing a JsonFile in the systems file system, you can simply
         * just instantiate the JsonFile class with the path to the given file.
         *
         * @param path: a string representation for the path of the jsonfile. Must include .json trailing,
         * if it is in the resource folder you should not add any leading forward slashes or absolute paths
         * to the src folder. If you are opening a file on the systems file system it must be an absolute path,
         * or a relative path to where the jarfile is being run.
         *
         * @param inResourceFolder: a boolean value representing if the given file is in the resourceFolder
         *
         * @return: a JsonFile representing the file at the given path. Which is a wrapper
         * of a JavaIO file, so it can not exist, and be a directory. Being a directory is not a
         * good idea and will cause issues when saving.
         *
         * @throws FileNotFoundException: If inResourceFolder is true, and the file does not exist
         * this method will throw an Exception.
         */
        fun openJsonFile(path: String, inResourceFolder: Boolean = false): JsonFile{
            return if(inResourceFolder){
                val url = JsonFile::class.java.getResource("/$path")
                if(url != null){
                    JsonFile(url.path)
                }else{
                    throw FileNotFoundException("The file $path does not exists in the resource folder")
                }
            }else{
                JsonFile(path)
            }
        }
    }

    //Sets up pretty json printing for saving to a file
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    //The JavaIO file we are wrapping
    private val file: File = File(path)
    //The jsonObject to be read and saved to the file.
    private val jsonObject: JsonObject

    /**
     * Instantiates the file on the filesystem
     * and loads any json if it is there.
     */
    init{
        jsonObject = if(exists()){
            val reader = BufferedReader(FileReader(file))
            try {
                gson.fromJson(reader, JsonObject::class.java)
            }catch(e: JsonIOException){
                println("Could not parse")
                JsonObject()
            }
        }else{
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
    fun get(key: String): Any?{
        return jsonObject.get(key)
    }

    /**
     * Gets the value at the given key from the file as a JsonPrimitive
     * @param key: the key used to retrieve the corresponding value
     * @return: the value at the specified key as a JsonPrimitive
     * @throws IllegalStateException: if the value at the given string is not a primitive value
     */
    fun getAsJsonPrimitive(key: String): JsonPrimitive = jsonObject[key].asJsonPrimitive

    /**
     * Accessor for String value
     * @param key: the key representing the String value wanted
     * @return: the String value found at the given key.
     * @throws UnsupportedOperationException: if the found object cannot be represented as a string.
     */
    fun getString(key: String): String = jsonObject[key].asString

    /**
     * Accessor for Number value
     * @param key: the key representing the Number value wanted
     * @return: the Number value found at the given key.
     * @throws UnsupportedOperationException: if the found object cannot be represented as a Number.
     */
    fun getNumber(key: String): Number = jsonObject[key].asNumber

    /**
     * Accessor for Boolean value
     * @param key: the key representing the Boolean value wanted
     * @return: the Boolean value found at the given key.
     * @throws UnsupportedOperationException: if the found object cannot be represented as a Boolean.
     */
    fun getBoolean(key: String): Boolean = jsonObject[key].asBoolean

    /**
     * Returns a casted nested JsonObject from the file with the specific key.
     * @param key: the string key representation of the nested JsonObject
     * @return: returns the nested JsonObject, returns an empty object if it does not exist.
     */
    fun getJsonObject(key: String): JsonObject{
        val obj = jsonObject.get(key)
        return if (obj is JsonObject) obj else JsonObject()
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
        if(!exists()) file.createNewFile()
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

    /**
     * @return: a prettified string representation of the json file.
     */
    fun toPrettyString(): String = gson.toJson(jsonObject)

}