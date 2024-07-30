package com.example.learningenglish.word.list

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.learningenglish.http.word.WordRepository
import com.example.learningenglish.test.testWordData
import com.example.learningenglish.ui.base.paging.GenericPagingListScreen
import com.example.learningenglish.ui.base.paging.GenericPagingRepository
import com.example.learningenglish.ui.theme.AppTheme
import com.example.learningenglish.word.model.WordUI
import com.example.learningenglish.word.model.toUI
import com.example.learningenglish.word.study.StudyBookActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber

@Preview
@Composable
private fun PreviewBookList() {
    AppTheme {
        val collectAsLazyPagingItems =
            flow { emit(PagingData.from(testWordData)) }.collectAsLazyPagingItems()
        BookWordListScreen(modifier = Modifier.fillMaxSize(), collectAsLazyPagingItems){

        }

    }
}

@Composable
fun AllBookWordsScreen(modifier: Modifier = Modifier, bookId: Long) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
    }

    val paging = remember {
        val wordRepository = WordRepository()
        GenericPagingRepository { page, size ->
            withContext(Dispatchers.IO) {
                Timber.d("page:$page , size:$size")
                val result = wordRepository.list(bookId, page, size)
                if (result.isSuccess) {
                    val data = result.getOrThrow()
                    if (data.isSuccess) {
                        data.data.data.map { it.toUI() }
                    } else {
                        emptyList()
                    }
                } else {
                    emptyList()
                }
            }
        }.getPagingData()
    }
    val pagingItems = paging.collectAsLazyPagingItems()
    BookWordListScreen(modifier, pagingItems){
        launcher.launch(StudyBookActivity.actionView(context, it.wordId))
    }
}

@Composable
private fun BookWordListScreen(
    modifier: Modifier,
    words: LazyPagingItems<WordUI>,
    onClick: (WordUI) -> Unit
) {
    GenericPagingListScreen(
        modifier = modifier.fillMaxSize(), items = words,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(words.itemCount) {
            val word = words[it]
            Card(shape = RoundedCornerShape(6.dp), modifier = Modifier
                .clickable {
                    word?.let { onClick(it) }
                }) {
                word?.let { WordItem(ui = it) }
            }
        }
    }
}

@Composable
private fun WordItem(modifier: Modifier = Modifier, ui: WordUI) {

    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = ui.word,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground
    )

}
