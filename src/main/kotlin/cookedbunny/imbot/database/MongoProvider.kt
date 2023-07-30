package cookedbunny.imbot.database

import com.mongodb.kotlin.client.coroutine.MongoClient


object MongoProvider {

    private lateinit var mongoClient: MongoClient

    fun initialize(url: String) {
        mongoClient = MongoClient.create(url)
    }

    fun get(): MongoClient {
        return this.mongoClient
    }
}