package cookedbunny.imbot.module.command

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.floor


object Luck: ICommand {

    private val mascot = listOf("宫水三叶", "赈早见琥珀主", "末永未来", "凉宫春日", "", "")

    private val positive = mapOf(
        "Cosplay" to "会被集邮",
        "刷题" to "停不下来",
        "" to "",
        "" to "",
        "" to "",
        "" to "",
        "" to "",
        "" to "",
        "" to "",
        "" to "",
        "" to "",
    )

    private val negative = mapOf(
        "抽卡" to "小保底会歪",
        "" to "",
        "" to "",
        "" to "",
        "" to "",
        "" to "",
        "" to "",
        "" to "",
        "" to "",
    )

    override suspend fun execute(group: Group, sender: Member, args: List<String>) {
        if (args.isNotEmpty()) {
            group.sendMessage("用法: 求签")
            return
        }

        val randomToday = getLuck(sender.id.toString())
        val luckNum = randomToday * 100
        val word = when {
            luckNum >= 80 -> "大吉"
            luckNum >= 60 -> "吉"
            luckNum >= 40 -> "中平"
            luckNum >= 20 -> "凶"
            else -> "大凶"
        }
        val hourNow = LocalDateTime.now().hour
        val greet = when {
            hourNow >= 18 -> "晚上好~"
            hourNow >= 14 -> "下午好~"
            hourNow >= 11 -> "中午好~"
            hourNow >= 9 -> "上午好~"
            hourNow >= 6 -> "早上好~"
            else -> "早...早上好？"
        }

        val positiveTitle = positive.keys.toList()[floor(randomToday * positive.size).toInt()]
        val positiveComment = positive[positiveTitle]

        val negativeTitle = negative.keys.toList()[floor(randomToday * negative.size).toInt()]
        val negativeComment = negative[negativeTitle]

        val mascotToday = mascot[(randomToday * mascot.size).toInt()]
        val formattedMessage = "${greet}${sender.nick}，你今天的运势是\n⌈${word}⌋ （点数：${luckNum}）\n\n" +
                "宜：${positiveTitle}（${positiveComment}）\n忌：${negativeTitle}（${negativeComment}）\n今日的吉祥物是 $mascotToday"
        group.sendMessage(formattedMessage)
    }

    private fun getLuck(bindQQ: String): Double {
        val seedCode = bindQQ + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val seedMd5: String = md5(seedCode).substring(0, 8)
        val seed = BigInteger(seedMd5, 16).toLong()
        val m = 4294967296L
        val a = 1103515245L
        val c = 12345L
        return (a * seed + c) % m / (m - 1).toDouble()
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}