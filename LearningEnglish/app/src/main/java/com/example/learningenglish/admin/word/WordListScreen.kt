package com.example.learningenglish.admin.word

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.learningenglish.word.model.WordEdit
import kotlinx.coroutines.launch

@Composable
fun WordListScreen(wordBookId: Long) {

    val viewModel: AdminWordsViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()
    val pagingItems = viewModel.wordPagingFlow(wordBookId).collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(pagingItems.itemCount) {
                val word = pagingItems.peek(it)
                word?.let {
                    WordItem(
                        modifier = Modifier,
                        word,
                        updateWord = { id: Long, word: String, info: String ->
                            coroutineScope.launch {
                                viewModel.updateWord(id, word, info)
                                    .collect {
                                        pagingItems.refresh()
                                    }
                            }
                        },
                        deleteWord = {
                            coroutineScope.launch {
                                viewModel.deleteWord(it.id)
                                    .collect {
                                        pagingItems.refresh()
                                    }
                            }
                        })
                }
            }
        }

        AddWordButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            addWord = { word: String, info: String ->
                coroutineScope.launch {
                    viewModel.createWord(wordBookId,word, info)
                        .collect {
                            pagingItems.refresh()
                        }
                }
            })
    }
}

@Composable
private fun AddWordButton(
    modifier: Modifier = Modifier,
    addWord: (word: String, info: String) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AddWordDialog(
            onDismiss = { showDialog = false },
            onSave = { word: String, info: String ->
                addWord(word, info)
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
fun WordItem(
    modifier: Modifier = Modifier,
    word: WordEdit,
    updateWord: (id: Long, word: String, info: String) -> Unit,
    deleteWord: (WordEdit) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        EditWordDialog(
            word = word,
            onDismiss = { showDialog = false },
            onSave = { id: Long, word: String, info: String ->
                updateWord(id, word, info)
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
            Text("Word: ${word.word}")
            Row {
                Button(onClick = { showDialog = true }) {
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { deleteWord(word) }) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun EditWordDialog(
    word: WordEdit,
    onDismiss: () -> Unit,
    onSave: (id: Long, word: String, info: String) -> Unit
) {
    var wordText by remember { mutableStateOf(word.word) }
    var info by remember { mutableStateOf(word.info) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Word") },
        text = {
            Column {
                TextField(
                    value = wordText,
                    onValueChange = { wordText = it },
                    label = { Text("Word") })
                TextField(value = info, onValueChange = { info = it }, label = { Text("Info") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(word.id, wordText, info)
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
fun AddWordDialog(
    onDismiss: () -> Unit,
    onSave: (word: String, info: String) -> Unit
) {
    var wordText by remember { mutableStateOf("") }
    var info by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Word") },
        text = {
            Column {
                TextField(
                    value = wordText,
                    onValueChange = { wordText = it },
                    label = { Text("Word") })
                TextField(value = info, onValueChange = { info = it }, label = { Text("Info") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(wordText, info)
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
