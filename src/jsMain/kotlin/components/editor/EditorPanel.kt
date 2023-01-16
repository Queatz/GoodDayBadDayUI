package components.editor

import androidx.compose.runtime.*
import api.Person
import api.PromptPack
import components.Loading
import components.PromptPackCard
import kotlinx.browser.window
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.B
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun EditorPanel(me: Person, myPromptPacks: List<PromptPack>, selectedPromptPack: PromptPack?, onNewName: (String) -> Unit, onNewPromptPack: suspend () -> Unit, onDisconnect: () -> Unit, onPromptPackSelected: (PromptPack) -> Unit) {
    val scope = rememberCoroutineScope()
    var createPackLoading by remember { mutableStateOf(false) }

    Div({
        style {
            marginBottom(1.cssRem)
            property("word-break", "break-all")
        }
    }) {
        Text("Connected as ")
        B {
            Text(me.name ?: "Unknown")
        }
        Button({
            style {
                marginLeft(.5.cssRem)
                lineHeight("1.25")
            }
            title("Edit name")
            onClick {
                val result = window.prompt("Edit name", me.name ?: "")

                if (result != null) {
                    onNewName(result)
                }
            }
        }) {
            Text("🖉")
        }
        Button({
            style {
                marginLeft(.5.cssRem)
                lineHeight("1.25")
            }
            title("Disconnect")
            onClick {
                onDisconnect()
            }
        }) {
            Text("🗙")
        }
    }
    if (me.active != true) {
        Div({
            style {
                padding(1.cssRem)
                backgroundColor(Color("peachpuff"))
                borderRadius(1.cssRem)
            }
        }) {
            B {
                Text("Spam control:")
            }
            Text(" This connection is not yet trusted by the site stewards, please reach out to them.  In the meantime, you can get started on a Prompt Pack.")
        }
    }
    Button({
        style {
            margin(1.cssRem)
            alignSelf(AlignSelf.Center)
        }
        onClick {
            scope.launch {
                createPackLoading = true
                onNewPromptPack()
                createPackLoading = false
            }
        }
    }) {
        Text("New Prompt Pack")
    }
    if (createPackLoading) {
        Loading(false)
    }
    myPromptPacks.forEach { promptPack ->
        PromptPackCard(promptPack, isSelected = selectedPromptPack == promptPack) {
            onPromptPackSelected(promptPack)
        }
    }
}
