package components

import Styles
import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Header
import org.jetbrains.compose.web.dom.Text

@Composable
fun PromptPackHeader(goBack: () -> Unit) {
    Header({
        style {
            padding(1.cssRem)
            marginBottom(1.cssRem)
            backgroundColor(Styles.colors.background)
            borderRadius(0.cssRem, 0.cssRem, 1.cssRem, 1.cssRem)
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            backgroundColor(Color("coral"))
            backgroundImage("linear-gradient(to top, transparent, rgba(255 255 255 / 25%))")
            property("border-bottom", "1px solid rgba(0 0 0 / 50%)")
            property("box-shadow", "0 0 1rem rgba(0 0 0 / 12%)")
        }
    }) {
        IconButton("🡨") {
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
                Text("On the trail")
            }
            Div {
                Text("by Nate Ferrero")
            }
        }
    }
}
