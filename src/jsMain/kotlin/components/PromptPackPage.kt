package components

import Styles
import androidx.compose.runtime.*
import api.PromptPack
import api.api
import io.ktor.client.plugins.*
import kotlinx.browser.window
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import splitAll

@Composable
fun PromptPackPage(id: String, goBack: () -> Unit, onLoad: (PromptPack) -> Unit) {
    var promptPack by remember { mutableStateOf<PromptPack?>(null) }
    var prompt by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(id) {
        scope.launch {
            try {
                promptPack = api.pack(id)
                onLoad(promptPack!!)
            } catch (e: ResponseException) {
                window.alert(e.response.status.description)
            }
        }
    }

    LaunchedEffect(promptPack) {
        prompt = promptPack?.prompts?.first()
    }

    if (promptPack == null) {
        Loading(onBackground = true)
        return
    }

    PromptPackHeader(promptPack!!) {
        goBack()
    }

    Div({
        style {
            padding(1.5.cssRem)
            margin(1.cssRem)
            borderRadius(1.cssRem)
            backgroundColor(Styles.colors.background)
            fontSize(24.px)
            lineHeight("1.5")
            property("box-shadow", "0 0 1rem rgba(0 0 0 / 12%)")
        }
    }) {
        prompt?.parse(promptPack?.color?.takeIf {
            it.lowercase() != "#fff" && it.lowercase() != "#ffffff" && it.lowercase() != "white"
        } ?: "coral")
    }
    Div({
        style {
            borderRadius(1.cssRem)
            backgroundColor(Color(promptPack?.color ?: "#fff"))
            padding(1.cssRem)
            fontSize(24.px)
            textAlign("center")
            alignSelf(AlignSelf.Center)
            lineHeight("1")
            cursor("pointer")
            property("user-select", "none")
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
private fun String.parse(color: String) {
    splitAll("@\\w+".toRegex()).map {
        if (it.startsWith("@")) {
            PersonName(it.drop(1), color)
        } else {
            Text(it)
        }
    }
}

@Composable
fun PersonName(name: String, color: String) {
    Span({
        style {
            fontWeight("bold")
            color(Color(color))
            property("border-bottom", "2px solid $color")
        }
    }) {
        Text("Person $name")
    }
}
