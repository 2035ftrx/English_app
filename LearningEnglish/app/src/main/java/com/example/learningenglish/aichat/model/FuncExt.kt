package com.example.learningenglish.aichat.model

import com.example.learningenglish.aichat.database.AIChatConversation
import com.example.learningenglish.aichat.database.AIChatRecord
import java.text.SimpleDateFormat
import java.util.Locale

val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

fun AIChatConversation.toUI(): ChatConversationUI {
    return ChatConversationUI(
        id = id,
        title = title,
        time = simpleDateFormat.format(createdAt)
    )
}

fun AIChatRecord.toUI(): ChatRecordUI {
    return ChatRecordUI(
        id = id,
        msg = msg,
        time = simpleDateFormat.format(createdAt),
        role = role.toRole(),
        status = status.toStatus()
    )
}


fun Int.toRole(): IChatRole {
    return when (this) {
        1 -> IChatRole.User
        2 -> IChatRole.Assistant
        else -> IChatRole.User
    }
}

fun Int.toStatus(): IChatRecordStatus {
    return when (this) {
        1 -> IChatRecordStatus.Success
        2 -> IChatRecordStatus.Failure
        3 -> IChatRecordStatus.Loading
        else -> IChatRecordStatus.Success
    }
}