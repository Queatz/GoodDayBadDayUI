package components

import Styles
import androidx.compose.runtime.*
import api.Person
import api.PromptPack
import app.softwork.routingcompose.Router
import content
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.name
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun Editor(router: Router) {
    var me by remember { mutableStateOf<Person?>(null) }
    var myPromptPacks by remember { mutableStateOf(emptyList<PromptPack>()) }
    var selectedPromptPack by remember { mutableStateOf<PromptPack?>(null) }
    var loading by remember { mutableStateOf(true) }

    fun disconnect() {
        me = null
        myPromptPacks = emptyList()
        selectedPromptPack = null
    }

    LaunchedEffect(true) {
        delay(200)
        loading = false
        // todo check for login with localStorage password
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
                minHeight(100.vh)
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
            if (loading) {
                Loading()
            } else if (me == null) {
                EditorConnect {
                    me = it
                    myPromptPacks = listOf(
                        PromptPack(name = "On the trail"),
                        PromptPack(name = "In the woods", description = "What would you do alone in the woods with your friends?")
                    )
                }
            } else {
                EditorPanel(me!!, myPromptPacks, selectedPromptPack, { disconnect() }) {
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
                EditorPromptPackDetails(selectedPromptPack!!)
            }
        }
    }
}

@Composable
fun EditorConnect(onMe: (Person) -> Unit) {
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    fun connect(password: String) {
        scope.launch {
            loading = true
            try {
                delay(500)
            } finally {
                loading = false
            }
            onMe(Person(name = password))
        }
    }

    if (loading) {
        Loading()
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
                    connect(password)
                    password = ""
                }
            }) {
                Text("Launch")
            }
        }
    }
}

@Composable
fun EditorPanel(me: Person, myPromptPacks: List<PromptPack>, selectedPromptPack: PromptPack?, onDisconnect: () -> Unit, onPromptPackSelected: (PromptPack) -> Unit) {
    Div({
        style {
            marginBottom(1.cssRem)
            property("word-break", "break-all")
        }
    }) {
        Text("Connected as ")
        B {
            Text(me.name!!)
        }
        Button({
            style {
                marginLeft(.5.cssRem)
            }
            title("Disconnect")
            onClick {
                onDisconnect()
            }
        }) {
            Text("🗙")
        }
    }
    myPromptPacks.forEach { promptPack ->
        PromptPackCard(promptPack) {
            onPromptPackSelected(promptPack)
        }
    }
}

@Composable
fun EditorPromptPackDetails(promptPack: PromptPack) {
    val scope = currentRecomposeScope
    var newPrompt by remember { mutableStateOf("") }

    LaunchedEffect(promptPack) {
        newPrompt = ""
    }

    Header({
        style {
            padding(1.cssRem)
            backgroundColor(Color(promptPack.color ?: "#fff"))
            backgroundImage("linear-gradient(to top, transparent, rgba(255 255 255 / 25%))")
            property("border-bottom", "1px solid rgba(0 0 0 / 50%)")
            property("box-shadow", "0 0 1rem rgba(0 0 0 / 12%)")
        }
    }) {
        H2({
            style {
                marginTop(0.cssRem)
            }
        }) {
            Text(promptPack.name!!)
            Text(" ")
            Button({
                onClick {
                    promptPack.active = promptPack.active != true
                    scope.invalidate()
                    // todo save
                }
            }) {
                Text(if (promptPack.active == true) "Deactivate" else "Activate")
            }
            Text(" ")
            Button({
                style {
                    lineHeight("1.25")
                }
                title("Delete this Prompt Pack")
                onClick {
                    val result = window.prompt("Enter the name of the Prompt Pack to delete it forever.")

                    if (result == promptPack.name) {
                        // todo delete
                    }
                }
            }) {
                Text("🗑")
            }
        }
        Div {
            Text("by ")
            TextInput(promptPack.author ?: "") {
                onInput {
                    promptPack.author = it.value
                    scope.invalidate()
                    // todo debounce save
                }
            }
        }
        Div {
            Text("for ")
            TextInput(promptPack.level ?: "") {
                onInput {
                    promptPack.level = it.value
                    scope.invalidate()
                    // todo debounce save
                }
            }
        }
        Div {
            Text("🎨 ")
            TextInput(promptPack.color ?: "") {
                onInput {
                    promptPack.color = it.value
                    scope.invalidate()
                    // todo debounce save
                }
            }
        }
        TextArea(promptPack.description ?: "") {
            placeholder("Description")
            onInput {
                promptPack.description = it.value
                scope.invalidate()
            }
        }
    }
    Div({
        style {
            padding(1.cssRem)
        }
    }) {
        content {
            promptPack.prompts.forEachIndexed { index, prompt ->
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
                        promptPack.prompts = promptPack.prompts.toMutableList().also { l ->
                            l[index] = it.value
                        }.toList()
                        scope.invalidate()
                        // todo debounce save
                    }
                    onKeyDown {
                        if (it.key == "Backspace" && prompt.isBlank()) {
                            promptPack.prompts = promptPack.prompts.toMutableList().also { l ->
                                l.removeAt(index)
                            }.toList()
                            it.preventDefault()
                            scope.invalidate()
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
                placeholder("Enter prompt here")
                onInput {
                    newPrompt = it.value
                }
                onKeyDown {
                    if (it.key == "Enter") {
                        promptPack.prompts = promptPack.prompts + newPrompt
                        newPrompt = ""
                        it.preventDefault()
                        scope.invalidate()
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
