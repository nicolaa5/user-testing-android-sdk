package com.samla.sdk.database

import android.content.Context

/**
 * DatabaseFactory implements the Singleton pattern for database access
 */
object DatabaseFactory {
    private var userDataAccess: DatabaseAccess? = null

    @Synchronized
    fun getDatabaseAccess(context: Context): DatabaseAccess {
        return userDataAccess ?: UserflowDatabase(context).also { userDataAccess = it}
    }
}