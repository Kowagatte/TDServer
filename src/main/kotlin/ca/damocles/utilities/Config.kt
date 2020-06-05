package ca.damocles.utilities

import ca.damocles.Server
import com.google.gson.Gson
import java.io.*
import kotlin.collections.HashMap

class ConfigFile(private val filename: String){

    var values: HashMap<String, Any> = load(filename)

    fun save(){
        try{
            val writer = FileWriter(Server.serverPath+filename)
            val gson = Gson()
            writer.write(gson.toJson(values))
            writer.flush()
            writer.close()
        }catch(e: IOException){ e.printStackTrace() }
    }

    private fun load(filename: String): java.util.HashMap<String, Any> {
        val reader: InputStream = try{
            FileInputStream(File(Server.serverPath+filename))
        }catch(e: FileNotFoundException){
            Server::class.java.getResourceAsStream("/configs/$filename")
        }
        try {
            val builder = StringBuilder()
            val gson = Gson()
            var character: Int
            while (reader.read().also { character = it } != -1) {
                builder.append(character.toChar())
            }
            reader.close()
            return gson.fromJson(builder.toString(), java.util.HashMap::class.java) as java.util.HashMap<String, Any>
        } catch (exception: IOException) { exception.printStackTrace() }
        return java.util.HashMap<String, Any>()
    }

}