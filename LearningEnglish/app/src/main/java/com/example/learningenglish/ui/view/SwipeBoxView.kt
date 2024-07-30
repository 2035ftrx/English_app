package com.example.learningenglish.ui.view

import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeBox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    bottomContent: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    var bottomWidth by remember { mutableFloatStateOf(0f) }
    var bottomHeight by remember { mutableStateOf(0) }
    val minBound = 0f
    val maxBound = -bottomWidth
    var forceAnimationCheck by remember { mutableStateOf(false) }
    val anchoredDraggableState = remember(maxBound) {
        AnchoredDraggableState(
            initialValue = checked,
            animationSpec = TweenSpec(durationMillis = 1000),
            anchors = DraggableAnchors {
                false at minBound
                true at maxBound
            },
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { maxBound }
        )
    }
    val currentOnCheckedChange by rememberUpdatedState(onCheckedChange)
    val currentChecked by rememberUpdatedState(checked)
    LaunchedEffect(anchoredDraggableState) {
        snapshotFlow { anchoredDraggableState.currentValue }
            .collectLatest { newValue ->
                if (currentChecked != newValue) {
                    currentOnCheckedChange?.invoke(newValue)
                    forceAnimationCheck = !forceAnimationCheck
                }
            }
    }
    LaunchedEffect(checked, forceAnimationCheck) {
        if (checked != anchoredDraggableState.currentValue) {
            anchoredDraggableState.animateTo(checked)
        }
    }

    Box(
        modifier = modifier
            .anchoredDraggable(
                state = anchoredDraggableState,
                orientation = Orientation.Horizontal,
                enabled = onCheckedChange != null
            )
            .clipToBounds()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(with(LocalDensity.current) { bottomHeight.toDp() })
                .onSizeChanged { bottomWidth = it.width.toFloat() }
        ) {
            bottomContent()
        }
        Box(
            modifier = Modifier
                .graphicsLayer { translationX = anchoredDraggableState.offset }
                .onSizeChanged { bottomHeight = it.height }
        ) {
            content()
        }
    }
}


//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun SwipeBox(
//    modifier: Modifier = Modifier,
//    actionWidth: Dp, //锚点宽度
//    startAction: List<@Composable BoxScope.() -> Unit>, //左侧展开锚点
//    startFillAction: (@Composable BoxScope.() -> Unit)? = null, //左侧全展开锚点
//    endAction: List<@Composable BoxScope.() -> Unit>, //右侧展开锚点
//    endFillAction: (@Composable BoxScope.() -> Unit)? = null, //右侧全展开锚点
//    content: @Composable BoxScope.() -> Unit
//) {
//    val density = LocalDensity.current
//    val actionWidthPx = with(density) { actionWidth.toPx() }
//    val startWidth = actionWidthPx * startAction.size //左侧锚点宽度
//    val startActionSize = startAction.size + 1 //左侧锚点总数 = startAction + startFillAction
//    val endWidth = actionWidthPx * endAction.size  //右侧锚点宽度
//    val endActionSize = endAction.size + 1 //右侧锚点总数 =  endAction + endFillAction
//    var contentWidth by remember { mutableFloatStateOf(0f) } //内容组件宽度
//    var contentHeight by remember { mutableFloatStateOf(0f) }
//    val state = remember(startWidth, endWidth, contentWidth) {
//        AnchoredDraggableState(
//            initialValue = DragAnchors.Center,
//            animationSpec = TweenSpec(durationMillis = 350),
//            anchors = DraggableAnchors {
//                DragAnchors.Start at (if (startFillAction != null) actionWidthPx else 0f) + startWidth //左侧全展开锚点宽度 + 左侧展开锚点宽度
//                DragAnchors.StartFill at (if (startFillAction != null) contentWidth else 0f) + startWidth // 内容组件宽度 + 左侧展开锚点宽度
//                DragAnchors.Center at 0f
//                DragAnchors.End at (if (endFillAction != null) -actionWidthPx else 0f) - endWidth //右侧全展开锚点宽度 + 右侧展开锚点宽度
//                DragAnchors.EndFill at (if (endFillAction != null) -contentWidth else 0f) - endWidth // 内容组件宽度 + 右侧展开锚点宽度
//            },
//            positionalThreshold = { distance -> distance * 0.5f },
//            velocityThreshold = { with(density) { 100.dp.toPx() } },
//        )
//    }
//
//    Box(
//        modifier = modifier
//            .anchoredDraggable(
//                state = state,
//                orientation = Orientation.Horizontal,
//            )
//            .clipToBounds()
//    ) {
//        startAction.forEachIndexed { index, action ->
//            Box(
//                modifier = Modifier
//                    .align(Alignment.CenterStart)
//                    .width(actionWidth)
//                    .height(with(density) {
//                        contentHeight.toDp()
//                    })
//                    .offset {
//                        IntOffset(
//                            x = if (state.offset <= actionWidthPx * startActionSize) {
//                                (-actionWidthPx + state.offset / startActionSize * (startActionSize - index)).roundToInt()
//                            } else {
//                                (-actionWidthPx * (index + 1) + state.offset).roundToInt()
//                            },
//                            y = 0,
//                        )
//                    }
//            ) {
//                action()
//            }
//        }
//        startFillAction?.let {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.CenterStart)
//                    .height(with(density) {
//                        contentHeight.toDp()
//                    })
//                    .offset {
//                        IntOffset(
//                            x = if (state.offset <= actionWidthPx * startActionSize) {
//                                (-contentWidth + state.offset / startActionSize).roundToInt()
//                            } else {
//                                (-contentWidth - startWidth + state.offset).roundToInt()
//                            },
//                            y = 0,
//                        )
//                    }
//            ) {
//                it()
//            }
//        }
//        endAction.forEachIndexed { index, action ->
//            Box(
//                modifier = Modifier
//                    .align(Alignment.CenterEnd)
//                    .width(actionWidth)
//                    .height(with(density) {
//                        contentHeight.toDp()
//                    })
//                    .offset {
//                        IntOffset(
//                            x = if (state.offset >= -(actionWidthPx * endActionSize)) {
//                                (actionWidthPx + state.offset / endActionSize * (endActionSize - index)).roundToInt()
//                            } else {
//                                (actionWidthPx * (index + 1) + state.offset).roundToInt()
//                            },
//                            y = 0,
//                        )
//                    }
//            ) {
//                action()
//            }
//        }
//        endFillAction?.let {
//            Box(
//                modifier = Modifier
//                    .align(Alignment.CenterEnd)
//                    .height(with(density) {
//                        contentHeight.toDp()
//                    })
//                    .offset {
//                        IntOffset(
//                            x = if (state.offset >= -(actionWidthPx * endActionSize)) {
//                                (contentWidth + state.offset / endActionSize).roundToInt()
//                            } else {
//                                (contentWidth + endWidth + state.offset).roundToInt()
//                            },
//                            y = 0,
//                        )
//                    }
//            ) {
//                it()
//            }
//        }
//        Box(
//            modifier = Modifier
//                .onSizeChanged {
//                    contentWidth = it.width.toFloat()
//                    contentHeight = it.height.toFloat()
//                }
//                .offset {
//                    IntOffset(
//                        x = state.offset.roundToInt(),
//                        y = 0,
//                    )
//                }
//        ) {
//            content()
//        }
//    }
//}
//
//enum class DragAnchors { Start, StartFill, Center, End, EndFill }
