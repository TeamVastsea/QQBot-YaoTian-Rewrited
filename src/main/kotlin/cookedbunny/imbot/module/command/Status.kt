package cookedbunny.imbot.module.command

import cookedbunny.imbot.PluginMain
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member

object Status: ICommand {

    override suspend fun execute(group: Group, sender: Member, args: List<String>) {
        if (args.isNotEmpty()) {
            group.sendMessage("用法: /状态")
            return
        }
        val time = System.currentTimeMillis()
        group.sendMessage(
                "[IM Bot Status Dump]\nRunning Time: ${PluginMain.RunningStatus.runningTime / 1000}s\n" +
                "Total RAM: ${PluginMain.RunningStatus.memoryTotal}MB\nUsed RAM: ${PluginMain.RunningStatus.memoryFree}MB\n" +
                "Processed Commands: ${PluginMain.RunningStatus.processedMessagesCount} element(s)\n\n" +
                "Version: ${PluginMain.description.version}\nMade with ❤️ by Rabbit0w0\n(time elapsed: ${System.currentTimeMillis() - time}ms)"
        )
    }
}