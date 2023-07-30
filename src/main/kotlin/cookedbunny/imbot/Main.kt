package cookedbunny.imbot

import com.google.gson.GsonBuilder
import cookedbunny.imbot.config.GlobalConfig
import cookedbunny.imbot.config.getDefaultConfig
import cookedbunny.imbot.database.MongoProvider
import cookedbunny.imbot.module.command.Luck
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import java.io.File
import java.nio.file.Files

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "cookedbunny.imbot",
        name = "YaoTian-IM-Bot",
        version = "0.1.0"
    ) {
        author("Rabbit0w0 <rabbit0w0@outlook.com>")
        info("IM Bot for Project Vastsea")
    }
) {
    val commandMap = mapOf(
        "luck" to Luck
    )

    override fun onEnable() {
        val configFile = File("config.json")
        val gson = GsonBuilder().setPrettyPrinting().create()

        if (!configFile.exists()) {
            configFile.createNewFile()
            Files.writeString(configFile.toPath(), gson.toJson(getDefaultConfig()))
            this.logger.warning("Please configure IM Bot and start again!")
            return
        }

        val config = gson.fromJson(Files.readString(configFile.toPath()), GlobalConfig::class.java)

        MongoProvider.initialize(config.mongoUrl)

        val eventChannel = GlobalEventChannel.parentScope(this)
        eventChannel.subscribeAlways<GroupMessageEvent> {
            if (message.contentToString().startsWith("/")) {
                var msg = message.contentToString()
                msg = msg.substring(1)
                val command = msg.split(" ")
                if (commandMap.containsKey(command[0])) {
                    commandMap[command[0]]?.execute(this.group, this.sender,
                        if (command.size > 1) command.subList(1, command.size) else listOf()
                    )
                }
            }
        }
    }
}