package ca.damocles.utilities

import com.google.gson.JsonPrimitive
import org.junit.Test

import org.junit.Assert.*

class JsonFileTest {

    @Test
    fun exists() {
        val file = JsonFile("./test.json")
        assertEquals(false, file.exists())
    }

    @Test
    fun setValue() {
        val file = JsonFile("./test.json")
        file.setValue("test", JsonPrimitive("Testing"))
        assertEquals(JsonPrimitive("Testing"), file.get("test"))
    }

    @Test
    fun getValue() {
        val file = JsonFile("./test.json")
        assertEquals(null, file.get("nothing"))
    }

    @Test
    fun hasValue() {
        val file = JsonFile("./test.json")
        assertEquals(false, file.hasValue("nothing"))
    }

    @Test
    fun testToString() {
        val file = JsonFile("./test.json")
        file.setValue("test", JsonPrimitive("Testing"))
        assertEquals("{\"test\":\"Testing\"}", file.toString())
    }
}