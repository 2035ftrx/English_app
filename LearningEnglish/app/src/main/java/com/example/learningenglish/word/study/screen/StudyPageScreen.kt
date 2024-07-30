package com.example.learningenglish.word.study.screen

import androidx.collection.mutableIntLongMapOf
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learningenglish.ui.base.CommonLoadingLayout
import com.example.learningenglish.ui.base.LoadingData
import com.example.learningenglish.word.model.WordUI
import com.example.learningenglish.word.model.record.IWordStudyRecordImpl
import com.example.learningenglish.word.model.study.IStudyLogic
import com.example.learningenglish.word.model.study.StudyInitData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudyPageScreen(modifier: Modifier = Modifier, iStudyLogic: IStudyLogic, onFinish: () -> Unit) {

    val coroutineScope = rememberCoroutineScope()
    val initDataLoading = remember {
        mutableStateOf<LoadingData<StudyInitData>>(LoadingData.Loading())
    }

    val iWordStudyRecord = remember {
        IWordStudyRecordImpl()
    }

    LaunchedEffect(Unit) {
        iStudyLogic.getInitialData().collect { initDataLoading.value = it }
    }

    CommonLoadingLayout(data = initDataLoading.value) { initData ->

        if (initData.pageCount != 0) {

            val pagerState = rememberPagerState(
                initialPage = maxOf(initData.initialPage, 0)
            ) { initData.pageCount }

            val pageStartTimetamp = remember {
                mutableIntLongMapOf()
            }

            LaunchedEffect(pagerState.currentPage) {
                pageStartTimetamp.put(pagerState.currentPage, System.currentTimeMillis())
            }

            HorizontalPager(
                state = pagerState,
                pageSpacing = 8.dp,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
            ) { page ->

                val wordUI = remember {
                    mutableStateOf<LoadingData<WordUI>>(LoadingData.Loading())
                }
                LaunchedEffect(page) {
                    iStudyLogic.getWordByPage(page).collect { wordUI.value = it }
                }

                CommonLoadingLayout(data = wordUI.value) {
                    StudyWordScreen(
                        modifier = modifier,
                        wordBookInfoLayout = {
                            WordBookInfo(Modifier, page + 1, initData.pageCount)
                        },
                        wordBookUI = initData.book,
                        wordUI = it,
                        onResult = { iWordStatus ->
                            coroutineScope.launch(Dispatchers.IO) {
                                iStudyLogic.onWordStatusChange(it, iWordStatus)
                                iWordStudyRecord.record(
                                    it, pageStartTimetamp[page], initData.studyType
                                )
                            }
                            if (pagerState.canScrollForward) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                onFinish()
                            }
                        },
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "很棒哦，已经完成学习啦~🎉🎉🎉")
            }
        }

    }
}

