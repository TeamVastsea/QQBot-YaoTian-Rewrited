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

    private val mascot = listOf(
        "宫水三叶", "赈早见琥珀主", "末永未来", "凉宫春日",
        "神里绫华", "山田凉", "姬坂乃爱", "白咲花",
        "小鸟游六花", "绪山真寻", "雪球", "天野阳菜",
        "宫水三叶", "荻野千寻", "四宫辉夜", "藤原千花",
        "御坂美琴", "白井黑子", "初春饰利", "可可萝",
        "百地希留耶", "后藤一里", "博丽灵梦", "雾雨魔理沙"
    )

    private val positive = mapOf(
        "Cosplay" to "会被集邮", "刷题" to "停不下来",
        "请教问题" to "获得大佬的解答", "开电脑" to "电脑的状态也很好",
        "找对象" to "心仪的对象就在眼前", "写程序" to "编译一次过, 运行无报错",
        "水博客" to "文思泉涌", "和大佬贴贴" to "大佬也喜欢你",
        "做数学题" to "神童附体", "女装发在群里" to "会被群友撅",
        "向雪球乞讨" to "可以获得一块钱", "收拾房间" to "干劲十足",
    )

    private val negative = mapOf(
        "抽卡" to "小保底会歪", "写作文" to "不知所云, 离题千里",
        "焊接电路板" to "电阻值漂飞, 电感在唱歌", "修电脑" to "水冷漏液",
        "和异性聊天" to "被嫌弃", "补作业" to "补错科目",
        "勾心斗角" to "被小人算计", "涩涩" to "被骂hentai",
        "装系统" to "启动引导BOOM", "写C++" to "会被野指针制裁",
        "调戏雪球" to "被收入池塘", "打舞萌DX" to "拼机的居然是男同？",
    )

    override suspend fun execute(group: Group, sender: Member, args: List<String>) {
        if (args.isNotEmpty()) {
            group.sendMessage("用法: /求签")
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
        val formattedMessage = "${greet}${sender.nick}, 你今天的运势是\n⌈${word}⌋  (点数：${luckNum.toInt()})\n\n" +
                "宜：${positiveTitle} (${positiveComment})\n忌：${negativeTitle} (${negativeComment})\n今日的吉祥物是 $mascotToday"
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
