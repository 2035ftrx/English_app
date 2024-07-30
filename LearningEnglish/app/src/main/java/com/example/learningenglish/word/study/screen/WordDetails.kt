package com.example.learningenglish.word.study.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learningenglish.ui.view.ViewSpacer
import com.example.learningenglish.word.model.WordBookUI
import com.example.learningenglish.word.model.WordDetailsUI

@Composable
fun WordDetails(
    modifier: Modifier = Modifier,
    wordBookUI: WordBookUI,
    detailsUI: WordDetailsUI
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {

        item {
            ViewSpacer(size = 10)
            Text(
                text = "来源：" + wordBookUI.title,
                fontSize = 12.sp, color = Color.Gray
            )
        }

        items(detailsUI.trans ?: emptyList()) { trans ->

            Row {
                Text(text = "${trans.pos}.", fontSize = 12.sp, color = Color.Gray)
                ViewSpacer(size = 10)
                Text(
                    text = "${trans.tranCn}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        item {
            ViewSpacer(size = 10)
            Text(
                text = "例句：",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            ViewSpacer(size = 10)
        }

        items(detailsUI.sentences) {

            it.content?.let { it1 ->
                Text(
                    text = it1,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
            it.trans?.let { it1 ->
                Text(
                    text = it1,
                    fontSize = 12.sp, color = Color.Gray
                )
            }
            ViewSpacer(size = 16)

        }


    }
}

