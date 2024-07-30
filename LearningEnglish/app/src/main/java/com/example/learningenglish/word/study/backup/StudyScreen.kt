package com.example.learningenglish.word.study.backup

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learningenglish.http.word.WordRepository
import com.example.learningenglish.test.testBookData
import com.example.learningenglish.ui.base.CommonLoadingLayout
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.ui.theme.AppTheme
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.WordUI
import com.example.learningenglish.word.model.word.IWordRepository
import com.example.learningenglish.word.model.word.IWordRepositoryImpl
import com.example.learningenglish.word.study.screen.StudyWordScreen
import com.example.learningenglish.word.study.screen.WordBookInfo


@Preview
@Composable
private fun PreviewStudyPageScreen() {
    AppTheme {
        Scaffold {
            StudyPageScreen(
                modifier = Modifier.padding(it),
                22389,
                testBookData.random()
            )
        }
    }
}

@Composable
fun StudyPageActivity(modifier: Modifier = Modifier, wordId: Long) {

    val studyViewModel: StudyViewModel =
        viewModel(factory = StudyViewModelFactory(LocalContext.current.applicationContext))
    LaunchedEffect(wordId) {
        studyViewModel.setupByWordId(wordId)
    }
    val currentPage = studyViewModel.currentPage.collectAsState().value
    val wordBookUI = studyViewModel.wordBookUI.collectAsState().value
    Column(modifier = Modifier.navigationBarsPadding()) {
        // TitleBar(title = "学习单词")
        CommonLoadingLayout(data = wordBookUI) {
            StudyPageScreen(Modifier.fillMaxSize(), currentPage, it)
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudyPageScreen(modifier: Modifier = Modifier, initialPage: Int, book: WordBookUI) {

    val pagerState = rememberPagerState(initialPage = maxOf(initialPage - 1, 0)) { book.wordNum }
    HorizontalPager(
        state = pagerState,
        pageSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        WordLoadScreen(modifier = modifier, wordRank = it + 1, book = book)
    }

}


@Composable
private fun WordLoadScreen(modifier: Modifier = Modifier, wordRank: Int, book: WordBookUI) {
    val wordRepositoryImpl: IWordRepository = remember {
        IWordRepositoryImpl(WordRepository())
    }
    val wordUI = remember {
        mutableStateOf<LoadingData<WordUI>>(LoadingData.Loading())
    }
    LaunchedEffect(wordRank, book) {
        wordRepositoryImpl.getWordByRank(wordRank, book.id)
            .collect {
                wordUI.value = it
            }
    }

    CommonLoadingLayout(data = wordUI.value) {
        StudyWordScreen(
            modifier = modifier,
            wordBookInfoLayout = {
                WordBookInfo(Modifier, it.wordRank, book.wordNum)
            },
            wordBookUI = book,
            wordUI = it
        )
    }

}
