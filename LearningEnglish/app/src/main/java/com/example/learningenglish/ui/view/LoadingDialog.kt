package com.example.learningenglish.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber


@Preview(showBackground = true, backgroundColor = 0xFFFF0000)
@Composable
private fun PreviewLoadingContent() {
    Column {
        LoadingDialogContent(
            rememberLoadingDialogState().apply {
                show(LoadingDialogData("test1", true))
            })
        LoadingDialogContent(
            rememberLoadingDialogState().apply {
                show(LoadingDialogData("test2", false))
            })
    }
}

sealed interface LoadingDialogEvent {
    object Cancel : LoadingDialogEvent
    object Confirm : LoadingDialogEvent
}

@Composable
fun rememberLoadingDialogState(data: LoadingDialogData? = null) =
    remember { LoadingDialogState(data) }

data class LoadingDialogData(
    val tips: String = "",
    val dismissOnClickOutside: Boolean = false,
)

class LoadingDialogState(data: LoadingDialogData? = null) {
    private val show = mutableStateOf(false)
    private val data = mutableStateOf<LoadingDialogData?>(data)
    private var event = MutableSharedFlow<LoadingDialogEvent>()

    fun subEvent(): Flow<LoadingDialogEvent> {
        return event.onEach {
            Timber.d(" on dialog event : $it ")
        }
    }

    fun clearData() {
        this.data.value = null
    }

    fun show(data: LoadingDialogData? = null) {
        data?.let {
            this.data.value = data
        }
        show.value = true
    }

    fun dismiss() {
        show.value = false
    }

    fun isShow(): Boolean {
        return show.value
    }

    fun getInputData(): LoadingDialogData? {
        return data.value
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingDialog(state: LoadingDialogState) {
    if (state.isShow()) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { state.dismiss() },
            properties = DialogProperties()
        ) {
            LoadingDialogContent(state)
        }
    }
}

@Composable
fun LoadingDialogContent(
    state: LoadingDialogState,
) {
    Column(
        modifier = Modifier
            .background(color = Color.White, shape = RoundedCornerShape(20.dp))
            .padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressIndicator()

        state.getInputData()?.tips?.let {
            Text(it, fontWeight = FontWeight.SemiBold, color = Color.Black, fontSize = 18.sp)
        }

    }
}