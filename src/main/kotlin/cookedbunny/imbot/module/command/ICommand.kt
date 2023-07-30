package cookedbunny.imbot.module.command

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member

interface ICommand {

    suspend fun execute(group: Group, sender: Member, args: List<String>)
}