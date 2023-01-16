import org.jetbrains.compose.web.css.*

object Styles : StyleSheet() {
    object colors {
        val background = Color("white")
        val primary = Color("rgb(185, 213, 181)")
    }
    val mainContent by style {
        boxSizing("border-box")
        maxWidth(640.px)
        property("margin", "0 auto")
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
    }

    val topBar by style {
        padding(1.cssRem)
        backgroundColor(colors.background)
        borderRadius(0.cssRem, 0.cssRem, 1.cssRem, 1.cssRem)
        display(DisplayStyle.Flex)
        alignItems(AlignItems.Center)
        backgroundImage("linear-gradient(to top, transparent, rgba(255 255 255 / 25%))")
        property("border-bottom", "1px solid rgba(0 0 0 / 50%)")
        property("border-left", "1px solid rgba(0 0 0 / 50%)")
        property("border-right", "1px solid rgba(0 0 0 / 50%)")
        property("box-shadow", "0 0 1rem rgba(0 0 0 / 12%)")

        media(mediaMaxWidth(640.px)) {
            self style {
                property("border-left", "none")
                property("border-right", "none")
                borderRadius(0.cssRem)
            }
        }
    }

    init {
        selector("input, textarea") style {
            fontFamily("inherit")
            property("border", "1px solid rgba(0 0 0 / 25%)")
            marginBottom(.5.cssRem)
            property("padding", ".5rem 1rem")
            padding(.5.cssRem, 1.cssRem)
            borderRadius(1.cssRem)
            backgroundColor(Color("rgba(255 255 255 / 50%)"))
        }
        selector("input:focus, textarea:focus") style {
            outline("2px solid slategray")
        }
        selector("textarea") style {
            property("resize", "none")
        }
    }
}
