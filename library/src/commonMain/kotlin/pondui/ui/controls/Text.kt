package pondui.ui.controls

import androidx.compose.runtime.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import pondui.ui.theme.Pond

@Composable
fun Text(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = Pond.typo.body,
    modifyStyle: ((TextStyle) -> TextStyle)? = null,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis
) = BasicText(
    text = text,
    color = { color },
    style = modifyStyle?.let { it(style) } ?: style,
    modifier = modifier,
    maxLines = maxLines,
    overflow = overflow
)

@Composable
fun Text(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = Pond.typo.body,
    modifyStyle: ((TextStyle) -> TextStyle)? = null,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis
) = BasicText(
    text = text,
    color = { color },
    style = modifyStyle?.let { it(style) } ?: style,
    modifier = modifier,
    maxLines = maxLines,
    overflow = overflow
)

@Composable
fun Label(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = Pond.typo.label,
    modifyStyle: ((TextStyle) -> TextStyle)? = null,
    color: Color = Pond.localColors.contentDim,
    maxLines: Int = 1
) = Text(
    text = text,
    modifier = modifier,
    style = style,
    modifyStyle = modifyStyle,
    color = color,
    maxLines = maxLines
)

@Composable
fun H1(
    text: String,
    modifier: Modifier = Modifier,
    modifyStyle: ((TextStyle) -> TextStyle)? = null,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE
) = Text(
    text = text,
    modifier = modifier,
    style = Pond.typo.h1,
    modifyStyle = modifyStyle,
    color = color,
    maxLines = maxLines
)

@Composable
fun H2(
    text: String,
    modifier: Modifier = Modifier,
    modifyStyle: ((TextStyle) -> TextStyle)? = null,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE
) = Text(
    text = text,
    modifier = modifier,
    style = Pond.typo.h2,
    modifyStyle = modifyStyle,
    color = color,
    maxLines = maxLines
)

@Composable
fun H3(
    text: String,
    modifier: Modifier = Modifier,
    modifyStyle: ((TextStyle) -> TextStyle)? = null,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE
) = Text(
    text = text,
    modifier = modifier,
    style = Pond.typo.h3,
    modifyStyle = modifyStyle,
    color = color,
    maxLines = maxLines
)

@Composable
fun H4(
    text: String,
    modifier: Modifier = Modifier,
    modifyStyle: ((TextStyle) -> TextStyle)? = null,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE
) = Text(
    text = text,
    modifier = modifier,
    style = Pond.typo.h4,
    modifyStyle = modifyStyle,
    color = color,
    maxLines = maxLines
)

@Composable
fun H5(
    text: String,
    modifier: Modifier = Modifier,
    modifyStyle: ((TextStyle) -> TextStyle)? = null,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE
) = Text(
    text = text,
    modifier = modifier,
    style = Pond.typo.h5,
    modifyStyle = modifyStyle,
    color = color,
    maxLines = maxLines
)