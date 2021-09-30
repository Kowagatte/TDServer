package ca.damocles.storage.database

import ca.damocles.storage.Account
import java.util.*

/**
 * AccountDatabase Class
 *
 * This is the entrypoint for the application to modify
 * the storage of Account objects in the database.
 *
 * ***There should be no calls to specific database implementations here.***
 *
 */
class AccountDatabase {

    //The namespace of the tables storing the accounts
    private val accountTable: String = TableConstants.ACCOUNT.name

    /**
     * Get's a reference of the default empty account.
     * Used to be returned on calls to the database where nothing is found.
     * @return: a reference to an empty account.
     */
    private fun emptyAccount(): Account =
        Account(
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            "Not Found",
            "Not Found",
            ""
        )



}