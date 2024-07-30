package com.example.learningenglish.word.task

import android.content.Context

class IAddBookTaskImpl(private val context: Context) : IAddBookTask {
    override fun onAdd(bookId: Long) {
        // 不主动下载单词了，出现了非常多问题。
        // DownloadWordByApiWorker.action(context, bookId)
    }
}