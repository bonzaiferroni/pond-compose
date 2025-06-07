package pondui.ui.controls

import androidx.compose.runtime.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import pondui.ui.theme.Pond

@Composable
fun Text(
    text: String,
    style: TextStyle = Pond.typo.body,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    style = style,
    modifier = modifier,
    maxLines = maxLines,
    overflow = overflow
)

@Composable
fun Text(
    text: AnnotatedString,
    style: TextStyle = Pond.typo.body,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    style = style,
    modifier = modifier,
    maxLines = maxLines,
    overflow = overflow
)

@Composable
fun Label(
    text: String,
    style: TextStyle = Pond.typo.label,
    color: Color = Pond.localColors.contentDim,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    modifier = modifier,
    style = style,
    maxLines = maxLines,
    overflow = TextOverflow.Ellipsis
)

@Composable
fun H1(
    text: String,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    style = Pond.typo.h1,
    maxLines = maxLines,
    overflow = TextOverflow.Ellipsis,
    modifier = modifier,
)

@Composable
fun H2(
    text: String,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    style = Pond.typo.h2,
    maxLines = maxLines,
    overflow = TextOverflow.Ellipsis,
    modifier = modifier
)

@Composable
fun H3(
    text: String,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    style = Pond.typo.h3,
    maxLines = maxLines,
    overflow = TextOverflow.Ellipsis,
    modifier = modifier
)

@Composable
fun H4(
    text: String,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    style = Pond.typo.h4,
    maxLines = maxLines,
    overflow = TextOverflow.Ellipsis,
    modifier = modifier
)

@Composable
fun H5(
    text: String,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    style = Pond.typo.h5,
    maxLines = maxLines,
    overflow = TextOverflow.Ellipsis,
    modifier = modifier
)