package com.example.learningenglish.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.learningenglish.ui.view.TState

enum class DialogValue {
    Open,
    Close
}

@Stable
class DialogState(initialValue: DialogValue) : TState<DialogValue>(initialValue) {

    fun isOpen(): Boolean {
        return value.value == DialogValue.Open
    }

    fun open() {
        value.value = DialogValue.Open
    }

    fun close() {
        value.value = DialogValue.Close
    }

    companion object {
        fun Saver() = androidx.compose.runtime.saveable.Saver<DialogState, DialogValue>(
            save = { it.currentValue },
            restore = { DialogState(it) }
        )
    }
}

@Composable
fun rememberDialogState(initialValue: DialogValue = DialogValue.Close): DialogState {
    return rememberSaveable(saver = DialogState.Saver()) { DialogState(initialValue) }
}
