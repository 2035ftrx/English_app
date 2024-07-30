package com.example.learningenglish.word.model

import com.example.learningenglish.database.WordBookEntity
import com.example.learningenglish.database.WordEntity
import com.example.learningenglish.http.study.StudyRecordWord
import com.example.learningenglish.http.word.Word
import com.example.learningenglish.http.wordbook.WordBook
import com.example.learningenglish.utils.JsonParse
import com.google.gson.annotations.SerializedName
import timber.log.Timber
import java.net.URLDecoder
import java.nio.charset.Charset


data class WordUI(
    val word: String,
    val wordRank: Int,
    val wordId: Long,
    val bookId: Long,
    val details: WordDetailsUI,
)

data class WordDetailsUI(
    val wordId: Long,
    val word: String,
    val trans: List<WordTrans>?,
    val phonetic: WordPhonetic,
    val sentences: List<WordSentences>,
)

data class WordPhonetic(
    val uk: String?,
    val us: String?,
)


data class WordSentence(

    @SerializedName("sentences") var sentences: ArrayList<WordSentences> = arrayListOf(),
    @SerializedName("desc") var desc: String? = null

)

data class WordSentences(
    @SerializedName("sContent") var content: String? = null,
    @SerializedName("sCn") var trans: String? = null
)


data class WordTrans(

    @SerializedName("pos") val pos: String?,
    @SerializedName("tranCn") val tranCn: String?,
    @SerializedName("descCn") val descCn: String?,

    )


data class WordInfo(

    @SerializedName("usphone") val usphone: String?,
    @SerializedName("ukphone") val ukphone: String?,
    @SerializedName("sentences") val sentences: ArrayList<WordSentences> = arrayListOf(),
    @SerializedName("trans") val trans: List<WordTrans> = arrayListOf(),

    )

fun WordBookEntity.toUI(): WordBookUI {
    return WordBookUI(
        id = id,
        picUrl = picUrl,
        title = title,
        wordNum = wordNum,
        tags = tags,
    )
}

data class WordEdit(val id: Long, val word: String, val info: String)

fun Word.toEdit(): WordEdit {
    return WordEdit(
        id = id,
        word = headWord,
        info = word
    )
}
fun Word.toUI(): WordUI {

    Timber.d(" word decode json before : $word")
    val json = URLDecoder.decode(word, "utf-8")
    Timber.d(" word decode json after : $json")
    val wordInfo: WordInfo = JsonParse.fromJson<WordInfo>(json, WordInfo::class.java)

    return WordUI(
        word = headWord,
        wordRank = wordRank,
        wordId = id,
        bookId = bookId,
        details = WordDetailsUI(
            wordId = id,
            word = headWord,
            trans = wordInfo.trans,
            phonetic = WordPhonetic(
                uk = wordInfo.ukphone,
                us = wordInfo.usphone
            ),
            sentences = wordInfo.sentences
        )
    )
}

fun StudyRecordWord.toUI(): WordUI {

    val wordInfo: WordInfo = JsonParse.fromJson<WordInfo>(word, WordInfo::class.java)

    return WordUI(
        word = headWord,
        wordRank = wordRank,
        wordId = wordId,
        bookId = bookId,
        details = WordDetailsUI(
            wordId = wordId,
            word = headWord,
            trans = wordInfo.trans,
            phonetic = WordPhonetic(
                uk = wordInfo.ukphone,
                us = wordInfo.usphone
            ),
            sentences = wordInfo.sentences
        )
    )
}

fun WordBook.toUI(): WordBookUI {
    return WordBookUI(
        id = id,
        title = title,
        picUrl = picUrl,
        tags = tags,
        wordNum = wordNum
    )
}

fun Word.toEntity(wordBook: WordBookEntity): WordEntity {
    return WordEntity(
        id = id,
        word = headWord,
        info = "",
        bookId = bookId,
        wordBookName = wordBook.title
    )
}

fun WordBookUI.toEntity(): WordBookEntity {
    return WordBookEntity(
        id = id,
        picUrl = picUrl,
        title = title,
        wordNum = wordNum,
        tags = tags,
    )
}

fun WordBook.toEntity(): WordBookEntity {
    return WordBookEntity(
        id = id,
        picUrl = picUrl,
        title = title,
        wordNum = wordNum,
        tags = tags,
    )
}

data class StudyPlan(
    val bookId: Long,
    val dailyWords: Int,
    val totalDays: Int,
)

data class LearningPlanEntity(
    val bookId: Long,
    val wordCount: Int,
    val dailyWordCount: Int,
    val startDate: Long
)

data class SelectedBookUI(val book: WordBookUI, val isSelected: Boolean)

data class WordBookUI(
    val id: Long,
    val picUrl: String,
    val title: String,
    val wordNum: Int,
    val tags: String,
)