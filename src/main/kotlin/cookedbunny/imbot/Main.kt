package cookedbunny.imbot

import com.google.gson.GsonBuilder
import com.sun.management.OperatingSystemMXBean
import cookedbunny.imbot.config.GlobalConfig
import cookedbunny.imbot.config.getDefaultConfig
import cookedbunny.imbot.database.MongoProvider
import cookedbunny.imbot.module.command.Luck
import cookedbunny.imbot.module.command.Status
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import java.io.File
import java.lang.management.ManagementFactory
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
        "luck" to Luck,
        "求签" to Luck,

        "status" to Status,
        "状态" to Status,
    )

    object RunningStatus {
        var processedMessagesCount = 0L
        private val _startTime = System.currentTimeMillis()

        val cpuUtilization: Double
            get() = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean::class.java).cpuLoad

        val memoryTotal: Long
            get() = Runtime.getRuntime().totalMemory() / (1024 * 1024)

        val memoryFree: Long
            get() = Runtime.getRuntime().freeMemory() / (1024 * 1024)

        val runningTime: Long
            get() = System.currentTimeMillis() - _startTime
    }

    override fun onEnable() {
        val configFile = File("config.json")
        val gson = GsonBuilder().setPrettyPrinting().create()

        if (!configFile.exists()) {
            configFile.createNewFile()
            Files.writeString(configFile.toPath(), gson.toJson(getDefaultConfig()))
            this.logger.warning("Generated configuration, please configure IM Bot!")
        }

        val config = gson.fromJson(Files.readString(configFile.toPath()), GlobalConfig::class.java)

        MongoProvider.initialize(config.mongoUrl)

        val eventChannel = GlobalEventChannel.parentScope(this)
        eventChannel.subscribeAlways<GroupMessageEvent> {
            if (!config.groups.contains(group.id)) {
                return@subscribeAlways
            }
            if (message.contentToString().startsWith("/")) {
                var msg = message.contentToString()
                msg = msg.substring(1)
                val command = msg.split(" ")
                if (commandMap.containsKey(command[0])) {
                    RunningStatus.processedMessagesCount++
                    commandMap[command[0]]?.execute(this.group, this.sender,
                        if (command.size > 1) command.subList(1, command.size) else listOf()
                    )
                } else {
                    group.sendMessage("Bad command!")
                }
            }
        }
    }
}