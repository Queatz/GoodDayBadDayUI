package components.editor

import Styles
import androidx.compose.runtime.*
import api.PromptPack
import api.api
import content
import io.ktor.client.plugins.*
import kotlinx.browser.window
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.ATarget
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.attributes.target
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun EditorPromptPackDetails(promptPack: PromptPack, onSave: () -> Unit, onDelete: () -> Unit) {
    val scope = currentRecomposeScope
    val coroutineScope = rememberCoroutineScope()
    var saveJob by remember { mutableStateOf<Job?>(null) }
    var newPrompt by remember { mutableStateOf("") }
    var dirty by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(promptPack) {
        newPrompt = ""
        dirty = null
    }

    fun save(delay: Duration = 0.seconds) {
        saveJob?.cancel()
        saveJob = coroutineScope.launch {
            delay(delay)

            try {
                api.updatePack(promptPack)
                onSave()
                dirty = false
            } catch (e: ResponseException) {
                window.alert(e.response.status.description)
            } finally {
                saveJob = null
            }
        }
    }

    LaunchedEffect(dirty) {
        if (dirty != true) return@LaunchedEffect
        save(5.seconds)
    }

    fun modified() {
        scope.invalidate()
        dirty = true
    }

    Header({
        style {
            padding(1.cssRem)
            backgroundColor(Color(promptPack.color ?: "#fff"))
            backgroundImage("linear-gradient(to top, transparent, rgba(255 255 255 / 25%))")
            property("border-bottom", "1px solid rgba(0 0 0 / 50%)")
            property("border-left", "1px solid rgba(0 0 0 / 50%)")
            property("border-right", "1px solid rgba(0 0 0 / 50%)")
            property("box-shadow", "0 0 1rem rgba(0 0 0 / 12%)")
        }
    }) {
        H2({
            style {
                margin(0.cssRem)
            }
        }) {
            TextInput(promptPack.name ?: "") {
                style {
                    property("font-size", "inherit")
                    width(16.cssRem)
                }
                title("Give your Prompt Pack a name. Ex. \"Cabin Trip\"")
                onInput {
                    promptPack.name = it.value
                    modified()
                }
            }
            Text(" ")
            Button({
                onClick {
                    promptPack.active = promptPack.active != true
                    scope.invalidate()
                    save()
                }
            }) {
                Text(if (promptPack.active == true) "Unpublish" else "Publish")
            }
            Text(" ")
            Button({
                style {
                    lineHeight("1.25")
                }
                title("Trash this Prompt Pack")
                onClick {
                    val result = window.prompt("Enter the name of the Prompt Pack to permanently trash it.")

                    if (result == (promptPack.name ?: "")) {
                        onDelete()
                    }
                }
            }) {
                Text("\uD83D\uDDD1")
            }
            A(href = "/pack/${promptPack.id}", {
                target(ATarget.Blank)
                style {
                    marginLeft(1.cssRem)
                    fontSize(80.percent)
                    textDecoration("none")
                    property("vertical-align", "middle")
                }
            }) {
                Text("Preview")
            }
        }
        Div {
            Text("by ")
            TextInput(promptPack.author ?: "") {
                title("Credit the author")
                onInput {
                    promptPack.author = it.value
                    modified()
                }
            }
        }
        Div {
            Text("for ")
            TextInput(promptPack.level ?: "") {
                title("Describe the level of friendship. Ex. \"really close friends\"")
                onInput {
                    promptPack.level = it.value
                    modified()
                }
            }
        }
        Div {
            Text("🎨 ")
            TextInput(promptPack.color ?: "") {
                title("Any HTML color")
                onInput {
                    promptPack.color = it.value
                    modified()
                }
            }
        }
        TextArea(promptPack.description ?: "") {
            placeholder("Description")
            title("Add a few more details for people to better understand the contents of this Prompt Pack. Ex. \"Great for playing on trips with friends.\"")
            onInput {
                promptPack.description = it.value
                modified()
            }
        }
        if (dirty != null) {
            Div({
                style {
                    marginTop(.5.cssRem)
                }
            }) {
                Span({
                    style {
                        opacity(.5)
                    }
                }) {
                    Text(if (dirty == false) "Changes saved" else "Changes not saved")
                }
                if (dirty == true) {
                    Text(" ")
                    Button({
                        onClick {
                            save()
                        }
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
    Div({
        style {
            padding(1.cssRem)
        }
    }) {
        content {
            promptPack.prompts!!.forEachIndexed { index, prompt ->
                TextArea(prompt) {
                    style {
                        marginBottom(1.cssRem)
                        fontSize(24.px)
                        padding(1.5.cssRem)
                        height(12.cssRem)
                        width(100.percent)
                        lineHeight("1.5")
                        backgroundColor(Styles.colors.background)
                    }
                    onInput {
                        promptPack.prompts = promptPack.prompts!!.toMutableList().also { l ->
                            l[index] = it.value
                        }.toList()
                        modified()
                    }
                    onKeyDown {
                        if (it.key == "Backspace" && prompt.isBlank()) {
                            promptPack.prompts = promptPack.prompts!!.toMutableList().also { l ->
                                l.removeAt(index)
                            }.toList()
                            it.preventDefault()
                            modified()
                        }
                    }
                }
            }
            H3 {
                Text("Add a prompt")
            }
            TextArea(newPrompt) {
                style {
                    marginBottom(1.cssRem)
                    fontSize(24.px)
                    padding(1.5.cssRem)
                    height(12.cssRem)
                    width(100.percent)
                    lineHeight("1.5")
                    backgroundColor(Styles.colors.background)
                }
                placeholder("Enter a prompt")
                onInput {
                    newPrompt = it.value
                }
                onKeyDown {
                    if (it.key == "Enter") {
                        promptPack.prompts = promptPack.prompts!! + newPrompt
                        newPrompt = ""
                        it.preventDefault()
                        modified()
                    }
                }
            }
            Div({
                style {
                    opacity(.5)
                    padding(0.cssRem, .5.cssRem)
                }
            }) {
                Text("Press Enter to add. Use @A, @B to insert Person A and Person B\n")
            }
        }
    }
}
