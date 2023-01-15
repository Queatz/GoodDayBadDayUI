package api

import kotlinx.serialization.Serializable

@Serializable
class Person(
    var id: String? = null,
    var name: String? = null,
)

@Serializable
class PromptPack(
    var id: String? = null,
    var name: String? = null,
    var color: String? = null,
    var author: String? = null,
    var level: String? = null,
    var active: Boolean? = null,
    var description: String? = null,
    var prompts: List<String> = emptyList()
)
