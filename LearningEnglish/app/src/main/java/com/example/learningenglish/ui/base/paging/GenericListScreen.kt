package com.example.learningenglish.ui.base.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T : Any> GenericPagingListScreen(
    modifier: Modifier, items: LazyPagingItems<T>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    itemLayout: LazyListScope. () -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        itemLayout()
        item {
            LoadStateLayout(items)
        }
    }
}

@Composable
fun <T : Any> GenericPagingRowListScreen(
    modifier: Modifier,
    items: LazyPagingItems<T>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    itemLayout: LazyListScope. () -> Unit,
) {

    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment
    ) {

        itemLayout()

        item {
            LoadStateLayout(items)
        }
    }
}

@Composable
fun <T : Any> GenericPagingGridScreen(
    modifier: Modifier,
    columns: GridCells,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    items: LazyPagingItems<T>,
    itemLayout: LazyGridScope.() -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = columns,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement
    ) {
        itemLayout()

        item {
            LoadStateLayout(items)
        }
    }
}

@Composable
private fun <T : Any> LoadStateLayout(items: LazyPagingItems<T>) {
    items.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                LoadingItem()
            }

            loadState.refresh is LoadState.Error -> {
                val e = items.loadState.refresh as LoadState.Error
                ErrorItem(
                    message = e.error.localizedMessage!!, modifier = Modifier.fillMaxSize()
                )
            }

            loadState.append is LoadState.Loading -> {
                LoadingItem()
            }

            loadState.append is LoadState.Error -> {
                val e = items.loadState.append as LoadState.Error
                ErrorItem(message = e.error.localizedMessage!!, Modifier)
            }
        }
    }
}

@Composable
fun LoadingItem() {
    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
}

@Composable
fun ErrorItem(message: String, modifier: Modifier) {
    Text(text = message, modifier = modifier.padding(16.dp))
}
