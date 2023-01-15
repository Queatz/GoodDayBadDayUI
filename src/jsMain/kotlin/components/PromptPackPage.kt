package components

import Styles
import androidx.compose.runtime.*
import api.PromptPack
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import splitAll

@Composable
fun PromptPackPage(id: String, onLoad: (PromptPack) -> Unit) {
    var promptPack by remember { mutableStateOf<PromptPack?>(null) }
    var prompt by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(id) {
        // todo load it
        delay(100)
        promptPack = PromptPack().apply {
            prompts = listOf(
                "@A walks into a stump and stubs their toe. @B has to stop for 3 hours to laugh.",
                "@A wakes up in the middle of the night to pee, but they're lazy so they just pee on @B and fall back asleep.",
            )
        }
    }

    LaunchedEffect(promptPack) {
        prompt = promptPack?.prompts?.first()
    }

    Div({
        style {
            padding(1.5.cssRem)
            margin(1.cssRem, 0.cssRem)
            borderRadius(1.cssRem)
            backgroundColor(Styles.colors.background)
            fontSize(24.px)
            lineHeight("1.5")
            property("box-shadow", "0 0 1rem rgba(0 0 0 / 12%)")
        }
    }) {
        prompt?.parse()
    }
    Div({
        style {
            borderRadius(1.cssRem)
            backgroundColor(Color("coral"))
            padding(1.cssRem)
            fontSize(24.px)
            textAlign("center")
            alignSelf(AlignSelf.Center)
            margin(1.cssRem)
            lineHeight("1")
            cursor("pointer")
            property("box-shadow", "0 0 1rem rgba(0 0 0 / 12%)")
            backgroundImage("linear-gradient(to top, transparent, rgba(255 255 255 / 25%))")
            border(1.px, LineStyle.Solid, Color("rgba(0 0 0 / 50%)"))
        }
        onClick {
            prompt = promptPack?.prompts?.filter { it != prompt }?.randomOrNull()
        }
    }) {
        Text("🗘 Shuffle")
    }
}

@Composable
private fun String.parse() {
    splitAll("@\\w+".toRegex()).map {
        if (it.startsWith("@")) {
            PersonName(it.drop(1))
        } else {
            Text(it)
        }
    }
}

@Composable
fun PersonName(name: String) {
    Span({
        style {
            fontWeight("bold")
            color(Color("coral"))
            property("border-bottom", "2px solid coral")
        }
    }) {
        Text("Person $name")
    }
}
