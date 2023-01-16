package components

import Styles
import androidx.compose.runtime.*
import api.Person
import api.PromptPack
import api.api
import app.softwork.routingcompose.Router
import components.editor.EditorConnect
import components.editor.EditorPanel
import components.editor.EditorPromptPackDetails
import io.ktor.client.plugins.*
import kotlinx.browser.window
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import state

@Composable
fun Editor(router: Router) {
    var me by remember { state.me }
    var myPromptPacks by remember { mutableStateOf(emptyList<PromptPack>()) }
    var selectedPromptPack by remember { mutableStateOf<PromptPack?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val scope = currentRecomposeScope

    fun disconnect() {
        me = null
        myPromptPacks = emptyList()
        selectedPromptPack = null
    }

    fun reload() {
        coroutineScope.launch {
            try {
                myPromptPacks = api.myPacks()
                selectedPromptPack = myPromptPacks.firstOrNull()
            } catch (e: ResponseException) {
                window.alert(e.response.status.description)
            }
        }
    }

    fun changeName(name: String) {
        coroutineScope.launch {
            try {
                api.updateMe(Person(name = name))
                me?.name = name
                scope.invalidate()
            } catch (e: ResponseException) {
                window.alert(e.response.status.description)
            }
        }
    }

    LaunchedEffect(me) {
        if (me != null) {
            reload()
        }
    }

    suspend fun new() {
        try {
            val promptPack = api.createPack()
            myPromptPacks = listOf(promptPack) + myPromptPacks
            selectedPromptPack = myPromptPacks.first()
        } catch (e: ResponseException) {
            window.alert(e.response.status.description)
        }
    }

    LaunchedEffect(selectedPromptPack) {
        window.scrollTo(0.0, 0.0)
    }

    Div({
        style {
            display(DisplayStyle.Flex)
            boxSizing("border-box")
            minHeight(100.vh)
        }
    }) {
        Aside({
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                height(100.vh)
                minHeight(100.vh)
                maxHeight(100.vh)
                width(20.cssRem)
                position(Position.Fixed)
                boxSizing("border-box")
                backgroundColor(Styles.colors.background)
                property("box-shadow", "0 0 1rem rgba(0 0 0 / 12%)")
                padding(1.cssRem)
                property("z-index", "1")
                overflow("hidden auto")
            }
        }) {
            H3({
                style {
                    marginTop(0.cssRem)
                    display(DisplayStyle.Flex)
                    alignItems(AlignItems.Center)
                }
            }) {
                Button({
                    style {
                        marginRight(.5.cssRem)
                    }
                    title("Go back")
                    onClick {
                        router.navigate("/")
                    }
                }) {
                    Text("🡨")
                }
                Text("Prompt Pack Editor")
            }
            if (me == null) {
                EditorConnect {
                    me = it
                }
            } else {
                EditorPanel(me!!, myPromptPacks, selectedPromptPack, {
                    changeName(it)
                }, { new() }, { disconnect() }) {
                    selectedPromptPack = it
                }
            }
        }
        Section({
            style {
                display(DisplayStyle.Flex)
                minHeight(100.vh)
                flexGrow(1)
                marginLeft(20.cssRem)
                flexDirection(FlexDirection.Column)
            }
        }) {
            if (selectedPromptPack != null) {
                EditorPromptPackDetails(selectedPromptPack!!, {
                    scope.invalidate()
                }) {
                    coroutineScope.launch {
                        try {
                            api.deletePack(selectedPromptPack!!.id!!)
                            reload()
                        } catch (e: ResponseException) {
                            window.alert(e.response.status.description)
                        }
                    }
                }
            }
        }
    }
}
