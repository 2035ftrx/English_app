package com.example.learningenglish.ui.view

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable


@Stable
class BooleanState(initialValue: Boolean) : TState<Boolean>(initialValue) {

    fun isTrue(): Boolean {
        return get()
    }

    fun isFalse(): Boolean {
        return !get()
    }

    fun beTrue() {
        update(true)
    }

    fun beFalse() {
        update(false)
    }

    fun toggle() {
        update(!get())
    }

}

@Stable
class UriState(initialValue: Uri) : TState<Uri>(initialValue) {

}

@Stable
class StringState(initialValue: String) : TState<String>(initialValue) {

}

@Stable
class FloatState(initialValue: Float) : TState<Float>(initialValue) {

}

@Stable
class IntState(initialValue: Int) : TState<Int>(initialValue) {

}

@Stable
open class TState<T>(initialValue: T) {

    internal val value = mutableStateOf(initialValue)

    val currentValue: T
        get() {
            return value.value
        }

    fun update(t: T) {
        value.value = t
    }

    fun get(): T {
        return value.value
    }

    companion object {
        // 基本类型(可以存储在 Bundle 内的对象)不需要自定义 Saver ，一些自定义的类型则需要自定义
        fun <T : Any> Saver() =
            androidx.compose.runtime.saveable.Saver<TState<T>, T>(save = { it.currentValue },
                restore = { TState<T>(it) })
    }
}

class TSaver<R : TState<T>, T : Any>(private val restoreFunc: (value: T) -> R) :
    androidx.compose.runtime.saveable.Saver<R, T> {

    override fun restore(value: T): R {
        return restoreFunc(value)
    }

    override fun SaverScope.save(value: R): T {
        return value.currentValue
    }

}

@Composable
fun rememberBooleanState(initialValue: Boolean = false): BooleanState {
    return rememberSaveable(saver = TSaver { BooleanState(it) }) { BooleanState(initialValue) }
}

@Composable
fun rememberStringState(initialValue: String = ""): StringState {
    return rememberSaveable(saver = TSaver { StringState(it) }) { StringState(initialValue) }
}

@Composable
fun rememberFloatState(initialValue: Float): FloatState {
    return rememberSaveable(saver = TSaver { FloatState(it) }) { FloatState(initialValue) }
}

@Composable
fun rememberIntState(initialValue: Int): IntState {
    return rememberSaveable(saver = TSaver { IntState(it) }) { IntState(initialValue) }
}

@Composable
fun rememberUriState(initialValue: Uri = Uri.EMPTY): UriState {
    return rememberSaveable(saver = TSaver { UriState(it) }, init = { UriState(initialValue) })
}

@Composable
fun <T : Any> rememberTState(initialValue: T): TState<T> {
    return rememberSaveable(saver = TState.Saver()) { TState(initialValue) }
}
