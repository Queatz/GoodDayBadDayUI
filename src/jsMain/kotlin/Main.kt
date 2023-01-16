import Styles.mainContent
import androidx.compose.runtime.*
import api.PromptPack
import api.api
import app.softwork.routingcompose.BrowserRouter
import app.softwork.routingcompose.Router
import components.*
import io.ktor.client.plugins.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposableInBody

const val appName = "Good Day Bad Day"

fun main() {
    renderComposableInBody {
        Style(Styles)
        Style(ComponentStyles)

        var promptPack by remember { mutableStateOf<PromptPack?>(null) }

        LaunchedEffect(promptPack) {
            document.title = promptPack?.name?.let { "$it • $appName" } ?: appName
        }

        BrowserRouter("") {
            val router = Router.current

            LaunchedEffect(router.currentPath) {
                window.scrollTo(0.0, 0.0)
                document.title = appName
            }

            route("") {
                var showHowToPlay by remember { mutableStateOf(false) }

                content {
                    Header({
                        style {
                            padding(1.cssRem)
                        }
                    }) {
                        AppName()
                    }
                    HowToPlay(showHowToPlay) { showHowToPlay = !showHowToPlay }
                    H3({
                        style {
                            padding(1.cssRem)
                            color(Styles.colors.background)
                            textAlign("center")
                        }
                    }) {
                        Span { Text("Prompt Packs") }
                        Span({
                            title("Go to Prompt Pack Editor")
                            style {
                                marginLeft(1.cssRem)
                                cursor("pointer")
                            }
                            onClick {
                                router.navigate("/editor")
                            }
                        }) {
                            Text("\uD83D\uDD8A")
                        }
                    }
                    PromptPacks {
                        router.navigate("/pack/${it.id}")
                    }
                }
            }

            route("editor") {
                Editor(router)
            }

            route("pack") {
                string { promptPackId ->
                    content {
                        PromptPackPage(promptPackId, {
                            router.navigate("/")
                        }) {
                            promptPack = it
                        }
                    }
                }
                noMatch {
                    redirect("/")
                }
            }

            noMatch {
                redirect("/")
            }
        }
    }
}

@Composable
fun content(content: @Composable () -> Unit) {
    Div({
        classes(mainContent)
    }) {
        content()
    }
}

@Composable
fun PromptPacks(onClick: (PromptPack) -> Unit) {
    var promptPacks by remember { mutableStateOf(listOf<PromptPack>()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        loading = true
        try {
            promptPacks = api.packs()
        } catch (e: ResponseException) {
            window.alert("Error loading Prompt Packs")
        } finally {
            loading = false
        }
    }

    if (loading) {
        Loading(onBackground = true)
    } else {
        promptPacks.forEach { promptPack ->
            PromptPackCard(promptPack, false) {
                onClick(promptPack)
            }
        }
    }
}

@Composable
fun AppName() {
    H3({
        style {
            textAlign("center")
            color(Styles.colors.background)
            property("text-shadow", "1px 1px 0 #00000069")
        }
    }) {
        Span {
            Text("Good Day")
        }
        Span({
            style {
                opacity(0.75)
            }
        }) {
            Text(" Bad Day")
        }
    }
}
