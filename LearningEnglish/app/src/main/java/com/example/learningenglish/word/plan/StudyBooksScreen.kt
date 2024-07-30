package com.example.learningenglish.word.plan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningenglish.test.testBookData
import com.example.learningenglish.test.testPlan1
import com.example.learningenglish.ui.base.CommonLoadingLayout
import com.example.learningenglish.ui.view.LoadingImage
import com.example.learningenglish.ui.view.ViewSpacer
import com.example.learningenglish.ui.view.ViewSpacerWeight
import com.example.learningenglish.ui.view.rememberBooleanState
import com.example.learningenglish.word.model.wordbook.LocalAllWordBookViewModel
import com.example.learningenglish.word.model.wordbook.LocalAllWordBookViewModelFactory
import com.example.learningenglish.word.model.StudyPlan
import com.example.learningenglish.word.model.WordBookUI

@Preview
@Composable
private fun PreviewStudyBooksScreen() {
    Column {
        TitleText("当前学习计划")

        StudyPlanScreen(
            book = testBookData.first(),
            studyPlan = testPlan1,
            onCreatePlan = { book, dailyWords ->
            })

        TitleText("已有单词本")

        StudyBooksScreen(
            modifier = Modifier.fillMaxHeight(),
            books = testBookData,
            onBookSelected = {
            })
    }
}

@Composable
fun StudyPlanScreen(modifier: Modifier = Modifier) {
    val viewModel: StudyPlanViewModel =
        viewModel(factory = StudyPlanViewModelFactory(LocalContext.current))

    val selectedBook by viewModel.selectedBook.observeAsState()
    val studyPlan by viewModel.studyPlan.observeAsState()

    val allWordBookViewModel: LocalAllWordBookViewModel =
        viewModel(factory = LocalAllWordBookViewModelFactory(LocalContext.current))
    val bookList = allWordBookViewModel.wordBookList.collectAsState().value

    Column(modifier = modifier.padding(horizontal = 16.dp)) {

        TitleText("当前学习计划")

        StudyPlanScreen(
            modifier = Modifier.fillMaxWidth(),
            book = selectedBook,
            studyPlan = studyPlan,
            onCreatePlan = { book, dailyWords ->
                viewModel.createPlan(book, dailyWords)
            })

        TitleText("已有单词本")

        CommonLoadingLayout(data = bookList) {
            StudyBooksScreen(modifier = Modifier.fillMaxHeight(), books = it, onBookSelected = {
                viewModel.selectBook(it)
            })
        }
    }

}

@Composable
private fun TitleText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(vertical = 16.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
}

@Composable
private fun StudyPlanScreen(
    modifier: Modifier = Modifier,
    book: WordBookUI?,
    studyPlan: StudyPlan?,
    onCreatePlan: (WordBookUI, Int) -> Unit
) {
    if (book != null) {
        Card(modifier = modifier) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                LoadingImage(
                    model = book.picUrl,
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(3 / 4f)
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    Text(text = book.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = book.tags)
                    Text(text = "${book.wordNum}词")

                    studyPlan?.let {
                        Text(text = "每天单词量: ${studyPlan.dailyWords}")
                        Text(text = "计划完成天数: ${studyPlan.totalDays}")
                    }

                }
                ModifyPlan({ onCreatePlan(book, it) }, book.wordNum)
            }
        }
    } else {
        Text(text = "请选择一个单词本")
    }
}

@Composable
private fun ModifyPlan(onCreatePlan: (Int) -> Unit, wordCount: Int) {
    val expanded = rememberBooleanState()

    val dailyWordsOptions = listOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
    var selectedDailyWords by remember { mutableStateOf(dailyWordsOptions.first()) }
    TextButton(
        onClick = { expanded.beTrue()  },
        modifier = Modifier
    ) {
        Text(text = "修改")
    }

    DropdownMenu(
        expanded = expanded.get(),
        onDismissRequest = { expanded.beFalse() }
    ) {
        dailyWordsOptions.forEach { dailyWords ->
            DropdownMenuItem(onClick = {
                selectedDailyWords = dailyWords
                expanded.beFalse()
                onCreatePlan(dailyWords)
            }, text = {
                Text(text = "每天${dailyWords}个单词，预计${(wordCount + dailyWords - 1) / dailyWords}天完成")
            })
        }

    }
}


@Composable
private fun StudyBooksScreen(
    modifier: Modifier = Modifier,
    books: List<WordBookUI>,
    onBookSelected: (WordBookUI) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(books) { book ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onBookSelected(book) }
            ) {
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
                                onClick = { onBookSelected(book) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.2f
                                    )
                                ),
                            ) {
                                Text(text = "学这本")
                            }

                            ViewSpacer(size = 16)

                        }

                    }

                }
            }
        }
    }
}