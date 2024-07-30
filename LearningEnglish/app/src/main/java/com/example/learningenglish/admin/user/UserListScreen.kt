package com.example.learningenglish.admin.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.learningenglish.http.admin.UserInfoADTO
import com.example.learningenglish.ui.view.rememberBooleanState

@Composable
fun UserListScreen(
    pagingFlow: LazyPagingItems<UserInfoADTO>,
    onCreateUser: (String, String) -> Unit,
    onEditUser: (UserInfoADTO) -> Unit,
    onDeleteUser: (UserInfoADTO) -> Unit,
) {
    val userPagingItems = pagingFlow

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(userPagingItems.itemCount) {
                val user = userPagingItems[it]
                user?.let {
                    UserItem(user, {
                        onEditUser(it)
                    }) {
                        onDeleteUser(it)
                    }
                }
            }
        }
        AddButton(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp), onCreateUser
        )
    }
}

@Composable
private fun AddButton(modifier: Modifier, onCreateUser: (String, String) -> Unit) {
    val showDialog = rememberBooleanState()
    if (showDialog.isTrue()) {
        UserAddDialog(onDismiss = { showDialog.beFalse() }) { username, password ->
            onCreateUser(username, password)
            showDialog.beFalse()
        }
    }
    FloatingActionButton(
        modifier = modifier,
        onClick = { showDialog.beTrue() }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "")
    }
}

@Composable
fun UserItem(user: UserInfoADTO, onEdit: (UserInfoADTO) -> Unit, onDelete: (UserInfoADTO) -> Unit) {

    val showDialog = rememberBooleanState()
    if (showDialog.isTrue()) {
        UserEditDialog(user = user, onDismiss = { showDialog.beFalse() }) { updatedUser ->
            onEdit(updatedUser)
            showDialog.beFalse()
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Username: ${user.username}")
            Text("Nickname: ${user.nickname}")
            Text("School: ${user.school}")
            Text("Grade: ${user.grade}")
            Row {
                Button(onClick = { showDialog.beTrue() }) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    onDelete(user)
                }) {
                    Text("Delete")
                }
            }
        }
    }
}
