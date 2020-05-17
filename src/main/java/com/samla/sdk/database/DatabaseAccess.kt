package com.samla.sdk.database

interface DatabaseAccess {
    fun create(apiKey: String, data: UserflowData)
    fun read(apiKey: String, data: UserflowData)
    fun update(apiKey: String, data: UserflowData)
    fun delete(apiKey: String, data: UserflowData)
}
