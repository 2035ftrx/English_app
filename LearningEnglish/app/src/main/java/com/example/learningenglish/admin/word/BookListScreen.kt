package com.example.learningenglish.admin.word

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.learningenglish.word.model.WordBookUI
import kotlinx.coroutines.launch

@Composable
fun WordBookListScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val viewModel: AdminBooksViewModel = viewModel()
    val pagingItems = viewModel.bookPagingFlow.collectAsLazyPagingItems()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            pagingItems.refresh()
        }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(pagingItems.itemCount) {
                val wordBook = pagingItems.peek(it)
                wordBook?.let {
                    WordBookItem(modifier = Modifier.clickable {
                        launcher.launch(WordManagerActivity.action(context, wordBook.id))
                    }, wordBook, updateWordBook = {
                        coroutineScope.launch {
                            viewModel.updateWordBook(it)
                                .collect {
                                    pagingItems.refresh()
                                }
                        }
                    }, deleteWordBook = {
                        coroutineScope.launch {
                            viewModel.deleteWordBook(it.id)
                                .collect {
                                    pagingItems.refresh()
                                }
                        }
                    })
                }
            }
        }

        AddBookButton(modifier = Modifier
            .padding(16.dp)
            .align(Alignment.BottomEnd),
            addWordBook = {
                coroutineScope.launch {
                    viewModel.createWordBook(it)
                        .collect {
                            pagingItems.refresh()
                        }
                }
            })
    }
}

@Composable
private fun AddBookButton(
    modifier: Modifier = Modifier,
    addWordBook: (WordBookUI) -> Unit,
) {

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddWordBookDialog(
            onDismiss = { showDialog = false },
            onSave = { updatedWordBook ->
                addWordBook(updatedWordBook)
                showDialog = false
            }
        )
    }

    FloatingActionButton(modifier = modifier, onClick = {
        showDialog = true
    }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "")
    }

}

@Composable
fun WordBookItem(
    modifier: Modifier = Modifier,
    wordBook: WordBookUI,
    updateWordBook: (WordBookUI) -> Unit,
    deleteWordBook: (WordBookUI) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        EditWordBookDialog(
            wordBook = wordBook,
            onDismiss = { showDialog = false },
            onSave = { updatedWordBook ->
                updateWordBook(updatedWordBook)
                showDialog = false
            }
        )
    }

    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("书名: ${wordBook.title}")
            Text("单词量: ${wordBook.wordNum}")
            Text("标签: ${wordBook.tags}")
            Row {
                Button(onClick = { showDialog = true }) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { deleteWordBook(wordBook) }) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun EditWordBookDialog(
    wordBook: WordBookUI,
    onDismiss: () -> Unit,
    onSave: (WordBookUI) -> Unit
) {
    var picUrl by remember { mutableStateOf(wordBook.picUrl) }
    var title by remember { mutableStateOf(wordBook.title) }
    var tags by remember { mutableStateOf(wordBook.tags) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Word Book") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it },
                    label = { Text("Title") })
                OutlinedTextField(value = picUrl, onValueChange = { picUrl = it },
                    label = { Text("Picture Url") })
                OutlinedTextField(value = tags, onValueChange = { tags = it },
                    label = { Text("Tags") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    wordBook.copy(
                        title = title,
                        picUrl = picUrl,
                        tags = tags
                    )
                )
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
fun AddWordBookDialog(
    onDismiss: () -> Unit,
    onSave: (WordBookUI) -> Unit
) {
    var picUrl by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Word Book") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it },
                    label = { Text("Title") })
                OutlinedTextField(value = picUrl, onValueChange = { picUrl = it },
                    label = { Text("Picture Url") })
                OutlinedTextField(value = tags, onValueChange = { tags = it },
                    label = { Text("Tags") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(
                    WordBookUI(
                        id = 0,
                        picUrl = picUrl,
                        title = title,
                        wordNum = 0,
                        tags = tags
                    )
                )
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
