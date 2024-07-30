package com.example.learningenglish.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.learningenglish.ui.theme.BackgroundColorD8D8D8
import com.example.learningenglish.ui.theme.TextColor3D3D3D
import com.example.learningenglish.ui.theme.TextColorC7C7C7

@Composable
fun GetInputDialog(
    dialogState: DialogState,
    title: String,
    hint: String = "",
    defaultInput: String = "",
    onDismiss: () -> Unit,
    onConfirm: (input: String) -> Unit
) {
    if (dialogState.isOpen()) {
        Dialog(onDismissRequest = {
            onDismiss()
        }, content = {
            GetDialogContentLayout(title, hint, defaultInput, onConfirm, onDismiss)
        })
    }
}

@Preview
@Composable
private fun PreviewInputDialogLayout() {
    GetDialogContentLayout(
        "标题文案",
        "输入提示文本",
        "",
        { i -> }) {}
}

@Composable
private fun GetDialogContentLayout(
    title: String = "",
    hint: String = "",
    defaultInput: String,
    onConfirm: (input: String) -> Unit,
    onCancel: () -> Unit
) {
    val userInputText = remember { mutableStateOf(defaultInput) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .background(Color.White, RoundedCornerShape(15.dp))
    ) {
        val (titleView, inputView, cancelView, confirmView, line1, line2) = createRefs()
        Text(text = title,
            textAlign = TextAlign.Center, color = TextColor3D3D3D, fontSize = 18.sp,
            modifier = Modifier
                .padding(top = 15.dp)
                .constrainAs(titleView) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                })
        BasicTextField(value = userInputText.value, onValueChange = {
            userInputText.value = it
        }, decorationBox = { innerTextField ->
            Row {
                Box {
                    if (userInputText.value.isEmpty()) {
                        Text(text = hint, fontSize = 16.sp, color = TextColorC7C7C7)
                    }
                    innerTextField()
                }
            }
        }, modifier = Modifier
            .padding(15.dp)
            .constrainAs(inputView) {
                top.linkTo(titleView.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            })
        Box(modifier = Modifier
            .height(0.5.dp)
            .background(BackgroundColorD8D8D8)
            .constrainAs(line1) {
                top.linkTo(inputView.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            })
        TextButton(
            onClick = { onCancel() },
            modifier = Modifier
                .constrainAs(cancelView) {
                    top.linkTo(line1.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(confirmView.start)
                    width = Dimension.fillToConstraints
                },
        ) {
            Text(
                text = "取消",
                textAlign = TextAlign.Center, color = TextColor3D3D3D, fontSize = 16.sp
            )
        }
        TextButton(onClick = { onConfirm(userInputText.value) },
            modifier = Modifier
                .constrainAs(confirmView) {
                    top.linkTo(line1.bottom)
                    start.linkTo(cancelView.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }) {
            Text(
                text = "确定", fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center, color = TextColor3D3D3D, fontSize = 16.sp
            )
        }
        Box(modifier = Modifier
            .width(0.5.dp)
            .background(BackgroundColorD8D8D8)
            .constrainAs(line2) {
                top.linkTo(line1.bottom)
                start.linkTo(confirmView.start)
                bottom.linkTo(parent.bottom)
                height = Dimension.fillToConstraints
            })
    }
}