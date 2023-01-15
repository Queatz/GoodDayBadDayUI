package components

import Styles
import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun HowToPlay(showHowToPlay: Boolean, toggle: () -> Unit) {
    Aside({
        style {
            padding(0.5.cssRem, 1.cssRem)
            backgroundColor(Styles.colors.background)
            property("box-shadow", "0 0 1rem rgba(0 0 0 / 12%)")
            borderRadius(1.cssRem)
        }
    }) {
        H1(
            {
                style {
                    cursor("pointer")
                    display(DisplayStyle.Flex)
                    justifyContent(JustifyContent.SpaceBetween)
                }
                onClick {
                    toggle()
                }
            }
        ) {
            Div { Text("How to play!") }
            Div { Text(if (showHowToPlay) "⏷" else "⏶") }
        }

        if (showHowToPlay) Div({
            style {
                whiteSpace("pre-wrap")
            }
        }) {
            H2 {
                Text("1. Setup")
            }
            P {
                Text("Place a pile of beans in the middle.  The number of beans determines the length of the game.  Everyone gets one Good Day and one Bad Day card. Choose a starting player.")
            }
            H2 {
                Text("2. Rounds")
            }
            P {
                Text(
                    """
                    The round starts with the player to the left of the player who is it.  This player asks a prompt to the player who is it.  The player who is it then places either their Good Day or Bad Day card face-down.  Not including these two players, all other players place their Good Day or Bad Day cards face-down with their guess of what the player who is it placed down.  The player who is it must remain silent.

                    Reveal all cards.  The player who is it explains why it was a good or bad day.  All players that guessed correctly get a bean.

                    The player to the left of the player who just asked the prompt then asks the player who is it a different prompt.  Repeat until all players have asked a prompt except the player who is it.  The round then ends and the player to the left of the player who is it becomes it.
                    """.trimIndent()
                )
            }
            H2 {
                Text("3. Winning")
            }
            P {
                Text("The game ends when the pile of beans runs out.  The player with the most beans wins.")
            }
        }
    }
}
