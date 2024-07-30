package com.example.learningenglish.user.userinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun GenderSelectionDialog(
    onDismiss: () -> Unit,
    onGenderSelected: (Int) -> Unit
) {
    val genderOptions = listOf("女", "男", "其它")
    var selectedOption by remember { mutableStateOf(genderOptions[0]) }

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = onDismiss,
        title = { Text("选择性别") },
        text = {
            Column {
                genderOptions.forEach { gender ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        RadioButton(
                            selected = (gender == selectedOption),
                            onClick = { selectedOption = gender }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = gender)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val selectedIndex = genderOptions.indexOf(selectedOption)
                    onGenderSelected(selectedIndex)
                    onDismiss()
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}