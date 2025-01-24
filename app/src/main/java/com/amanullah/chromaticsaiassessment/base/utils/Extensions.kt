package com.amanullah.chromaticsaiassessment.base.utils

import android.annotation.SuppressLint
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

fun String.getInitials(): String {
    val words = this.split(" ")
    return when (words.size) {
        0 -> ""
        1 -> words[0].take(1).uppercase()
        else -> (words[0].take(1) + words[1].take(1)).uppercase()
    }
}

fun String.isValidBangladeshPhoneNumber(): Boolean {
    val bangladeshPhoneNumberRegex = "^(?:\\+8801|8801|01)[3-9]\\d{8}\$|^(?:\\+8802|8802|02)\\d{8}\$".toRegex()
    return bangladeshPhoneNumberRegex.matches(input = this)
}

@SuppressLint("ReturnFromAwaitPointerEventScope")
fun Modifier.gesturesDisabledWithAlpha(
    disabled: Boolean = true,
    disabledAlpha: Float = 0.5F
): Modifier {
    return if (disabled) {
        this
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    // Consume all pointer events to disable gestures
                    while (true) {
                        awaitPointerEvent(pass = PointerEventPass.Initial)
                            .changes
                            .forEach(PointerInputChange::consume)
                    }
                }
            }
            .alpha(disabledAlpha) // Set alpha when disabled
    } else {
        this.alpha(1F) // Normal alpha when not disabled
    }
}

@Composable
fun keyboardState(): State<Boolean> {
    val view = LocalView.current
    var isImeVisible by remember { mutableStateOf(false) }

    DisposableEffect(LocalWindowInfo.current) {
        val listener = ViewTreeObserver.OnPreDrawListener {
            isImeVisible = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) == true
            true
        }
        view.viewTreeObserver.addOnPreDrawListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnPreDrawListener(listener)
        }
    }
    return rememberUpdatedState(isImeVisible)
}