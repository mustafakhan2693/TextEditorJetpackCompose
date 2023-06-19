package com.technomapps.texteditorcompose.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.technomapps.texteditorcompose.R

@Composable
fun SetTextEditorToolbar(
    isBoldEnabled: MutableState<Boolean>,
    isItalicEnabled: MutableState<Boolean>,
    isUnderlineEnabled: MutableState<Boolean>,
    isStrikeThroughEnabled: MutableState<Boolean>,
    onToolbarActionClick: (
        onBoldClick: Boolean,
        onItalicClick: Boolean,
        onUnderlineClick: Boolean,
        onStrikeThroughClick: Boolean,
        alignLeft: Boolean,
        alignCenter: Boolean,
        alignRight: Boolean
    ) -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = Color.White)
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_action_bold),
            contentDescription = "Bold Action",
            colorFilter = ColorFilter.tint(color = if (isBoldEnabled.value) Color.Red else Color.Black),
            modifier = Modifier
                .padding(all = 8.dp)
                .weight(1f)
                .clickable {
                    isBoldEnabled.value = !isBoldEnabled.value
                    onToolbarActionClick(
                        true, false, false, false,
                        false, false, false
                    )
                }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_action_italic),
            contentDescription = "Italic Action",
            colorFilter = ColorFilter.tint(color = if (isItalicEnabled.value) Color.Red else Color.Black),
            modifier = Modifier
                .padding(all = 8.dp)
                .weight(1f)
                .clickable {
                    isItalicEnabled.value = !isItalicEnabled.value
                    onToolbarActionClick(
                        false, true, false, false,
                        false, false, false
                    )
                }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_action_underline),
            contentDescription = "Underline Action",
            colorFilter = ColorFilter.tint(color = if (isUnderlineEnabled.value) Color.Red else Color.Black),
            modifier = Modifier
                .padding(all = 8.dp)
                .weight(1f)
                .clickable {
                    isUnderlineEnabled.value = !isUnderlineEnabled.value
                    onToolbarActionClick(
                        false, false, true, false,
                        false, false, false
                    )
                }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_action_strike_through),
            contentDescription = "StrikeThrough Action",
            colorFilter = ColorFilter.tint(color = if (isStrikeThroughEnabled.value) Color.Red else Color.Black),
            modifier = Modifier
                .padding(all = 8.dp)
                .weight(1f)
                .clickable {
                    isStrikeThroughEnabled.value = !isStrikeThroughEnabled.value
                    onToolbarActionClick(
                        false, false, false, true,
                        false, false, false
                    )
                }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_action_align_left),
            contentDescription = "Align Left Action",
            colorFilter = ColorFilter.tint(color = Color.Black),
            modifier = Modifier
                .padding(all = 8.dp)
                .weight(1f)
                .clickable {
                    onToolbarActionClick(
                        false, false, false, false,
                        true, false, false
                    )
                }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_action_align_center),
            contentDescription = "Align Center Action",
            colorFilter = ColorFilter.tint(color = Color.Black),
            modifier = Modifier
                .padding(all = 8.dp)
                .weight(1f)
                .clickable {
                    onToolbarActionClick(
                        false, false, false, false,
                        false, true, false
                    )
                }
        )

        Image(
            painter = painterResource(id = R.drawable.ic_action_align_right),
            contentDescription = "Align Right Action",
            colorFilter = ColorFilter.tint(color = Color.Black),
            modifier = Modifier
                .padding(all = 8.dp)
                .weight(1f)
                .clickable {
                    onToolbarActionClick(
                        false, false, false, false,
                        false, false, true
                    )
                }
        )
    }
}

enum class Type {
    BOLD,
    ITALIC,
    UNDERLINE,
    STRIKETHROUGH,
    H1, H2, H3, H4, H5, H6,
    JUSTIFYCENTER,
    JUSTIFYFULL,
    JUSTIFYLEFT,
    JUSTIFYRIGHT
}

interface OnStateChangeListener {
    fun onStateChanged(text: String?, types: List<Type>?)
}

fun handleStateChange(
    types: List<Type>?,
    isBoldEnabled: MutableState<Boolean>,
    isItalicEnabled: MutableState<Boolean>,
    isStrikeThroughEnabled: MutableState<Boolean>,
    isUnderlineEnabled: MutableState<Boolean>
) {
    if (!types.isNullOrEmpty()) {
        isBoldEnabled.value = types.contains(Type.BOLD)
        isItalicEnabled.value = types.contains(Type.ITALIC)
        isStrikeThroughEnabled.value = types.contains(Type.STRIKETHROUGH)
        isUnderlineEnabled.value = types.contains(Type.UNDERLINE)
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun PreviewTextEditor() {
    SetTextEditorToolbar(
        isBoldEnabled = mutableStateOf(false),
        isItalicEnabled = mutableStateOf(true),
        isUnderlineEnabled = mutableStateOf(false),
        isStrikeThroughEnabled = mutableStateOf(true)
    ) { _, _, _, _, _, _, _ ->

    }
}