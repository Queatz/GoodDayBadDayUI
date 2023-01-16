package components

import androidx.compose.runtime.Composable
import api.PromptPack
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text

@Composable
fun PromptPackCard(promptPack: PromptPack, isSelected: Boolean, onClick: () -> Unit) {
    Div({
        style {
            padding(1.cssRem)
            marginBottom(1.cssRem)
            borderRadius(1.cssRem)
            cursor("pointer")
            backgroundColor(Color(promptPack.color ?: "#fff"))
            border(1.px, LineStyle.Solid, Color("rgba(0 0 0 / 50%)"))
            if (isSelected) {
                outline("2px solid rgba(0 0 0 / 50%)")
            }
            property("box-shadow", "0 0 1rem rgba(0 0 0 / 12%)")
            backgroundImage("linear-gradient(to top, transparent, rgba(255 255 255 / 25%))")
        }
        onClick {
            onClick()
        }
    }) {
        H2({
            style {
                margin(0.cssRem)
            }
        }) {
            Text(promptPack.name ?: "")
        }
        Div({
            style {
                marginBottom(1.cssRem)
            }
        }) {
            Text("by ${promptPack.author ?: "Unknown Author"}")
        }
        Div({
            style {
                marginBottom(1.cssRem)
            }
        }) {
            Text(promptPack.description ?: "")
        }
        Div({
            style {
                opacity(.5)
                fontStyle("italic")
            }
        }) {
            Text("${promptPack.prompts!!.size} ${if (promptPack.prompts!!.size == 1) "prompt" else "prompts"} for ${promptPack.level ?: "friends"}")
        }
    }
}
