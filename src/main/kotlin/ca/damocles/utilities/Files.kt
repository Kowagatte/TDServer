package ca.damocles.utilities

/**
 * Files Object
 *
 * All files that reside on the system.
 */
object Files {
    val passFile: JsonFile = JsonFile.openJsonFile("passwords.json", true)
    val properties: JsonFile = JsonFile.openJsonFile("config.json", true)
}