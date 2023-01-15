import Styles.mainContent
import androidx.compose.runtime.*
import api.PromptPack
import app.softwork.routingcompose.BrowserRouter
import app.softwork.routingcompose.Router
import components.*
import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposableInBody

val http = HttpClient(Js) {
    install(ContentNegotiation) { json(DefaultJson) }
}

const val appName = "Good Day Bad Day"

fun main() {
    renderComposableInBody {
        var promptPack by remember { mutableStateOf<PromptPack?>(null) }

        Style(Styles)
        Style(ComponentStyles)

        LaunchedEffect(promptPack) {
            document.title = promptPack?.name ?: appName
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
                                // todo app.openEditor()
                                //      checks if connected yet, if not, show modal
                                router.navigate("/editor")
                            }
                        }) {
                            Text("🖊")
                        }
                    }
                    PromptPacks(router)
                }
            }

            route("editor") {
                Editor(router)
            }

            route("pack") {
                string { promptPackId ->
                    content {
                        PromptPackHeader {
                            router.navigate("/")
                        }
                        PromptPackPage(promptPackId) {
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
fun PromptPacks(router: Router) {
    PromptPackCard(PromptPack()) {
        router.navigate("/pack/${1}")
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
