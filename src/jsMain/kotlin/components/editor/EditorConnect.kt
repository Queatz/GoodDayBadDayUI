package components.editor

import androidx.compose.runtime.*
import api.Person
import api.api
import components.Loading
import io.ktor.client.plugins.*
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.name
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.PasswordInput
import org.jetbrains.compose.web.dom.Text

@Composable
fun EditorConnect(onMe: (Person) -> Unit) {
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun connect(password: String) {
        scope.launch {
            loading = true
            try {
                api.password = password
                onMe(api.me(password))
            } catch (e: ResponseException) {
                window.alert(e.response.status.description)
            } finally {
                loading = false
            }
        }
    }

    if (loading) {
        Loading(false)
        return
    }

    var password by remember { mutableStateOf("") }
    Div({
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignItems(AlignItems.Center)
        }
    }) {
        PasswordInput(password) {
            placeholder("Password")
            name("password")
            onInput {
                password = it.value
            }
            onKeyDown {
                if (it.key == "Enter") {
                    connect(password)
                    password = ""
                }
            }
        }
        if (password.isNotEmpty()) {
            Button({
                style {
                    marginTop(1.cssRem)
                }
                onClick {
                    if (password.isNotBlank()) {
                        connect(password)
                        password = ""
                    }
                }
            }) {
                Text("Launch")
            }
        }
    }
}
