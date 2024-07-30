package com.example.learningenglish.word.allbook

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningenglish.test.testBookData
import com.example.learningenglish.ui.base.CommonLoadingLayout
import com.example.learningenglish.ui.theme.AppTheme
import com.example.learningenglish.ui.view.LoadingImage
import com.example.learningenglish.ui.view.ViewSpacer
import com.example.learningenglish.ui.view.ViewSpacerWeight
import com.example.learningenglish.word.model.SelectedBookUI
import kotlin.random.Random

@Preview
@Composable
private fun PreviewBookList() {
    AppTheme {

        BookListScreen(modifier = Modifier.fillMaxSize(), books = testBookData.map {
            SelectedBookUI(it, Random.nextBoolean())
        }) {
        }

    }
}

@Composable
fun AllBookScreen(modifier: Modifier = Modifier) {

    val viewModel: AllBookViewModel =
        viewModel(factory = AllBookViewModelFactory(LocalContext.current))

    val wordBookList = viewModel.wordBookList.collectAsState().value

    CommonLoadingLayout(modifier = modifier, data = wordBookList) { books ->
        BookListScreen(modifier, books) {
            viewModel.addBook(it)
        }
    }

}

@Composable
private fun BookListScreen(
    modifier: Modifier,
    books: List<SelectedBookUI>,
    onAdd: (SelectedBookUI) -> Unit
) {


    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(books) {
            Card(shape = RoundedCornerShape(6.dp)) {
                WordBookItem(ui = it) {
                    onAdd(it)
                }
            }
        }
    }

}

@Composable
private fun WordBookItem(modifier: Modifier = Modifier, ui: SelectedBookUI, onAdd: () -> Unit) {
    val book = ui.book
    Row(modifier = modifier) {

        LoadingImage(
            modifier = Modifier
                .width(100.dp)
                .aspectRatio(3 / 4f), model = book.picUrl
        )

        ViewSpacer(size = 16)

        Column(modifier = Modifier.fillMaxHeight()) {

            ViewSpacer(size = 10)

            Text(
                text = book.title,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = book.tags,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            ViewSpacerWeight()

            Row(verticalAlignment = Alignment.Bottom) {

                Text(
                    text = "${book.wordNum}词", modifier = Modifier, fontSize = 12.sp
                )

                ViewSpacerWeight()

                Button(
                    onClick = { onAdd() }, contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ),
                    enabled = !ui.isSelected
                ) {
                    if (ui.isSelected) {
                        Text(text = "已添加")
                    } else {
                        Text(text = "添加")
                    }
                }

                ViewSpacer(size = 16)

            }

        }

    }

}
