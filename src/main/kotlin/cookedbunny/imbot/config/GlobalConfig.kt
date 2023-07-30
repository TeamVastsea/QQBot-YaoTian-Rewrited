package cookedbunny.imbot.config

data class GlobalConfig(
    val password: String,
    val adminList: List<Long>,

    val forumApiKey: String,
    val forumBaseUrl: String,

    val mongoUrl: String,
    val mongoDatabaseName: String,
    val rabbitHost: String,
    val rabbitPort: Int,
)

fun getDefaultConfig(): GlobalConfig {
    return GlobalConfig(
        password = "",
        adminList = listOf(),
        forumApiKey = "",
        forumBaseUrl = "",
        mongoUrl = "mongodb://root@127.0.0.1:27017/",
        mongoDatabaseName = "vastsea",
        rabbitHost = "127.0.0.1",
        rabbitPort = 5672,
    )
}
