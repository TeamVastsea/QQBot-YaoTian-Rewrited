package cookedbunny.imbot.config

data class GlobalConfig(
    val groups: List<Long>,
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
        groups = listOf(767819300),
        adminList = listOf(),
        forumApiKey = "",
        forumBaseUrl = "",
        mongoUrl = "mongodb://127.0.0.1:27017/",
        mongoDatabaseName = "vastsea",
        rabbitHost = "127.0.0.1",
        rabbitPort = 5672,
    )
}
