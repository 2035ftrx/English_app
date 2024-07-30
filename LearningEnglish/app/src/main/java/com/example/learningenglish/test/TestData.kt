package com.example.learningenglish.test

import com.example.learningenglish.aichat.model.ChatConversationUI
import com.example.learningenglish.aichat.model.ChatRecordUI
import com.example.learningenglish.aichat.model.toRole
import com.example.learningenglish.aichat.model.toStatus
import com.example.learningenglish.home.TodayTaskUI
import com.example.learningenglish.word.model.StudyPlan
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.WordDetailsUI
import com.example.learningenglish.word.model.WordPhonetic
import com.example.learningenglish.word.model.WordSentences
import com.example.learningenglish.word.model.WordTrans
import com.example.learningenglish.word.model.WordUI
import kotlin.random.Random

fun randomWordSentences(): WordSentences {
    return WordSentences("Example Sentence ${Random.nextInt()} ", "例句 ${Random.nextInt()}")
}

fun randomWordTrans(): WordTrans {
    return WordTrans(
        "pos ${Random.nextInt()}  ",
        "中文意思${Random.nextInt()} ",
        "中文解释 ${Random.nextInt()} "
    )
}

fun randomWordDetailsUI(): WordDetailsUI {
    return WordDetailsUI(
        wordId = Random.nextLong(),
        word = "word${Random.nextInt()}",
        trans = List(20, { randomWordTrans() }),
        phonetic = WordPhonetic(
            uk = "uk${Random.nextInt()}",
            us = "us${Random.nextInt()}"
        ),
        sentences = List(20, { randomWordSentences() })
    )
}

fun randomWordUI(): WordUI {
    return WordUI(
        word = "word${Random.nextInt()}",
        wordRank = Random.nextInt(),
        wordId = Random.nextLong(),
        bookId = Random.nextLong(),
        details = testWordDetails.random()
    )
}

fun randomWordBookUI(): WordBookUI {
    return WordBookUI(
        id = Random.nextLong(),
        title = "单词本${Random.nextInt()}",
        picUrl = "https://img.alicdn.com/imgextra/i4/O1CN01KzY0hV1Q0JZ0K1ZJf_!!6000000006414-2-tps-1200-1200.jpg",
        wordNum = Random.nextInt(),
        tags = "sdgejtkf"
    )
}

val testWordBookName = listOf(
    "四级单词",
    "六级单词",
    "考研单词",
)

fun randomTodayTaskUI(): TodayTaskUI {
    return TodayTaskUI(
        bookName = testWordBookName.random(),
        learnedWordCount = Random.nextInt(90),
        totalWordCount = Random.nextInt(100),
        todayWordCount = Random.nextInt(109),
        todayReviewWordCount = Random.nextInt(45),
        hasPlan = true
    )
}

fun randomChatRecordUI(): ChatRecordUI {
    return ChatRecordUI(
        id = Random.nextLong(),
        msg = "msg${Random.nextInt()}",
        role = Random.nextInt(3).toRole(),
        time = "time${Random.nextInt()}",
        status = Random.nextInt(3).toStatus()
    )
}

fun randomChatConversationUI(): ChatConversationUI {
    return ChatConversationUI(
        id = Random.nextLong(),
        title = "msg${Random.nextInt()}",
        time = "time${Random.nextInt()}"
    )
}

val testPlan1 = StudyPlan(
    bookId = 1,
    dailyWords = 10,
    totalDays = 100,
)

val testWordDetails = List(20) { randomWordDetailsUI() }

val testWordData = List(20) { randomWordUI() }

val testBookData = List(20) { randomWordBookUI() }

val testTaskData = List(20) { randomTodayTaskUI() }


val testChatRecordData = List(10) { randomChatRecordUI() }

val testChatConversationData = List(10) { randomChatConversationUI() }