package components

import Styles
import androidx.compose.runtime.Composable
import api.PromptPack
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Header
import org.jetbrains.compose.web.dom.Text

@Composable
fun PromptPackHeader(promptPack: PromptPack, goBack: () -> Unit) {
    Header({
        classes(Styles.topBar)
        style {
            backgroundColor(Color(promptPack.color ?: "#fff"))
        }
    }) {
        IconButton("\uD83E\uDC68") {
            goBack()
        }
        Div({
            style {
                marginLeft(.5.cssRem)
            }
        }) {
            H2({
                style {
                    margin(0.cssRem)
                }
            }) {
                Text(promptPack.name ?: "")
            }
            Div {
                Text("by ${promptPack.author ?: "Unknown Author"}")
            }
        }
    }
}
