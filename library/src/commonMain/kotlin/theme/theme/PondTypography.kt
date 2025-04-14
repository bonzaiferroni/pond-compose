package newsref.app.pond.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import newsref.app.generated.resources.Inter_18pt_Light
import newsref.app.generated.resources.Inter_18pt_Regular
import newsref.app.generated.resources.Inter_24pt_Light
import newsref.app.generated.resources.Inter_28pt_Light
import newsref.app.generated.resources.Res
import newsref.app.generated.resources.forum_regular
import org.jetbrains.compose.resources.FontResource

interface PondTypography {
    val title: TextStyle
    val h1: TextStyle
    val h2: TextStyle
    val h3: TextStyle
    val h4: TextStyle
    val body: TextStyle
    val label: TextStyle
}

@Composable
fun DefaultTypography() = object : PondTypography {
    override val title = base.copy(
        fontSize = 20.sp,
    )
    override val h1 = base.copy(
        fontSize = 28.sp,
        fontWeight = FontWeight.Light,
        fontFamily = useFamily(Res.font.Inter_28pt_Light, FontWeight.Light),
    )
    override val h2 = base.copy(
        fontSize = 24.sp,
        fontWeight = FontWeight.Light,
        fontFamily = useFamily(Res.font.Inter_24pt_Light, FontWeight.Light),
    )
    override val h3 = base.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.Light,
        fontFamily = useFamily(Res.font.Inter_24pt_Light, FontWeight.Light),
    )
    override val h4 = base.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Light,
        fontFamily = useFamily(Res.font.Inter_18pt_Light, FontWeight.Light),
    )
    override val body = base
    override val label = base.copy(
        fontSize = 12.sp
    )
}

private val base @Composable get() = TextStyle.Default.copy(
    fontFamily = useFamily(Res.font.Inter_18pt_Regular)
)

@Composable
fun PlayfairFontFamily() = FontFamily(
    Font(Res.font.forum_regular, weight = FontWeight.Normal),
)

@Composable
fun useFamily(resource: FontResource, weight: FontWeight = FontWeight.Normal) = FontFamily(
    Font(resource, weight = weight)
)