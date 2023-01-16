import androidx.compose.runtime.mutableStateOf
import api.Person

val state = State()

class State {
    var me = mutableStateOf<Person?>(null)
}
