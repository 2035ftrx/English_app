package com.example.learningenglish.admin.user

import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.learningenglish.http.admin.UserInfoADTO
import com.example.learningenglish.ui.view.ViewSpacer
import com.example.learningenglish.ui.view.rememberIntState
import com.example.learningenglish.ui.view.rememberStringState
import timber.log.Timber

@Composable
fun UserEditDialog(
    user: UserInfoADTO,
    onDismiss: () -> Unit,
    onSave: (UserInfoADTO) -> Unit
) {
    val nickname = rememberStringState(user.nickname.orEmpty())
    val school = rememberStringState(user.school.orEmpty())
    val gender = rememberIntState(initialValue = user.gender)

    val grade = rememberStringState(user.grade.orEmpty())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit User") },
        text = {
            Column {
                OutlinedTextField(
                    value = nickname.get(),
                    onValueChange = { nickname.update(it) },
                    label = { Text("nickname") }
                )
                ViewSpacer(size = 8)
                OutlinedTextField(
                    value = school.get(),
                    onValueChange = { school.update(it) },
                    label = { Text("School") }
                )
                ViewSpacer(size = 8)
                OutlinedTextField(
                    value = grade.get(),
                    onValueChange = { grade.update(it) },
                    label = { Text("Grade") }
                )
                ViewSpacer(size = 8)
                OutlinedTextField(
                    value = gender.get().toString(),
                    onValueChange = { gender.update(it.toIntOrNull() ?: 0) },
                    label = { Text("Gender") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                Timber.d(" copy : ${nickname.get()}  ${school.get()}  ${grade.get()}  ${gender.get()} ")
                user.copy(
                    nickname = nickname.get(),
                    school = school.get(),
                    grade = grade.get(),
                    gender = gender.get(),
                ).apply{
                    Timber.d(" copy : $this ")
                }.let(onSave)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun UserAddDialog(
    onDismiss: () -> Unit,
    onSave: (username: String, password: String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add User") },
        text = {
            Column {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") }
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(username, password)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
