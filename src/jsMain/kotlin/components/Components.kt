package components

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.ExperimentalComposeWebApi
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@OptIn(ExperimentalComposeWebApi::class)
object ComponentStyles : StyleSheet() {
    val iconButton by style {
        borderRadius(36.px)
        fontWeight("bold")
        fontSize(125.percent)
        height(36.px)
        width(36.px)
        display(DisplayStyle.Flex)
        justifyContent(JustifyContent.Center)
        alignItems(AlignItems.Center)
        lineHeight("1")
        cursor("Pointer")
        transitions {
            "background-color" {
                duration = 100.ms
            }
        }
        (self + hover) {
            backgroundColor(Color("rgba(0 0 0 / 25%)"))
        }
    }

    val spin by keyframes {
        from {
            transform { rotate(0.deg) }
        }
        to {
            transform { rotate(360.deg) }
        }
    }

    val loading by style {
        margin(1.cssRem)
        animation(spin) {
            duration(2.s)
            iterationCount(null)
            timingFunction(AnimationTimingFunction.Linear)
        }
    }
}

@Composable
fun Loading(onBackground: Boolean) {
    Div({
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignItems(AlignItems.Center)
            fontSize(24.px)
            color(if (onBackground) Color.white else Color.slategray)
        }
    }) {
        Span({
            classes(ComponentStyles.loading)
        }) {
            Text("☯")
        }
    }
}

@Composable
fun IconButton(text: String, onClick: () -> Unit) {
    Div({
        classes(ComponentStyles.iconButton)
        onClick {
            onClick()
        }
    }) {
        Text(text)
    }
}
