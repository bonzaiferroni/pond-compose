package newsref.app.pond.controls

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
import newsref.app.pond.theme.*

@Composable
fun Text(
    text: String,
    color: Color = Pond.localColors.content,
    style: TextStyle = Pond.typo.body,
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
    color: Color = Pond.localColors.content,
    style: TextStyle = Pond.typo.body,
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
    color: Color = Pond.localColors.contentDim,
    style: TextStyle = Pond.typo.label,
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    modifier = modifier,
    style = style,
    maxLines = maxLines,
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
)

@Composable
fun H2(
    text: String,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle? = null,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    style = Pond.typo.h2.merge(style),
    maxLines = maxLines,
    modifier = modifier
)

@Composable
fun H3(
    text: String,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle? = null,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    style = Pond.typo.h3.merge(style),
    maxLines = maxLines,
    modifier = modifier
)

@Composable
fun H4(
    text: String,
    color: Color = Pond.localColors.content,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle? = null,
    modifier: Modifier = Modifier
) = BasicText(
    text = text,
    color = { color },
    style = Pond.typo.h4.merge(style),
    maxLines = maxLines,
    modifier = modifier
)

@Composable
fun OtherText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = TextStyle.Default,
) {
    val textColor = color.takeOrElse { Pond.localColors.content }

    BasicText(
        text,
        modifier,
        style.merge(
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign ?: TextAlign.Unspecified,
            lineHeight = lineHeight,
            fontFamily = fontFamily,
            textDecoration = textDecoration,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing
        ),
        onTextLayout,
        overflow,
        softWrap,
        maxLines,
        minLines
    )
}

@Composable
fun OtherH1(
    text: String,
) = OtherText(text, style = Pond.typo.h1)