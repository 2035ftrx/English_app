package com.example.learningenglish.word.study.screen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningenglish.test.testBookData
import com.example.learningenglish.test.testWordData
import com.example.learningenglish.ui.theme.AppTheme
import com.example.learningenglish.ui.view.SpacerDivider
import com.example.learningenglish.ui.view.ViewSpacer
import com.example.learningenglish.ui.view.rememberBooleanState
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.WordUI
import com.example.learningenglish.word.model.IWordStatus
import kotlin.random.Random

@Preview
@Composable
private fun PreviewStudyScreen() {
    AppTheme {
        Scaffold {
            StudyWordScreen(
                modifier = Modifier.padding(it),
                wordBookInfoLayout = {
                    WordBookInfo(Modifier, Random.nextInt(), Random.nextInt())
                },
                wordBookUI = testBookData.random(),
                wordUI = testWordData.random(),
            )
        }
    }
}

@Composable
fun StudyWordScreen(
    modifier: Modifier = Modifier,
    wordBookInfoLayout: @Composable () -> Unit,
    wordBookUI: WordBookUI,
    wordUI: WordUI,
    onResult: (IWordStatus) -> Unit = {},
) {

    Card(
        modifier = modifier.fillMaxSize(),
        colors = CardDefaults.cardColors()
    ) {

        ViewSpacer(size = 16)
        wordBookInfoLayout()
        ViewSpacer(size = 16)

        WordTitle(modifier = Modifier.align(Alignment.CenterHorizontally), wordUI)

        ViewSpacer(size = 16)
        SpacerDivider()
        ViewSpacer(size = 3)

        StudyWordDetails(modifier = Modifier.weight(1f), wordBookUI, wordUI)

        WordTagButton() {
            onResult(it)
        }

        ViewSpacer(size = 16)

    }

}

@Composable
private fun StudyWordDetails(
    modifier: Modifier = Modifier,
    wordBookUI: WordBookUI,
    wordUI: WordUI
) {

    val showDetails = rememberBooleanState()

    AnimatedVisibility(modifier = modifier, visible = showDetails.isTrue()) {
        WordDetails(
            modifier = Modifier.fillMaxSize(),
            wordBookUI, wordUI.details
        )
    }

    AnimatedVisibility(modifier = modifier, visible = showDetails.isFalse()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { showDetails.beTrue() },
            contentAlignment = Alignment.Center
        ) { Text(text = "点击显示释义", color = Color.Gray, fontSize = 14.sp) }
    }

}

@Composable
fun WordBookInfo(modifier: Modifier = Modifier, index: Int, count: Int) {
    Text(
        text = "${index}/${count}",
        modifier = modifier.padding(horizontal = 16.dp),
        fontSize = 13.sp,
        color = Color.Gray,
        fontWeight = FontWeight.SemiBold
    )
}

