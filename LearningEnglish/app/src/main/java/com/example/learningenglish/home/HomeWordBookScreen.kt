package com.example.learningenglish.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningenglish.test.testBookData
import com.example.learningenglish.ui.base.CommonLoadingLayout
import com.example.learningenglish.ui.view.LoadingImage
import com.example.learningenglish.ui.view.ViewSpacer
import com.example.learningenglish.ui.view.ViewSpacerWeight
import com.example.learningenglish.word.allbook.AllBookSelectorActivity
import com.example.learningenglish.word.list.BookWordListActivity
import com.example.learningenglish.word.model.wordbook.LocalAllWordBookViewModel
import com.example.learningenglish.word.model.wordbook.LocalAllWordBookViewModelFactory
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.mybook.MyBookActivity

@Preview
@Composable
private fun PreviewHomeWordBookScreen() {
    BookListScreen(books = testBookData, onBookClick = {}, onAdd = {}, onItemClick = {})
}


@Composable
fun HomeWordBookScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: LocalAllWordBookViewModel =
        viewModel(factory = LocalAllWordBookViewModelFactory(context))
    val bookList = viewModel.wordBookList.collectAsState().value
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {

        }
    CommonLoadingLayout(data = bookList) { books ->
        BookListScreen(modifier, books, onBookClick = {
            launcher.launch(MyBookActivity.actionView(context))
        }, {
            launcher.launch(AllBookSelectorActivity.actionView(context))
        }) {
            launcher.launch(BookWordListActivity.actionView(context, it.id))
        }
    }
}

@Composable
private fun BookListScreen(
    modifier: Modifier = Modifier,
    books: List<WordBookUI>,
    onBookClick: () -> Unit,
    onAdd: () -> Unit,
    onItemClick: (WordBookUI) -> Unit,
) {
    Column(modifier = modifier) {

        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            ViewSpacer(size = 16)
            Text(text = "单词本", modifier = Modifier.clickable { onBookClick() })
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = ""
            )
            ViewSpacerWeight()
            IconButton(onClick = {
                onAdd()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) {
            items(books) {
                WordBookItem(Modifier.clickable {
                    onItemClick(it)
                }, book = it)
            }
        }

    }
}

@Composable
private fun WordBookItem(modifier: Modifier = Modifier, book: WordBookUI) {
    Box(modifier = modifier.clip(RoundedCornerShape(16.dp))) {
        LoadingImage(
            modifier = Modifier
                .width(150.dp)
                .aspectRatio(3 / 4f), model = book.picUrl
        )
        Text(
            text = "${book.wordNum}词",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            fontSize = 12.sp,
        )
    }
}

